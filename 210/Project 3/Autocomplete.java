import java.util.Arrays;
import java.util.Comparator;

import stdlib.In;
import stdlib.StdIn;
import stdlib.StdOut;

public class Autocomplete {

    private Term[] terms;  // Array to hold the terms

    // Constructs an autocomplete data structure from an array of terms.
    public Autocomplete(Term[] terms) {
        // Check if the terms array is null and throw an exception if it is
        if (terms == null) {
            throw new NullPointerException("terms is null");
        }
        // Make a defensive copy of the terms array and sort it
        this.terms = Arrays.copyOf(terms, terms.length);
        Arrays.sort(this.terms);
    }

    // Returns all terms that start with prefix, in descending order of their weights.
    public Term[] allMatches(String prefix) {
        // Check if the prefix is null and throw an exception if it is
        if (prefix == null) {
            throw new NullPointerException("prefix is null");
        }
        // Create a comparator for comparing terms by prefix
        Comparator<Term> prefixOrder = Term.byPrefixOrder(prefix.length());
        // Get the indices of the first and last occurrence of the prefix
        int first = BinarySearchDeluxe.firstIndexOf(terms, new Term(prefix, 0), prefixOrder);
        int last = BinarySearchDeluxe.lastIndexOf(terms, new Term(prefix, 0), prefixOrder);
        // If no matches are found, return an empty array
        if (first == -1 || last == -1) {
            return new Term[0];
        }
        // Copy the matching terms into a new array
        Term[] matches = Arrays.copyOfRange(terms, first, last + 1);
        // Sort the matches in descending order of their weights
        Arrays.sort(matches, Term.byReverseWeightOrder());
        return matches;
    }

    // Returns the number of terms that start with prefix.
    public int numberOfMatches(String prefix) {
        // Check if the prefix is null and throw an exception if it is
        if (prefix==null) {
            throw new NullPointerException("prefix is null");
        }
        // Create a comparator for comparing terms by prefix
        Comparator<Term> prefixOrder = Term.byPrefixOrder(prefix.length());
        // Get the indices of the first and last occurrence of the prefix
        int firstIndex = BinarySearchDeluxe.firstIndexOf(terms, new Term(prefix), prefixOrder);
        int lastIndex = BinarySearchDeluxe.lastIndexOf(terms, new Term(prefix), prefixOrder);
        // If no matches are found, return 0
        if (firstIndex == -1 || lastIndex == -1) {
            return 0;
        }
        // Otherwise, calculate and return the number of matching terms
        return (lastIndex - firstIndex) + 1;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        int k = Integer.parseInt(args[1]);
        In in = new In(filename);
        int N = in.readInt();
        Term[] terms = new Term[N];
        for (int i = 0; i < N; i++) {
            long weight = in.readLong();
            in.readChar();
            String query = in.readLine();
            terms[i] = new Term(query.trim(), weight);
        }
        Autocomplete autocomplete = new Autocomplete(terms);
        StdOut.print("Enter a prefix (or ctrl-d to quit): ");
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            Term[] results = autocomplete.allMatches(prefix);
            String msg = " matches for \"" + prefix + "\", in descending order by weight:";
            if (results.length == 0) {
                msg = "No matches";
            } else if (results.length > k) {
                msg = "First " + k + msg;
            } else {
                msg = "All" + msg;
            }
            StdOut.printf("%s\n", msg);
            for (int i = 0; i < Math.min(k, results.length); i++) {
                StdOut.println("  " + results[i]);
            }
            StdOut.print("Enter a prefix (or ctrl-d to quit): ");
        }
    }
}
