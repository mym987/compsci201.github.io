package ngram;
import java.util.*;

/**
 * The model for the Markov text generation assignment. See methods for
 * details.  This model should use a brute-force algorithm for
 * generating text, i.e., the entire training text is rescanned each
 * time a new character is generated.
 * 
 * modified by: @author Mike Ma
 * 
 */

public class MarkovModel implements INgram {
    private String myString;
    private Random myRandom;
    
    public MarkovModel(Random random) {
        myRandom = random;
    }
    
    public int initialize(Scanner s) {
    	myString = s.useDelimiter("\\Z").next();
        s.close();
        return myString.length();
    }
    
    public String makeNGram(int k, int maxLetters) {
        // Appending to StringBuilder is faster than appending to String
        StringBuilder build = new StringBuilder();
        // Pick a random starting index
        int start = myRandom.nextInt(myString.length() - k + 1);
        String seed = myString.substring(start, start + k);
        ArrayList<Character> list = new ArrayList<Character>();
        // generate at most maxLetters characters
        for (int i = 0; i < maxLetters; i++) {
            list.clear();
            int pos = 0;
            while ((pos = myString.indexOf(seed, pos)) != -1 && pos < myString.length()) {
            	if (pos + k >= myString.length())
            		list.add((char) 0);
            	else
            		list.add(myString.charAt(pos + k));
                pos++;
            }
            int pick = myRandom.nextInt(list.size());
            char ch = list.get(pick);
            if (ch == 0) //end-of-file
            	return build.toString();
            build.append(ch);
            seed = seed.substring(1) + ch;
        }
        return build.toString();
    }
}
