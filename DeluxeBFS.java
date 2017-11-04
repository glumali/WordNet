/******************************************************************************
  *  Name:    Greg Umali
  * 
  *  Description:  Computes the shortest common ancestor using BFS with 
  *  linear probing hash tables (which uses a resizing array) from source 
  *  vertices or subsets.
  * 
  *****************************************************************************/

import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;

public class DeluxeBFS {
    
    // used to initialize current minimum distance
    private static final int INFINITY = Integer.MAX_VALUE;
    
    // stores distance to each vertex from a source vertex
    private LinearProbingHashST<Integer, Integer> vDistTo; 
    
    // stores distance to each vertex from the second source vertex
    private LinearProbingHashST<Integer, Integer> wDistTo;
    
    // stores ancestor
    private int ancestor;
    
    // stores minimum distance
    private int minDist;

    // constructor for two source vertices
    public DeluxeBFS(Digraph G, int v, int w) {
        
        // initializes hash table
        vDistTo = new LinearProbingHashST<Integer, Integer>();
        wDistTo = new LinearProbingHashST<Integer, Integer>();
        
        if (v == w) {
            ancestor = v;
            minDist = 0;
        } else {
        // call to helper method to find shortest common ancestor and distance
        bfs(G, v, w);
        }
    }

    // constructor for two interable objects
    public DeluxeBFS(Digraph G, Iterable<Integer> vSources, 
                     Iterable<Integer> wSources ) {
        
        // initializes hash table
        vDistTo = new LinearProbingHashST<Integer, Integer>();
        wDistTo = new LinearProbingHashST<Integer, Integer>();
        
        // call to helper method to find shortest common ancestor and distance
        bfs(G, vSources, wSources);
    }

    // helper method BFS from two source vertices
    private void bfs(Digraph G, int s, int w) {
        
        int curMinDist = INFINITY;
        int curAnc = -1;
        
        // stores reachable ancestors from source s
        Queue<Integer> q = new Queue<Integer>();
       
        // stores distance to source as 0
        vDistTo.put(s, 0);
        // puts the source on the queue
        q.enqueue(s);
        
        // finds the distance to each reachable ancestor and stores it
        // in hash table
        while (!q.isEmpty()) {
            int v = q.dequeue();
            
            for (int temp : G.adj(v)) {
                if (!vDistTo.contains(temp)) {
                    vDistTo.put(temp, vDistTo.get(v) + 1);
                    q.enqueue(temp);
                }
                
            }
        }
        
        // stores reachable ancestors from source w
        Queue<Integer> p = new Queue<Integer>();
        
        // source distance = 0
        wDistTo.put(w, 0);
        // add source to queue
        p.enqueue(w);

        // finds the distance to each reachable ancestor, checks if reachable
        // ancestor is the shortest common ancestor, and stores distance to
        // ancestor from source in hash table 
        while(!p.isEmpty()) {
            int pAnc = p.dequeue();

            // checks and stores closest ancestor
            if (vDistTo.contains(pAnc) && 
                    vDistTo.get(pAnc) + wDistTo.get(pAnc) < curMinDist) {
                    curMinDist =  vDistTo.get(pAnc) + wDistTo.get(pAnc);
                    curAnc = pAnc;
                }
            
            // adds all possible ancestors to queue if not added before
            for (int temp : G.adj(pAnc)) {
                if (!wDistTo.contains(temp)) {
                    wDistTo.put(temp, wDistTo.get(pAnc) + 1);
                    p.enqueue(temp);
                }       
                
            }
        }
        
        // sets instance variables
            minDist = curMinDist;
            ancestor = curAnc;
        
    }

    // BFS from multiple sources
    
    private void bfs(Digraph G, Iterable<Integer> vSources, 
                     Iterable<Integer> wSources ) {
        
        
        int curMinDist = INFINITY;
        int curAnc = -1;
        
        // stores possible ancestors from vSources
        Queue<Integer> q = new Queue<Integer>();
        // sets all distances to source vertices to 0
        for (int s : vSources) {
            vDistTo.put(s, 0);
            q.enqueue(s);
        }
        
        // calculate and store all distances for possible ancestors
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int temp : G.adj(v)) {
                if (!vDistTo.contains(temp)) {
                    vDistTo.put(temp, vDistTo.get(v) + 1);
                    q.enqueue(temp);
                }
                
            }
        }
        
        // stores possible ancestors from wSources
        Queue<Integer> p = new Queue<Integer>();
        
        for (int s : wSources) {
            wDistTo.put(s, 0);
            p.enqueue(s);
        }
        
        while(!p.isEmpty()) {
            int pAnc = p.dequeue();
            
            // calculates shortest ancestor and distnace
            if (vDistTo.contains(pAnc) && 
                vDistTo.get(pAnc) + wDistTo.get(pAnc) < curMinDist) {
                curMinDist =  vDistTo.get(pAnc) + wDistTo.get(pAnc);
                curAnc = pAnc;
            }
            
            for (int temp : G.adj(pAnc)) {
                if (!wDistTo.contains(temp)) {
                    wDistTo.put(temp, wDistTo.get(pAnc) + 1);
                    p.enqueue(temp);
                }
            }
        }
        
        // sets instance variables
        minDist = curMinDist;
        ancestor = curAnc;
        
    }
    
    // getter method to return shortest common ancestor
    public int getAnc() {
        return ancestor;
    }
    
    // getter method to return distance to shortest common ancestor
    public int getDist() {
        return minDist;
    }


    
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        
        // int s = Integer.parseInt(args[1]);
        DeluxeBFS bfs = new DeluxeBFS(G, 6, 7);
        System.out.println("ancestor = (3) " + bfs.getAnc());
        
        Queue<Integer> vSources = new Queue<Integer>();
        Queue<Integer> wSources = new Queue<Integer>();
        vSources.enqueue(6);
        vSources.enqueue(7);
        wSources.enqueue(1);
        wSources.enqueue(9);
        
        DeluxeBFS bfs2 = new DeluxeBFS(G, vSources, wSources);
        System.out.println("ancestor = (1) " + bfs2.getAnc());

        
    }


}