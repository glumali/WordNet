/******************************************************************************
  *  Name:    Greg Umali
  * 
  *  Description:  Represents a digraph of synsets, in which each synset points
  *  to its hypernym, a more general synset that the current synset belongs to.
  * 
  *****************************************************************************/

import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

public class WordNet {
    
    // represents digraph of synsets
    private Digraph graph;
    // Symbol Table with words as keys and a bag of integers corresponding to
    // synset ID's that the word is a part of as the value
    private RedBlackBST<String, Bag<Integer>> wordToSynsets;
    // vertex-indexed array (synset IDs) each a string containing the words
    // in the synset
    private String[] synsetArray;
    // object that allows one to calculate the shortest common ancestor between
    // two vertices in a digraph
    private ShortestCommonAncestor sca;

   // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new NullPointerException();
        
        In synsetFile = new In(synsets);
        In hypernymFile = new In(hypernyms);
        // contains contents of the synset file
        String[] synsetTemp = synsetFile.readAllLines();
        // number of sysets in the file
        int numOfV = synsetTemp.length;
        
        // intialize instance variables
        synsetArray = new String[numOfV];
        wordToSynsets = new RedBlackBST<String, Bag<Integer>>();
       
        // done for each line
        for (String s : synsetTemp) {
            String[] temp;
            String[] wordsInSynset;
            Bag<Integer> toAdd;
            
            // split into three tokens: [synset ID, words, gloss]
            temp = s.split(",");
            
            // bag that holds all the words in a given synset
            Bag<String> words = new Bag<String>();
            // different words in a synset are separated by spaces, split
            wordsInSynset = temp[1].split(" ");
            
            // stores synset ID as an integer
            int synsetID = Integer.parseInt(temp[0]);
            
            // adds the String of words in the synset to the corresponding
            // place in the array (by synset ID)
            synsetArray[synsetID] = temp[1];
            
            // for each word after the second split (by spaces)
            for (String word : wordsInSynset) {
                
                // create symbol table of words that hold a bag of 
                // synsetID's that they are found in
                
                // if not already in ST
                if (!wordToSynsets.contains(word)) {
                    // create a new bag
                    toAdd = new Bag<Integer>();
                    // place first integer corresponding to the synset it
                    // was first encountered in
                    toAdd.add(synsetID);
                    
                    // add to ST
                    wordToSynsets.put(word, toAdd);
                    
                } else {
                    // add current synset ID to the Bag associated with the word
                    toAdd = wordToSynsets.get(word);
                    toAdd.add(synsetID);
                }
                
                // add this word to the bag of this synset
                words.add(word);
            } 
        }
        
        // make a new digraph
        graph = new Digraph(numOfV);
        
        // parses in hypernyms file
        while (hypernymFile.hasNextLine()) {
            String temp = hypernymFile.readLine();
            // split each line by comma [synset ID, hypernym 1, ..., hypernym n]
            String[] tokens = temp.split(",");
            // first entry is always the synset ID
            int v = Integer.parseInt(tokens[0]);
            // for all following tokens, add an edge from current synset ID to
            // the corresponding synset vertex
            for (int i = 1; i < tokens.length; i++) {
                graph.addEdge(v, Integer.parseInt(tokens[i]));       
            }  
        }  
        
        sca = new ShortestCommonAncestor(graph);
    }

   // all WordNet nouns
    public Iterable<String> nouns() {
        return wordToSynsets.keys();
    }

   // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new NullPointerException();
        return wordToSynsets.contains(word);
    }
    
   // a synset (second field of synsets.txt) that is a shortest common ancestor
   // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {
        if (!isNoun(noun1)) throw new IllegalArgumentException();
        if (!isNoun(noun2)) throw new IllegalArgumentException();
        
        Queue<Integer> noun1sets = new Queue<Integer>();
        Queue<Integer> noun2sets = new Queue<Integer>();
        // get an Iterable<Integer> of all synsets that noun1 is in
        // for each int in the word's associated bag
        for (int id : wordToSynsets.get(noun1)) {
            noun1sets.enqueue(id);
        }
        // get an Iterable<Integer> of all synsets that noun2 is in
        for (int id : wordToSynsets.get(noun2)) {
            noun2sets.enqueue(id);
        }
        
        // return the ancestor's synset string
        return synsetArray[sca.ancestor(noun1sets, noun2sets)];
    }

    // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {
        if (!isNoun(noun1)) throw new IllegalArgumentException();
        if (!isNoun(noun2)) throw new IllegalArgumentException();
        
        Queue<Integer> noun1sets = new Queue<Integer>();
        Queue<Integer> noun2sets = new Queue<Integer>();
        // get an Iterable<Integer> of all synsets that noun1 is in
        // for each int in the word's associated bag
        for (int id : wordToSynsets.get(noun1)) {
            noun1sets.enqueue(id);
        }
        // get an Iterable<Integer> of all synsets that noun2 is in
        for (int id : wordToSynsets.get(noun2)) {
            noun2sets.enqueue(id);
        }
        
        // compute and return the length between the set of synsets
        return sca.length(noun1sets, noun2sets);
    }
    
   // do unit testing of this class
    
    public static void main(String[] args) {
        WordNet test = new WordNet(args[0], args[1]);
        
        // prints out the graph created (used for testing)
        // System.out.println(test.graph);
        
        // prints out the height of the BST created (make sure not empty)
        System.out.println("Height of our synset BST: " 
                               + test.wordToSynsets.height());
        System.out.println();
        
        System.out.println("Test length function:");
        String test1 = "Black_Plague";
        String test2 = "black_marlin";
        
        System.out.println("Distance between " + test1 + " and " + test2 + ": " 
                               + test.distance(test1, test2));
        System.out.println();
        
        System.out.println("Test ancestor function:");
        String test3 = "individual";
        String test4 = "edible_fruit";
        
        System.out.println("Common ancestor between " + test3 + " and " 
                               + test4 + ": " + test.sca(test3, test4));
        System.out.println();
    }
}