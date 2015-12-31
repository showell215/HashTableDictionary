import java.io.PrintStream;
import java.util.Scanner;

import util.IOUtilities;

/**
 * Main class for the Spell Checker program. Contains methods to:
 * 		- Read an input file to create a dictionary
 * 		- Read an input file to check, and parse words to spellcheck
 * Calls methods from HashTable class to check spelling of words in input file.
 * Outputs results to specified output file
 * @author Steven Howell (schowel2)
 *
 */
public class SpellCheckMain {
	/** HashTable object for this session */
	private HashTable ht;
	/** Number of misspelled words */
	private int misspelled = 0;
	/** Number of words to check from the input file */
	private int wordsToCheck = 0;

	/**
	 * Constructs a new SpellCheckMain object. Creates a new hash table, reads input files,
	 * creates an output file, and calls supporting methods.  
	 */
	public SpellCheckMain() {
		ht = new HashTable();
		Scanner console = new Scanner(System.in);
		Scanner dictInput = IOUtilities.getFileScanner(console, " for Dictionary");
		readDict(dictInput);
		Scanner textInput = IOUtilities.getFileScanner(console, " for File to check");
		PrintStream output = IOUtilities.getOutputStream(console, "");
		
		/**
		//For testing
		PrintStream testDictOut = null;
		try {
			testDictOut = new PrintStream(new File("dictOUT.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		testDictOut.println(ht.toString());
		*/
		spellCheck(textInput, output);
		output.println("Words in dictionary: " + ht.getWordCount());
		output.println("Words in text: " + wordsToCheck);
		output.println("Misspelled words: " + misspelled);
		output.println("Total probes: " + ht.getProbes());
		output.println("Average probes per word checked: "
						+ (double)ht.getProbes() / wordsToCheck);
		output.println("Average probes per lookup operation: "
						+ (double)ht.getProbes() / ht.getLookups());		
	}
	/**
	 * Read input file to check, parse each word, and check spelling.
	 * @param input input file to spellcheck
	 * @param output output file
	 */
	private void spellCheck(Scanner input, PrintStream output) {
		boolean newSentence = false; //change to lower case?	
		boolean hyphenated = false; //two words to check?
		String word2 = null; //2nd word variable to use if hyphen found
		while (input.hasNext()) {
			boolean newSentenceNext = false;
			String word = input.next();
			wordsToCheck++;
			//quotes?
			if (word.startsWith("\"") || word.startsWith("“") || word.startsWith("'")) {
				word = word.substring(1, word.length());
			}
			//ending quotes after punctuation
			if (word.endsWith("\"") || word.endsWith("'") || word.endsWith("”")) {
				word = word.substring(0, word.length() - 1);
			}
			//other punctuation attached to word
			if (word.endsWith(";") || word.endsWith(",") || word.endsWith(":")) {
				word = word.substring(0, word.length() - 1);
			}
			//end of sentence
			if (word.endsWith(".") || word.endsWith("?") || word.endsWith("!")) {
				word = word.substring(0, word.length() - 1);
				//flag the next word to lower-case
				newSentenceNext = true;
			}
			//ending quotes before punctuation
			if (word.endsWith("\"") || word.endsWith("'") || word.endsWith("”")) {
				word = word.substring(0, word.length() - 1);
			}
			//dict.txt contains "i" instead of "I"
			if (word.equals("I")) {
				word = word.toLowerCase();
			}
			//hyphenated?
			if (word.contains("-")) {
				int psn = word.indexOf('-');
				//2nd word is after hyphen to end
				word2 = word.substring(psn + 1, word.length());
				//1st word is 0 to hyphen - 1
				word = word.substring(0, psn);
			}
			//first word in document, same case as newSentence
			if (wordsToCheck == 1) {
				newSentence = true;
			}
			//if beginning of sentence
			if (newSentence) {
				newSentence = newSentenceNext;
				if (!isCorrect(word)) {
					word = word.toLowerCase();
				} else {
					//if we found it, go to next word
					continue;
				}
			}
			newSentence = newSentenceNext;
			//check word
			if (!isCorrect(word)) {
				output.println(word);
				this.misspelled++;
			}
			//if hyphenated flag, check 2nd word if exists
			if (hyphenated && word2 != "" && word2 != null) {
				wordsToCheck++;
				if (!isCorrect(word2)) {
					output.println(word2);
					this.misspelled++;
				}
			}
		}
	}
	/**
	 * Read the dictionary file and insert words into hash table.
	 * @param input the input file
	 */
	private void readDict(Scanner input) {
		while (input.hasNext()) {
			ht.insert(input.next());
		}
	}
	/**
	 * Is this word correctly spelled?
	 * @param word the word to check
	 * @return true if correct; false otherwise.
	 */
	private boolean isCorrect(String word) {
		return ht.spellCheck(word);
	}

	/**
	 * Main method. Starts the program.
	 * @param args command line arguments - not use
	 */
	public static void main(String[] args) {
		new SpellCheckMain();
	}
}
