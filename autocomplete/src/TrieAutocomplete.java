import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * General trie/priority queue algorithm for implementing Autocompletor
 * 
 * @author Austin Lu
 *
 */
public class TrieAutocomplete implements Autocompletor {

	/**
	 * Root of entire trie
	 */
	protected Node myRoot;

	/**
	 * Constructor method for TrieAutocomplete. Should initialize the trie
	 * rooted at myRoot, as well as add all nodes necessary to represent the
	 * words in terms.
	 * 
	 * @param terms
	 *            - The words we will autocomplete from
	 * @param weights
	 *            - Their weights, such that terms[i] has weight weights[i].
	 * @throws a
	 *             NullPointerException if either argument is null
	 */
	public TrieAutocomplete(String[] terms, double[] weights) {
		if (terms == null || weights == null)
			throw new NullPointerException("One or more arguments null");
		// Represent the root as a dummy/placeholder node
		myRoot = new Node('-', null, 0);

		for (int i = 0; i < terms.length; i++) {
			add(terms[i], weights[i]);
		}
	}

	/**
	 * Add the word with given weight to the trie. If word already exists in the
	 * trie, no new nodes should be created, but the weight of word should be
	 * updated.
	 * 
	 * In adding a word, this method should do the following: Create any
	 * necessary intermediate nodes if they do not exist. Update the
	 * subtreeMaxWeight of all nodes in the path from root to the node
	 * representing word. Set the value of myWord, myWeight, isWord, and
	 * mySubtreeMaxWeight of the node corresponding to the added word to the
	 * correct values
	 * 
	 * @throws a
	 *             NullPointerException if word is null
	 * @throws an
	 *             IllegalArgumentException if weight is negative.
	 * 
	 */
	private void add(String word, double weight) {
		Node p = myRoot;
		p.mySubtreeMaxWeight = Math.max(p.mySubtreeMaxWeight, weight);
		if(word==null)
			System.err.println("ERR:NULL WORD");
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			if (!p.children.containsKey(c))
				p.children.put(c, new Node(c, p, weight));
			p = p.getChild(c);
			p.mySubtreeMaxWeight = Math.max(p.mySubtreeMaxWeight, weight);
		}
		p.isWord = true;
		p.myWord = word;
		p.myWeight = weight;
	}

	@Override
	/**
	 * Required by the Autocompletor interface. Returns an array containing the
	 * k words in the trie with the largest weight which match the given prefix,
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
	 *         no such words exist, return an empty array
	 * @throws a
	 *             NullPointerException if prefix is null
	 */
	public String[] topKMatches(String prefix, int k) {
		Node p = find(myRoot, prefix);
		if (p == null)
			return new String[0];
		List<Node> list = new LinkedList<>();
		traverse(p, list);
		return list.stream().sorted(Collections.reverseOrder()).map(n -> n.myWord).distinct().limit(k)
				.toArray(n -> new String[n]);
	}

	private Node find(Node p, String prefix) {
		for (int i = 0; i < prefix.length(); i++) {
			char c = prefix.charAt(i);
			if (p.getChild(c) == null)
				return null;
			p = p.getChild(c);
		}
		return p;
	}

	private void traverse(Node root, List<Node> list) {
		if (root.isWord)
			list.add(root);
		root.children.forEach((k, v) -> traverse(v, list));
	}

	@Override
	/**
	 * Given a prefix, returns the largest-weight word in the trie starting with
	 * that prefix.
	 * 
	 * @param prefix
	 *            - the prefix the returned word should start with
	 * @return The word from _terms with the largest weight starting with
	 *         prefix, or an empty string if none exists
	 * @throws a
	 *             NullPointerException if the prefix is null
	 * 
	 */
	public String topMatch(String prefix) {
		Node p = find(myRoot, prefix);
		return p == null ? "" : find(p, p.mySubtreeMaxWeight).myWord;
	}

	private Node find(Node p, double weight) {
		if (p.isWord && p.myWeight == weight)
			return p;
		for (char ch : p.children.keySet()) {
			if (p.getChild(ch).mySubtreeMaxWeight == weight)
				return find(p.getChild(ch), weight);
		}
		return null;
	}

	public static void main(String[] args) {
		String[] a = { "a", "ab", "abc", "abcd", "abcde" };
		double[] b = { 1, 2, 3, 5, 5 };
		TrieAutocomplete tr = new TrieAutocomplete(a, b);
		// for (String s : tr.topKMatches("ab", 5))
		// System.out.println(s);
		System.out.println(tr.topMatch(""));
		// System.out.println(lastIndexOf(t, new Term("dbcd", 0), new
		// Term.PrefixOrder(6)));
	}

}
