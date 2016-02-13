package ngram;

import java.util.Scanner;

/**
 * The interface for the Markov text generation assignment. This interface
 * specifies all methods that a Markov model should implement.
 * 
 * @author Mike Ma
 * 
 */
public interface INgram {
	/**
	 * Create a new training text for this model based on the information read
	 * from the scanner.
	 * 
	 * @param s
	 *            is the source of information
	 *            
	 * @return number of tokens read from the scanner
	 */
	public int initialize(Scanner s);

	/**
	 * Generates random text that is similar to the reference text (myString).
	 * 
	 * @param k
	 *            order of n-gram
	 * @requires k > 0
	 * @param maxLetters
	 *            number of characters to generate
	 * @return maxLetters of randomly selected characters based on picking
	 *         representative characters that follow each k characters
	 */
	public String makeNGram(int k, int maxLetters);

}
