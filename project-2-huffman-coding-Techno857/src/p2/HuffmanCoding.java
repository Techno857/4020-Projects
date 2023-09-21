package p2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import p2.DataStructures.List.List;
import p2.DataStructures.Map.HashTableSC;
import p2.DataStructures.Map.Map;
import p2.DataStructures.SortedList.SortedLinkedList;
import p2.DataStructures.SortedList.SortedList;
import p2.DataStructures.Tree.BTNode;
import p2.Utils.BinaryTreePrinter;

/**
 * The Huffman Encoding Algorithm
 * 
 * This is a data compression algorithm designed 
 * by David A. Huffman and published in 1952
 * 
 * What it does is it takes a string and by constructing 
 * a special binary tree with the frequencies of each character.
 * 
 * This tree generates special prefix codes that make the size 
 * of each string encoded a lot smaller, thus saving space.
 * 
 * @author Fernando J. Bermudez Medina (Template) 
 * @author ADD YOUR NAME HERE - 802212115
 * @version 3.0
 * @since 03/28/2023
 */
public class HuffmanCoding {

	public static void main(String[] args) {
		HuffmanEncodedResult();
	}

	/* This method just runs all the main methods developed or the algorithm */
	private static void HuffmanEncodedResult() {
		/* You can create other test input files and add them to the inputData Folder */
		String data = load_data("input1.txt");

		/* If input string is not empty we can encode the text using our algorithm */
		if(!data.isEmpty()) {
			Map<String, Integer> fD = compute_fd(data);
			BTNode<Integer,String> huffmanRoot = huffman_tree(fD);
			Map<String,String> encodedHuffman = huffman_code(huffmanRoot);
			String output = encode(encodedHuffman, data);
			process_results(fD, encodedHuffman,data,output);
		} else 
			System.out.println("Input Data Is Empty! Try Again with a File that has data inside!");
		

	}

	/**
	 * Receives a file named in parameter inputFile (including its path),
	 * and returns a single string with the contents.
	 * 
	 * @param inputFile name of the file to be processed in the path inputData/
	 * @return String with the information to be processed
	 */
	public static String load_data(String inputFile) {
		BufferedReader in = null;
		String line = "";

		try {
			/**
			 * We create a new reader that accepts UTF-8 encoding and 
			 * extract the input string from the file, and we return it
			 */
			in = new BufferedReader(new InputStreamReader(new FileInputStream("inputData/" + inputFile), "UTF-8"));

			/**
			 * If input file is empty just return an 
			 * empty string, if not just extract the data
			 */
			String extracted = in.readLine();
			if(extracted != null)
				line = extracted;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) 
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return line;
	}

	/**
	 * computes the frequency distribution of each character 
	 * in the input string and returns a map with the count of each character 
	 * as values and the characters themselves as keys.
	 * 
	 * @param inputString String who's frequency distribution will be computed
	 * @return Map with the frequency distribution of each character in the input string
	 */
	public static Map<String, Integer> compute_fd(String inputString) {

		Map<String, Integer> freqDistrib = new HashTableSC<>();

		for(int i = 0; i < inputString.length(); i++) {
			String charac = String.valueOf(inputString.charAt(i));
			if (freqDistrib.get(charac) == null)
				freqDistrib.put(charac, 1);
			else
				freqDistrib.put(charac, freqDistrib.get(charac) + 1);
		}
		return freqDistrib;
	}

	/**
	 * Method that creates a sorted linked list of BTNodes from the frequency distribution map
	 * passsed as parameter, and then constructs the Huffman Tree from the sorted list.
	 * 
	 * @param fD Map containing the frequency distribution of each character in its original input string
	 * @return Btnode that is the root of the Huffman Tree
	 */
	public static BTNode<Integer, String> huffman_tree(Map<String, Integer> fD) {

		/* TODO Construct Huffman Tree */
		int size = fD.size();
		SortedList<BTNode<Integer,String>> sortedList = new SortedLinkedList<>();

		for(String key: fD.getKeys()){
			BTNode<Integer,String> node = new BTNode<>(fD.get(key), key);
			sortedList.add(node);
		}
		for (int i = 0; i < size - 1; i++){
			BTNode<Integer,String> tempRoot = new BTNode<>();
			BTNode<Integer,String> x = sortedList.removeIndex(0);
			BTNode<Integer,String> y = sortedList.removeIndex(0);

			tempRoot.setLeftChild(x);
			tempRoot.setRightChild(y);

			tempRoot.setKey(x.getKey() + y.getKey());
			tempRoot.setValue(x.getValue() + y.getValue());
			
			sortedList.add(tempRoot);
		}
		BinaryTreePrinter.print(sortedList.get(0));
		
		return sortedList.get(0);
//		BTNode<Integer,String> temp = new BTNode<>();
//		return temp;
	}

	/**
	 * Method that traverses the Huffman Tree and generates the prefix codes for each character
	 * using the huffmanHelper auxiliary method.
	 * 
	 * @param huffmanRoot - Root of the Huffman Tree to be traversed
	 * @return Map with the prefix codes for each character in the input string
	 */
	public static Map<String, String> huffman_code(BTNode<Integer,String> huffmanRoot) {
		/* TODO Construct Prefix Codes */
		//self note: create void helper function that takes in root and map and empty string
		Map<String, String> prefixCodes = new HashTableSC<>();
		huffmanHelper(huffmanRoot, prefixCodes, "");

		return prefixCodes;
	}

	/**
	 * Method in charge of encoding the input string using the prefix codes generated by the huffman_code method.
	 * The method traverses the input string and uses the encodingMap to get the prefix code for each character.
	 * 
	 * @param encodingMap - Map to be used to decode the input string
	 * @param inputString - String to be decoded
	 * @return String with the encoded input string
	 */
	public static String encode(Map<String, String> encodingMap, String inputString) {
		/* TODO Encode String */
		String encryptedString = "";

		for(int i = 0; i < inputString.length(); i++){
			String bit = encodingMap.get(String.valueOf(inputString.charAt(i)));
			encryptedString += bit;
		}
		return encryptedString;
	}

	/**
	 * Receives the frequency distribution map, the Huffman Prefix Code HashTable, the input string, 
	 * and the output string, and prints the results to the screen (per specifications).
	 * 
	 * Output Includes: symbol, frequency and code. 
	 * Also includes how many bits has the original and encoded string, plus how much space was saved using this encoding algorithm
	 * 
	 * @param fD Frequency Distribution of all the characters in input string
	 * @param encodedHuffman Prefix Code Map
	 * @param inputData text string from the input file
	 * @param output processed encoded string
	 */
	public static void process_results(Map<String, Integer> fD, Map<String, String> encodedHuffman, String inputData, String output) {
		/*To get the bytes of the input string, we just get the bytes of the original string with string.getBytes().length*/
		int inputBytes = inputData.getBytes().length;

		/**
		 * For the bytes of the encoded one, it's not so easy.
		 * 
		 * Here we have to get the bytes the same way we got the bytes 
		 * for the original one but we divide it by 8, because 
		 * 1 byte = 8 bits and our huffman code is in bits (0,1), not bytes. 
		 * 
		 * This is because we want to calculate how many bytes we saved by 
		 * counting how many bits we generated with the encoding 
		 */
		DecimalFormat d = new DecimalFormat("##.##");
		double outputBytes = Math.ceil((float) output.getBytes().length / 8);

		/**
		 * to calculate how much space we saved we just take the percentage.
		 * the number of encoded bytes divided by the number of original bytes 
		 * will give us how much space we "chopped off".
		 * 
		 * So we have to subtract that "chopped off" percentage to the total (which is 100%) 
		 * and that's the difference in space required
		 */
		String savings =  d.format(100 - (( (float) (outputBytes / (float)inputBytes) ) * 100));


		/**
		 * Finally we just output our results to the console 
		 * with a more visual pleasing version of both our 
		 * Hash Tables in decreasing order by frequency.
		 * 
		 * Notice that when the output is shown, the characters 
		 * with the highest frequency have the lowest amount of bits.
		 * 
		 * This means the encoding worked and we saved space!
		 */
		System.out.println("Symbol\t" + "Frequency   " + "Code");
		System.out.println("------\t" + "---------   " + "----");

		SortedList<BTNode<Integer,String>> sortedList = new SortedLinkedList<BTNode<Integer,String>>();

		/**
		 * To print the table in decreasing order by frequency, 
		 * we do the same thing we did when we built the tree.
		 * 
		 * We add each key with it's frequency in a node into a SortedList, 
		 * this way we get the frequencies in ascending order
		 */
		for (String key : fD.getKeys()) {
			BTNode<Integer,String> node = new BTNode<Integer,String>(fD.get(key),key);
			sortedList.add(node);
		}

		/**
		 * Since we have the frequencies in ascending order, 
		 * we just traverse the list backwards and start printing 
		 * the nodes key (character) and value (frequency) and find 
		 * the same key in our prefix code "Lookup Table" we made 
		 * earlier on in huffman_code(). 
		 * 
		 * That way we get the table in decreasing order by frequency
		 */
		for (int i = sortedList.size() - 1; i >= 0; i--) {
			BTNode<Integer,String> node = sortedList.get(i);
			System.out.println(node.getValue() + "\t" + node.getKey() + "\t    " + encodedHuffman.get(node.getValue()));
		}

		System.out.println("\nOriginal String: \n" + inputData);
		System.out.println("Encoded String: \n" + output);
		System.out.println("Decoded String: \n" + decodeHuff(output, encodedHuffman) + "\n");
		System.out.println("The original string requires " + inputBytes + " bytes.");
		System.out.println("The encoded string requires " + (int) outputBytes + " bytes.");
		System.out.println("Difference in space requiered is " + savings + "%.");
	}


	/*************************************************************************************
	 ** ADD ANY AUXILIARY METHOD YOU WISH TO IMPLEMENT TO FACILITATE YOUR SOLUTION HERE **
	 *************************************************************************************/
	
	/**
	 * Auxiliary Method that recursively adds the corresponding elements to the huffman tree.
	 * 
	 * @param root - Root of the tree passed into huffman_code()
	 * @param map - Map of the prefix codes generated by the huffman algorithm
	 * @param code - String that will hold the code to be built
	 */
	public static void huffmanHelper(BTNode<Integer,String> root, Map<String, String> map, String code){
		if(root == null){
			return;
		}
		if(root.getLeftChild() == null && root.getRightChild() == null){
			map.put(root.getValue(), code);
		}
		huffmanHelper(root.getLeftChild(), map, code + "0");
		huffmanHelper(root.getRightChild(), map, code + "1");
	}
	/**
	 * Auxiliary Method that decodes the generated string by the Huffman Coding Algorithm
	 * 
	 * Used for output Purposes
	 * 
	 * @param output - Encoded String
	 * @param lookupTable 
	 * @return The decoded String, this should be the original input string parsed from the input file
	 */
	public static String decodeHuff(String output, Map<String, String> lookupTable) {
		String result = "";
		int start = 0;
		List<String>  prefixCodes = lookupTable.getValues();
		List<String> symbols = lookupTable.getKeys();

		/**
		 * Loop through output until a prefix code is found on map and 
		 * adding the symbol that the code that represents it to result 
		 */
		for(int i = 0; i <= output.length();i++){

			String searched = output.substring(start, i);

			int index = prefixCodes.firstIndex(searched);

			if(index >= 0) { // Found it!
				result= result + symbols.get(index);
				start = i;
			}
		}
		return result;    
	}
}