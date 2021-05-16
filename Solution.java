import java.util.Arrays;
import java.util.LinkedList;
import java.util.Comparator;

public class Solution {

  public static void main(String[] args) {

  public TrieNode root;
  public String[] words;

  /*
  By the problem design on binarysearch.com, we have to work
  around the given method 'public int solve(String[] words)'
  so that the code can be run on the website. Even though the name 'solve'
  does not make a lot of sense, it is left as it is, so that the code can
  be run directly on the website, without any modifications.
  */
  public int solve(String[] words) {
    this.words = words;
    root = new TrieNode();

    return find_totalNumberOfEntirelyConcatenatedWords();
  }

  public int find_totalNumberOfEntirelyConcatenatedWords() {

    Arrays.sort(words, sortWords_fromSmalerToBiggerLength());
    int totalNumberOfEntirelyConcatenatedWords = 0;

    for (String word : words) {
      if (isEntirelyConcatenatedWord(word)) {
        totalNumberOfEntirelyConcatenatedWords++;
      }
      /*
      Since the array is sorted by word length, inserting a word in the Trie is done after the check for concatenation,
      so that the method does not return 'true' for the word matching itself.
      */
      insert(word);
    }
    return totalNumberOfEntirelyConcatenatedWords;
  }

  /*
  The method checks whether a word can be composed entirely of words stored in the Trie.

  Starting from index '0' of the word being checked, the length of each possible concatenation is added to
  the LinkedList 'concatenatedLength'. If there is another possible concatenation from the index immediately
  following the length of the previous concatenation, then the length of the previous concatenations is added
  to the length of the new concatenation, and this sum is added to the LinkedList 'concatenatedLength'.

  This continues until either the total length of the concatenations equals the length of the word being checked,
  or there are no further possible concatenations.

  Example:

  # Words in Trie: binary, binaryse, search.
  # Word being checked: binarysearch.

  1. At index '5', an end of word is found. This is for the concatenation 'binary'.
     It's length (6 chars) is added to the LinkedList.

  2. At index '7', an end of word is found. This is for the concatenation 'binaryse'.
     It's length (8 chars) is added to the LinkedList.

  3. The word 'binary' is removed from the LinkedList and starting from the index immeditately
     after 'binary', i.e. index 6, another check is made. We find a possible concatenation from
     index '6' to index '11'. This is for the concatenation 'search'.The combined length of 'binary'
     and 'search' is equal to the length of the word beng checked.

  4. Therefore, in this particular case, the method returs 'true'.
  */
  public boolean isEntirelyConcatenatedWord(String word) {

    LinkedList<Integer> concatenatedLength = new LinkedList<Integer>();
    concatenatedLength.add(0);

    while (!concatenatedLength.isEmpty()) {

      TrieNode currentTrieNode = root;
      int start = concatenatedLength.removeFirst();
      String concatenation = word.substring(start);

      for (int i = 0; i < concatenation.length(); i++) {

        char ch = concatenation.charAt(i);
        if (currentTrieNode.branches[ch - 'a'] == null) {
          break;
        }

        currentTrieNode = currentTrieNode.branches[ch - 'a'];

        if (currentTrieNode.isEndOfWord) {
          if (start + i == word.length() - 1) {
            return true;
          }
          concatenatedLength.add(start + i + 1);
        }
      }
    }
    return false;
  }

  public void insert(String word) {

    TrieNode currentTrieNode = root;
    for (int i = 0; i < word.length(); i++) {

      char ch = word.charAt(i);
      if (currentTrieNode.branches[ch - 'a'] == null) {
        currentTrieNode.branches[ch - 'a'] = new TrieNode();
      }
      currentTrieNode = currentTrieNode.branches[ch - 'a'];
    }

    currentTrieNode.isEndOfWord = true;
  }

  public Comparator<String> sortWords_fromSmalerToBiggerLength() {

    return new Comparator<String>() {
      @Override
      public int compare(String wordOne, String wordTwo) {
        return wordOne.length() - wordTwo.length();
      }
    };
  }
}

class TrieNode {
  TrieNode[] branches;
  boolean isEndOfWord;

  public TrieNode() {
    this.branches = new TrieNode[26];
  }
}
