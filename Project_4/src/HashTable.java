/**
 * Class representing implemenation of a Hash Table using the separate
 * chaining method of collision resolution. Hash Table is an array, with
 * each index containing a linked list to store words.
 * @author Steven Howell (schowel2)
 *
 */
public class HashTable {
	/** The hash table, represented as an array of linked lists containing
	 * words hashed to that index */
	private LinkedList[] wordArray;
	/** The number of probes performed for this session */
	private int probes = 0;
	/** The Number of lookups performed for this session */
	private int lookups = 0;
	/** The number of words in the hash table */
	private int words = 0;
	
	/** The size of the hash table */
	private static final int M = 34763;
	/** Constant used in the hash function */
	private static final int HASH_CONSTANT = 7;
	
	/**
	 * Constructs a new hash table with a size of 34763
	 */
	public HashTable() {
		wordArray = new LinkedList[M];
	}

	/**
	 * Caller method for inserting a word into the dictionary.
	 * Calls hash() to get the hash code.
	 * Calls add() to insert into hash table.
	 * @param word the word to insert
	 */
	public void insert(String word) {
		add(hash(word), word);
		this.words++;
	}
	/**
	 * Gets a hash code for this word.
	 * @param word the word to hash
	 * @return the hash code for this word
	 */
	private int hash(String word) {
		long hash = 1;
		int ch;
		int sLen = word.length();
		for (int i = 0; i < sLen; i++) {
			ch = word.charAt(i);
			//multiply by psn to account for order
			//and by length to account for length
			hash = ((hash * HASH_CONSTANT * sLen) + (ch * (i + 1)));
		}
		//Compression: mod by m
		int h = (int)(hash % M);
		//in case of overflow, turn negative value into positive
		if (h < 0) {
			h = Math.abs(h) % M;
		}
		return h;
	}
	/**
	 * Add a word to the dictionary at the specified index into the hash table.
	 * @param idx the index at which to insert the word
	 * @param word the word to insert
	 */
	private void add(int idx, String word) {
		if (wordArray[idx] == null) { //1st word mapped to this idx
			wordArray[idx] = new LinkedList(word);
		} else { // not the 1st word mapped to this idx
			wordArray[idx].insert(word);
		}
	}
	/**
	 * Did we find the word we are searching for? Searches the linked list
	 * at this index for the word. Linked list is not sorted -- max list size
	 * will be 5 items for "dict.txt", so uses a simple list traversal.
	 * 
	 * Increments lookup count once
	 * Increments probe count for each item examined in the list
	 * 
	 * @param word the word we are searching for
	 * @param hash the index word hashed to
	 * @return true if we find a match; false otherwise
	 */
	private boolean isMatch(String word, int hash) {
		String lookupWord = null;
		LinkedList.Node p = null;
		try {
			lookups++;
			p = wordArray[hash].head;
		} catch (NullPointerException e) {
			return false; //empty dictionary element
					      //don't count a probe
		}
		//traverse list at this hash table index to search for word
		while (p != null) {
			lookupWord = p.word;
			probes++; //count a probe for each word examined
			if (word.equals(lookupWord)) {
				return true;
			} 
			p = p.next;
		}
		//not in list, return false
		return false;
	}
	/**
	 * Checks the spelling of a word. First, looks up the full word, then attempts
	 * to strip of endings and searches again.
	 * @param s the word to check
	 * @return true if a match is found in dictionary; false otherwise
	 */
	public boolean spellCheck(String s) {
		//hash the word 
		int hash = hash(s);
		int sLen = s.length();
		
		//try the full word as is
		if (isMatch(s, hash)) {
			return true;
			//don't return false yet - try stripping endings
		}
		//if necessary perform other checks by removing endings
		String ss = null; //temp string to strip endings
		//ends in appostrophe s?
		if (s.endsWith("'s") || s.endsWith("’s") || s.endsWith("'s")) {
			ss = s.substring(0, sLen - 2);
			hash = hash(ss);
			return isMatch(ss, hash);
		//ends in "s"?
		} else if (s.endsWith("s")){
			ss = s.substring(0, sLen - 1);
			hash = hash(ss);
			if (isMatch(ss, hash)) {
				return true;
				//don't return false yet - try dropping "es"
			}
			//ends in "es"?
			if (s.endsWith("es")) {
				ss = s.substring(0, sLen - 2);
				hash = hash(ss);
				//if no match after dropping "es", no match exists
				return isMatch(ss, hash);
			}
		//ends in "ed"?
		} else if (s.endsWith("ed")) {
			ss = s.substring(0, sLen - 2);
			hash = hash(ss);
			if (isMatch(ss, hash)) {
				return true;
				//don't return false yet - try just dropping "d"
			}
			//try just dropping "d"
			ss = s.substring(0, sLen - 1);
			hash = hash(ss);
			//if no match after dropping "d", no match exists
			return isMatch(ss, hash);
		//ends in "er"?
		} else if (s.endsWith("er")) {
			ss = s.substring(0, sLen - 2);
			hash = hash(ss);
			if (isMatch(ss, hash)) {
				return true;
				//don't return false yet - try dropping "r"
			}
			//try just dropping "r"
			ss = s.substring(0, sLen - 1);
			hash = hash(ss);
			//if no match after dropping "r", no match possible
			return isMatch(ss, hash);
		//ends in "ing"?
		} else if (s.endsWith("ing")) {
			ss = s.substring(0, sLen - 3);
			hash = hash(ss);
			if (isMatch(ss, hash)) {
				return true;
				//don't return false yet - try replacing "ing" w/ "e"
			}
			//try replacing "ing" with "e"
			StringBuilder sb = new StringBuilder(s.substring(0, sLen -3 ));
			sb.append("e");
			ss = sb.toString();
			hash = hash(ss);
			//if no match, no match possible
			return isMatch(ss, hash);
		} else if (s.endsWith("ly")) {
			ss = s.substring(0, sLen - 2);
			hash = hash(ss);
			return isMatch(ss, hash);
		}
		//if no match so far, false
		return false;
	}
	/**
	 * Get the number of probes for this session
	 * @return the number of probes
	 */
	public int getProbes() {
		return this.probes;
	}
	/**
	 * Get the number of lookups for this session
	 * @return the number of lookups
	 */
	public int getLookups() {
		return this.lookups;
	}
	/**
	 * Get the number of words in the dictionary
	 * @return the number of words in the dictionary
	 */
	public int getWordCount() {
		return this.words;
	}
	/**
	 * Convert this hash table to string format (for use in testing)
	 * @return String representation of this hash table
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < M; i++) {
			if (wordArray[i] != null) {
				sb.append(wordArray[i].toString() + "\n");
			} else {
				sb.append("null\n");
			}
		}
		return sb.toString();
				
	}
//-------------------------------begin LinkedList class-------------------------------//
	/**
	 * Linked List implementation to be used in this hash table. Will
	 * store linked list of words at each table index.
	 * @author Steven Howell (schowel2)
	 *
	 */
	private class LinkedList {
		/** The head, containing 1st word hashed to this index */
		public Node head;
		/** number of elements in this list */
		public int size = 0;
		
		/** 
		 * Create a new linked list with this word s as the head 
		 * @param word the 1st word hashed to this index
		 */
		public LinkedList(String word) {
			this.head = new Node(word);
			this.size++;
		}
		/**
		 * Insert a new word at the end of the list
		 * Traverse the list to find the end
		 * @param word the word to insert
		 */
		public void insert(String word) {
			Node p = this.head;
			while (p.next != null) {
				p = p.next;
			}
			p.next = new Node(word);
			this.size++;
		}
		/**
		 * Get a String representation of this list
		 * @return a String representation of the list
		 */
		public String toString() {
			StringBuilder sb = new StringBuilder();
			Node p = this.head;
			while (p != null) {
				sb.append(p.word + "\t");
				p = p.next;
			}
			return sb.toString();
		}
//-------------------------------begin Node class-------------------------------//
		/**
		 * Private class defining a linked list node.
		 * Contains the word and a pointer to the next word.
		 * @author Steven Howell (schowel2)
		 *
		 */
		private class Node {
			/** The word */
			public String word;
			/** The next word */
			public Node next;
			
			/** 
			 * Create a new node containing this word.
			 * @param word the word
			 */
			public Node(String word) {
				this.word = word;
				next = null;
			}
		}
	}
}
