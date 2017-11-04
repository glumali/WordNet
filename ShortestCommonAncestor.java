/******************************************************************************
  *  Name:    Greg Umali
  * 
  *  Description:  Computes the shortest common ancestor between two vertices
  *  in a Digraph objects.
  * 
  *****************************************************************************/

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedCycle;

public class ShortestCommonAncestor {
    
    // represents digraph of synsets
    private final Digraph graph;

    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
        // check if digraph is null
        if (G == null) throw new NullPointerException();
        
        // check if there is a cycle in the digraph
        DirectedCycle rootedDAG = new DirectedCycle(G);
        if (rootedDAG.hasCycle()) {
            throw new IllegalArgumentException();
        }
        
        // check if digraph is rooted
        int numOfV = G.V();
        
        if (numOfV == 0) throw new IllegalArgumentException();
        
        int numOfRoots = 0;
        
        for (int i = 0; i < numOfV; i++) {
            // counts number of roots
            if (G.outdegree(i) == 0) {
                numOfRoots++;
            }
            // if more than one root, throw exception
            if (numOfRoots > 1) {
                throw new IllegalArgumentException();
            }            
            
        }
   
      
        graph = new Digraph(G);
    }
    
    // private helper method to check if synset ID is value
    private void checkInBounds(int v) {
        if (v < 0 || v >= graph.V()) {
            throw new IndexOutOfBoundsException();
        }
        
    }

    
    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        
        checkInBounds(v);
        checkInBounds(w);
        
        // uses helper class to calculate shortest ancestor and length
        // to ancestor
        DeluxeBFS length = new DeluxeBFS(graph, v, w);
        
        return length.getDist();
        
    }
    

   // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        checkInBounds(v);
        checkInBounds(w);
        
        // uses helper class to calculate shortest ancestor and length
        // to ancestor
        DeluxeBFS anc = new DeluxeBFS(graph, v, w);
        return anc.getAnc();
        
    }

   // length of shortest ancestral path of vertex subsets A and B
    public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (subsetA == null || subsetB == null) throw new NullPointerException();
 
        // check if subsetA contains a vertex that is out of bounds
        int counterA = 0;
        for (int i : subsetA) {
            checkInBounds(i);
            counterA++;
        }
        // check if subsetA is empty
        if (counterA == 0) throw new IllegalArgumentException();
        
        // check if subsetB contains a vertex that is out of bounds
        int counterB = 0;
        for (int i : subsetB) {
            checkInBounds(i);
            counterB++;
        }
        // check if subsetB is empty
        if (counterB == 0) throw new IllegalArgumentException();
        
        DeluxeBFS length = new DeluxeBFS(graph, subsetA, subsetB);
        return length.getDist();
        
    }

   // a shortest common ancestor of vertex subsets A and B
    public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (subsetA == null || subsetB == null) throw new NullPointerException();
        
        // check if any vertex in either iterable object is out of bounds,
        // or if either iterable is empty
        int counterA = 0;
        for (int i : subsetA) {
            checkInBounds(i);
            counterA++;
        } 
        if (counterA == 0) throw new IllegalArgumentException();

        int counterB = 0;
        for (int i : subsetB) {
            checkInBounds(i);
            counterB++;
        }
        if (counterB == 0) throw new IllegalArgumentException();
        
        DeluxeBFS anc = new DeluxeBFS(graph, subsetA, subsetB);
        return anc.getAnc();
    }
    
    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}