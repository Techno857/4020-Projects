package police;

import criminals.Organization;
import criminals.Member;
import interfaces.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import lists.ArrayList;
import java.util.Arrays;
import interfaces.List;


public class PoliceDepartment {

	private String captainName;
	private List<Organization> criminalOrganizations;
	private int arrestCounter;
	
	/**
	 * Constructor that initializes the variable captainName with the given parameter and 
	 * criminalOrganizations as a new ArrayList of type Organization.
	 * 
	 * @param captain -> Name of the captain managing the report.
	 */
	public PoliceDepartment(String captain) {
		this.captainName = captain;
		this.criminalOrganizations = new ArrayList<Organization>();
	}
	
	public List<Organization> getCriminalOrganizations() {
		
		return this.criminalOrganizations;
	}

	/**
	 * Sets up the organizations for a given case folder. The method will read the directory with
	 * the organizations, sorts them in alphabetical order, then adds them to the criminalOrganizations
	 * list.
	 * 
	 * @param caseFolder -> The folder containing the criminal organizations that will be set up.
	 * @throws IOException
	 */
	public void setUpOrganizations(String caseFolder) throws IOException{	
		String PATH = caseFolder + "/CriminalOrganizations/";

		File directory = new File(PATH);
		File[] organizationNames = directory.listFiles();
		Arrays.sort(organizationNames);

		for (File organizationName : organizationNames) {
			Organization organization = new Organization(PATH + organizationName.getName());
			this.criminalOrganizations.add(organization);
		}
	}
	/**
	 * Parses through the file at the specified caseFolder then determines the organization's index
	 * by using getDigiRoot in the first line of the file deciphers the message in the 
	 * first letter of a word at given by the leader's key. Finally, obtains the first letter of the
	 * word at that index and constructs the leader's nickname and returns it.
	 * 
	 * @param caseFolder -> String path to the case folder with the message file.
	 * @return Nickname of the organization in question
	 * @throws IOException
	 */
	
	public String decipherMessage(String caseFolder) throws IOException {
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(caseFolder));
			
			String currentLine;
			ArrayList<String> fileContents = new ArrayList<String>();
			
			while((currentLine = reader.readLine()) != null) {
				fileContents.add(currentLine);
			}
			
			reader.close();
			
			String serialNumberLine = fileContents.get(0);
			int organizationIndex = getDigiroot(serialNumberLine) - 1;
			Organization currentOrganization = criminalOrganizations.get(organizationIndex);
			
			int keyIndex = currentOrganization.getLeaderKey() - 1;
			
			String nickName = "";
			for (int i = 2; i < fileContents.size(); i++) {
				if (fileContents.get(i).contains("--")) {
					break;
				}
				
				String[] words = fileContents.get(i).split(" ");
				
				if(keyIndex < words.length){
					nickName += words[keyIndex].charAt(0);
				}
				else {
					nickName += " ";
				}
			}
			
			return nickName;
		} 
		catch(IOException exception) {
			exception.printStackTrace();
		}
		
		return "";
	}

	/**
	 * Removes the "#" character at the beginning of the line and calculates the
	 * digi root recursively by continuously adding the numbers until their size is 0.
	 * 
	 * @param numbers -> String of numbers to be obtain the digiroot of.
	 * @return 
	 */
	public int getDigiroot(String numbers) {
		int sum = 0;
		String serialNumber = numbers.replaceAll("#", "");

		for (int i = 0; i < serialNumber.length(); i++) {
			sum += Integer.parseInt(serialNumber.charAt(i) + "");
		}
		if (sum > 9) {
			return getDigiroot(sum + "");
		}
		return sum;
	}
	/**
	 * Creates a list of all the members in an organization then determines whether they are arrested 
	 * or not. Recursively determines which member possesses the largest number of underlings and arrests them if they
	 * have yet to be arrested
	 * 
	 * @param leader -> String representing the name of the leader
	 */
	public void arrest(String leader) {
		for (Organization currOrganization : criminalOrganizations){
			List<Member> membersList = currOrganization.organizationTraversal((Member individual) -> individual.getNickname().toLowerCase().equals(leader.toLowerCase()));
			String newLeader = "";
			int underlingsCounter = 0;
			
			for (Member currMember : membersList) {
				if(!currMember.isArrested()){
					currMember.setArrested(true);
					this.arrestCounter += 1;
				}
				int i = 0;

				for(Member currUnderling : currMember.getUnderlings()) {
					if (!currUnderling.isArrested()) {
						currUnderling.setArrested(true);
						this.arrestCounter += 1;
					}
					
					if(currUnderling.getUnderlings().size() >= underlingsCounter) {
						underlingsCounter = currUnderling.getUnderlings().size();
						newLeader = currUnderling.getNickname();
					}
					i += 1;

					if (i == currMember.getUnderlings().size() && underlingsCounter == 0) {
						return;
					}
					
					if (i == currMember.getUnderlings().size() && underlingsCounter > 0) {
						arrest(newLeader);
					}
				}
			}
		}
	}
	
	/**
	 * Method in charge of parsing the given file and constructing the report utilizing 
	 * the above methods and variables.
	 * 
	 * @param filePath -> Path to file to be worked on.
	 * @throws IOException
	 */
	public void policeReport(String filePath) throws IOException{
		try {
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filePath));
			fileWriter.write("CASE REPORT" + "\n");
			fileWriter.newLine();
			fileWriter.write("In charge of Operation: " + this.captainName + "\n");
			fileWriter.newLine();
			fileWriter.write("Total arrests made: " + this.arrestCounter + "\n");
			fileWriter.newLine();
			fileWriter.write("Current Status of Criminal Organizations:" + "\n");
			fileWriter.newLine();
					
					
			for(Organization currOrg : criminalOrganizations) {
				if(currOrg.getBoss().isArrested()) {
					fileWriter.write("DISSOLVED" + "\n");
				}
				fileWriter.write(currOrg.toString());
				fileWriter.write("---");
				fileWriter.newLine();
			}
			
			fileWriter.write("END OF REPORT");
			fileWriter.close();
			
		}
		catch(IOException exception) {
			exception.printStackTrace();
		}
	}



}
