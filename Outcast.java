/******************************************************************************
  *  Name:    Greg Umali
  * 
  *  Description:  Computes the outcast of given list of nouns (the one with
  *  a larger total distance to every other noun in the set of given nouns).
  * 
  *****************************************************************************/
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast { 
    
    // data type that represents a network of connected synsets
    private final WordNet wordnet;
        
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }
    
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        // tracks maximum distance from one noun and all others
        int maxTotalDist = 0;
        // tracks current outcast noun in the wordnet
        String currentOutcast = "";
        
        for (String queryNoun : nouns) {
            int maxDist = 0;
            
            for (String compNoun : nouns) {
                // from one noun, compute distance to all others
                maxDist += wordnet.distance(queryNoun, compNoun);
            }
            
            if (maxDist > maxTotalDist) {
                // updates largest distance and corresponding noun
                currentOutcast = queryNoun;
                maxTotalDist = maxDist;
            }
        }
        
        return currentOutcast;
    }
    
    // test client
    public static void main(String[] args) {
    WordNet wordnet = new WordNet(args[0], args[1]);
    Outcast outcast = new Outcast(wordnet);
    for (int t = 2; t < args.length; t++) {
        In in = new In(args[t]);
        String[] nouns = in.readAllStrings();
        StdOut.println(args[t] + ": " + outcast.outcast(nouns));
    }
}
}