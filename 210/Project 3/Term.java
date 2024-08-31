import java.util.Arrays;
import java.util.Comparator;

import stdlib.In;
import stdlib.StdOut;

public class Term implements Comparable<Term> {
    String query; // The query string
    long weight; // The weight of the query string

    // Constructs a term given the associated query string, having weight 0.
    public Term(String query) {
        // Corner case: If query is null
        if (query == null) {
            // Throw an error
            throw new NullPointerException("query is null");
        }

        this.query = query; // Set the query
        this.weight = 0; // Set the weight to 0
    }

    // Constructs a term given the associated query string and weight.
    public Term(String query, long weight) {
        // Corner case: If query is null
        if (query == null) {
            // Throw an error
            throw new NullPointerException("query is null");
        }

        // Corner case: Weight is less than 0
        if (weight < 0) {
            // Throw an error
            throw new IllegalArgumentException("Illegal weight");
        }

        this.query = query; // Set the query
        this.weight = weight; // Set the weight
    }

    // Returns a string representation of this term.
    public String toString() {
        return  this.weight + "\t" + this.query;
    }

    // Returns a comparison of this term and other by query.
    public int compareTo(Term other) {
        return this.query.compareTo(other.query);
    }

    // Returns a comparator for comparing two terms in reverse order of their weights.
    public static Comparator<Term> byReverseWeightOrder() {
        return new ReverseWeightOrder();
    }

    // Returns a comparator for comparing two terms by their prefixes of length r.
    public static Comparator<Term> byPrefixOrder(int r) {
        // Corner case: Prefix length is less than 0
        if (r < 0) {
            // Throw an error
            throw new IllegalArgumentException("Illegal r");
        }

        return new PrefixOrder(r);
    }

    // Reverse-weight comparator.
    private static class ReverseWeightOrder implements Comparator<Term> {
        // Returns a comparison of terms v and w by their weights in reverse order.
        public int compare(Term v, Term w) {
            if (v.weight > w.weight) {
                return -1;
            } else if (v.weight < w.weight) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    // Prefix-order comparator.
    private static class PrefixOrder implements Comparator<Term> {
        int r; // The prefix length

        // Constructs a new prefix order given the prefix length.
        PrefixOrder(int r) {
            this.r = r; // Set the prefix length
        }

        // Returns a comparison of terms v and w by their prefixes of length r.
        public int compare(Term v, Term w) {
            // Set the length of the prefix
            int lengthW = this.r <= w.query.length() ? this.r : w.query.length();
            int lengthV = this.r <= v.query.length() ? this.r : v.query.length();

            // Get the prefixes
            String prefixW = w.query.substring(0, lengthW);
            String prefixV = v.query.substring(0, lengthV);

            // Create two new terms from the prefixes
            Term prefixTermW = new Term(prefixW, w.weight);
            Term prefixTermV = new Term(prefixV, v.weight);


            return prefixTermV.compareTo(prefixTermW);
        }
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
        StdOut.printf("Top %d by lexicographic order:\n", k);
        Arrays.sort(terms);
        for (int i = 0; i < k; i++) {
            StdOut.println(terms[i]);
        }
        StdOut.printf("Top %d by reverse-weight order:\n", k);
        Arrays.sort(terms, Term.byReverseWeightOrder());
        for (int i = 0; i < k; i++) {
            StdOut.println(terms[i]);
        }
    }}

