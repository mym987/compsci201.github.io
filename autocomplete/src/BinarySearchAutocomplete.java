import java.util.Arrays;
import java.util.Comparator;

/**
 * 
 * Using a sorted array of Term objects, this implementation uses binary search
 * to find the top term(s).
 * 
 * @author Austin Lu, adapted from Kevin Wayne
 *
 */
public class BinarySearchAutocomplete implements Autocompletor {

	Term[] myTerms;

	/**
	 * Given arrays of words and weights, initialize _terms to a corresponding
	 * array of Terms sorted lexicographically.
	 * 
	 * This constructor is written for you, but you may make modifications to
	 * it.
	 * 
	 * @param terms
	 *            - A list of words to form terms from
	 * @param weights
	 *            - A corresponding list of weights, such that terms[i] has
	 *            weight[i].
	 * @return a BinarySearchAutocomplete whose _terms object has _terms[i] = a
	 *         Term with word terms[i] and weight weights[i].
	 * @throws a
	 *             NullPointerException if either argument passed in is null
	 */
	public BinarySearchAutocomplete(String[] terms, double[] weights) {
		if (terms == null || weights == null)
			throw new NullPointerException("One or more arguments null");
		myTerms = new Term[terms.length];
		for (int i = 0; i < terms.length; i++) {
			myTerms[i] = new Term(terms[i], weights[i]);
		}
		Arrays.sort(myTerms);
	}

	public BinarySearchAutocomplete(Term[] terms) {
		myTerms = terms;
		Arrays.sort(myTerms);
	}

	/**
	 * Uses binary search to find the index of the first Term in the passed in
	 * array which is considered equivalent by a comparator to the given key.
	 * This method should not call comparator.compare() more than 1+log n times,
	 * where n is the size of a.
	 * 
	 * @param a
	 *            - The array of Terms being searched
	 * @param key
	 *            - The key being searched for.
	 * @param comparator
	 *            - A comparator, used to determine equivalency between the
	 *            values in a and the key.
	 * @return The first index i for which comparator considers a[i] and key as
	 *         being equal. If no such index exists, return -1 instead.
	 */
	static int firstIndexOf(Term[] a, Term key, Comparator<Term> comparator) {
		int start = 0, end = a.length - 1;
		while (start < end - 1) {
			int i = (start + end) / 2;
			if (comparator.compare(a[i], key) >= 0) {
				end = i;
			} else {
				start = i;
			}
		}
		int cmp = comparator.compare(a[start], key);
		if (cmp < 0) {
			if (comparator.compare(a[end], key) == 0)
				return end;
			else
				return -1;
		} else if (cmp == 0)
			return start;
		else
			return -1;
	}

	/**
	 * The same as firstIndexOf, but instead finding the index of the last Term.
	 * 
	 * @param a
	 *            - The array of Terms being searched
	 * @param key
	 *            - The key being searched for.
	 * @param comparator
	 *            - A comparator, used to determine equivalency between the
	 *            values in a and the key.
	 * @return The last index i for which comparator considers a[i] and key as
	 *         being equal. If no such index exists, return -1 instead.
	 */
	static int lastIndexOf(Term[] a, Term key, Comparator<Term> comparator) {
		int start = 0, end = a.length - 1;
		while (start < end - 1) {
			int i = (start + end) / 2;
			if (comparator.compare(a[i], key) <= 0) {
				start = i;
			} else {
				end = i;
			}
		}
		int cmp = comparator.compare(a[end], key);
		if (cmp > 0) {
			if (comparator.compare(a[start], key) == 0)
				return start;
			else
				return -1;
		} else if (cmp == 0)
			return end;
		else
			return -1;
	}

	/**
	 * Required by the Autocompletor interface. Returns an array containing the
	 * k words in _terms with the largest weight which match the given prefix,
	 * in descending weight order. If less than k words exist matching the given
	 * prefix (including if no words exist), then the array instead contains all
	 * those words. e.g. If terms is {air:3, bat:2, bell:4, boy:1}, then
	 * topKMatches("b", 2) should return {"bell", "bat"}, but topKMatches("a",
	 * 2) should return {"air"}
	 * 
	 * @param prefix
	 *            - A prefix which all returned words must start with
	 * @param k
	 *            - The (maximum) number of words to be returned
	 * @return An array of the k words with the largest weights among all words
	 *         starting with prefix, in descending weight order. If less than k
	 *         such words exist, return an array containing all those words If
	 *         no such words exist, reutrn an empty array
	 * @throws NullPointerException
	 *             if prefix is null
	 */
	public String[] topKMatches(String prefix, int k) {
		Comparator<Term> cmp = new Term.PrefixOrder(prefix.length());
		int start = firstIndexOf(myTerms, new Term(prefix, 0), cmp);
		if (start == -1)
			return new String[0];
		int end = lastIndexOf(myTerms, new Term(prefix, 0), cmp);
		return Arrays.stream(myTerms, start, end+1)
				.sorted(new Term.ReverseWeightOrder())
				.map(t -> t.getWord()).distinct().limit(k)
				.toArray(n -> new String[n]);
	}

	@Override
	/**
	 * Given a prefix, returns the largest-weight word in _terms starting with
	 * that prefix. e.g. for {air:3, bat:2, bell:4, boy:1}, topMatch("b") would
	 * return "bell". If no such word exists, return an empty String.
	 * 
	 * @param prefix
	 *            - the prefix the returned word should start with
	 * @return The word from _terms with the largest weight starting with
	 *         prefix, or an empty string if none exists
	 * @throws NullPointerException
	 *             if the prefix is null
	 * 
	 */
	public String topMatch(String prefix) {
		Comparator<Term> cmp = new Term.PrefixOrder(prefix.length());
		int start = firstIndexOf(myTerms, new Term(prefix, 0), cmp);
		if (start == -1)
			return "";
		int end = lastIndexOf(myTerms, new Term(prefix, 0), cmp);
		return Arrays.stream(myTerms, start, end+1).max(new Term.WeightOrder()).get().getWord();
	}

	public static void main(String[] args) throws Exception{
		Term[] t = {new Term("ape", 6), 
				new Term("app", 4), 
				new Term("ban", 2),
				new Term("bat", 3),
				new Term("bee", 5),
				new Term("car", 7),
				new Term("cat", 1),
				new Term("caa",100)};
		// String[] t1 = new String[5];
		// System.arraycopy(t, 0, t1, 0, 5);
		Autocompletor auto = (Autocompletor) Class.forName("BinarySearchAutocomplete")
	                  .getDeclaredConstructor(String[].class, double[].class).newInstance(null, null);
		BinarySearchAutocomplete b = new BinarySearchAutocomplete(t);
		for (String s : b.topKMatches("ca", 4))
			;//System.out.println(s);
		System.out.println(b.topMatch("ca"));
	}
}
