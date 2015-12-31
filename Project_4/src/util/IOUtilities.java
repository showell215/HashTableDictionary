package util;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Utilities class that contains static methods for basic IO operations. 
 * @author Steven Howell (schowel2)
 *
 */
public class IOUtilities {
	
	/**
	 * Prompts user for a file name, and attempts to create a Scanner
	 * to read from that file.
	 * @param in Scanner for user input
	 * @return a Scanner to read file
	 */
	public static Scanner getFileScanner(Scanner in, String msg) {
		Scanner fileScanner = null;
		while (fileScanner == null) {
			System.out.print("Enter input filename" + msg + ": ");
			String fname = in.nextLine();
			try {
				fileScanner = new Scanner(new File(fname));
			} catch (FileNotFoundException e) {
				System.out.println("Error reading file.");
			}
		}
		return fileScanner;
	}
	/**
	 * Prompts user for a file name, and attempts to create a 
	 * PrintStream to write to that file.
	 * @param in Scanner for user input
	 * @return PrintStream to write to file
	 */
	public static PrintStream getOutputStream(Scanner in, String msg) {
		PrintStream output = null;
		while (output == null) {
			System.out.print("Enter output filename" +  msg + ": ");
			String fname = in.nextLine();
			try {
				output = new PrintStream(new File(fname));
			} catch (IOException e) {
				System.out.println("Error creating file.");
			}
		}
		return output;
	}
}
