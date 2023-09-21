package police;
import java.io.File;
import java.io.IOException;


public class ArrestOperation {
	/**
	 * Utilizes all methods previously implemented in Poilce Departmentto construct 
	 * the reports with given/desired parameters. 
	 * 
	 * @param args -> Part of the main method.
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{
		PoliceDepartment report1 = new PoliceDepartment("Captain Morgan");
		File folder = new File("inputFiles/case1/flyers");
		File[] folders = folder.listFiles();
		report1.setUpOrganizations("inputFiles/case1");
		for (File flyer : folders) {
			String messageBoss = report1.decipherMessage(flyer.getPath());
			report1.arrest(messageBoss);
		}
		report1.policeReport("results/case1report");
		
		PoliceDepartment report2 = new PoliceDepartment("Captain Morgan");
		folder = new File("inputFiles/case2/flyers");
		folders = folder.listFiles();
		report2.setUpOrganizations("inputFiles/case2");
		for (File flyer : folders) {
			String messageBoss = report2.decipherMessage(flyer.getPath());
			report2.arrest(messageBoss);
		}
		report2.policeReport("results/case2report");
		
//		PoliceDepartment otherCase = new PoliceDepartment("Captain Morgan");
//		folder = new File("inputFiles/input_case_name_here/flyers"); 
//		folders = folder.listFiles();
//		otherCase.setUpOrganizations("inputFiles/input_case_name_here");
//		for (File flyer : folders) {
//			String messageBoss = otherCase.decipherMessage(flyer.getPath());
//			otherCase.arrest(messageBoss);
//		}
//		report2.policeReport("results/input_case_name_here" + "report");
	}
}
