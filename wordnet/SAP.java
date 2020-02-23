import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;


public class SAP {

  private Digraph G;

  // constructor takes a digraph (not necessarily a DAG)
  public SAP(Digraph G) {
    validateArgument(G);
    this.G = new Digraph(G);
  }

  private void validateArgument(Object arg) {
    if (arg == null) {
      throw new IllegalArgumentException();
    }
    if (arg instanceof Integer) {
      int argInt = (int) arg;
      if (argInt > this.G.V() - 1) {
        throw new IllegalArgumentException();
      }
    }
  }

  // length of shortest ancestral path between v and w; -1 if no such path
  public int length(int v, int w) {
    validateArgument(v);
    validateArgument(w);

    BreadthFirstDirectedPaths bfsOfV = new BreadthFirstDirectedPaths(this.G, v);
    BreadthFirstDirectedPaths bfsOfW = new BreadthFirstDirectedPaths(this.G, w);
    
    int shortestLength = Integer.MAX_VALUE;
    for (int k = 0; k < this.G.V(); k++) {
      if (bfsOfV.hasPathTo(k) && bfsOfW.hasPathTo(k)) {
        int lengthOfAncestralPath = bfsOfV.distTo(k) + bfsOfW.distTo(k);
        if (lengthOfAncestralPath < shortestLength) {
          shortestLength = lengthOfAncestralPath;
        }
      }
    }

    if (shortestLength == Integer.MAX_VALUE) {
      return -1;
    } else {
      return shortestLength;
    }

  }


  // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
  public int ancestor(int v, int w) {
    validateArgument(v);
    validateArgument(w);

    BreadthFirstDirectedPaths bfsOfV = new BreadthFirstDirectedPaths(this.G, v);
    BreadthFirstDirectedPaths bfsOfW = new BreadthFirstDirectedPaths(this.G, w);
    
    int shortestLength = Integer.MAX_VALUE;
    int shortestAncestor = -1;
    for (int k = 0; k < this.G.V(); k++) {
      if (bfsOfV.hasPathTo(k) && bfsOfW.hasPathTo(k)) {
        int lengthOfAncestralPath = bfsOfV.distTo(k) + bfsOfW.distTo(k);
        if (lengthOfAncestralPath < shortestLength) {
          shortestLength = lengthOfAncestralPath;
          shortestAncestor = k;
        }
      }
    }

    return shortestAncestor;
  }

  // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
  public int length(Iterable<Integer> v, Iterable<Integer> w) {
    validateArgument(v);
    validateArgument(w);
    for (int vv: v) {
      for (int ww: w) {
        validateArgument(vv);
        validateArgument(ww);
      }
    }
    
    BreadthFirstDirectedPaths bfsOfV = new BreadthFirstDirectedPaths(this.G, v);
    BreadthFirstDirectedPaths bfsOfW = new BreadthFirstDirectedPaths(this.G, w);
    
    int shortestLength = Integer.MAX_VALUE;
    for (int k = 0; k < this.G.V(); k++) {
      if (bfsOfV.hasPathTo(k) && bfsOfW.hasPathTo(k)) {
        int lengthOfAncestralPath = bfsOfV.distTo(k) + bfsOfW.distTo(k);
        if (lengthOfAncestralPath < shortestLength) {
          shortestLength = lengthOfAncestralPath;
        }
      }
    }

    if (shortestLength == Integer.MAX_VALUE) {
      return -1;
    } else {
      return shortestLength;
    }
    
  }

  // a common ancestor that participates in shortest ancestral path; -1 if no such path
  public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
    validateArgument(v);
    validateArgument(w);
    for (int vv: v) {
      for (int ww: w) {
        validateArgument(vv);
        validateArgument(ww);
      }
    }

    BreadthFirstDirectedPaths bfsOfV = new BreadthFirstDirectedPaths(this.G, v);
    BreadthFirstDirectedPaths bfsOfW = new BreadthFirstDirectedPaths(this.G, w);
    
    int shortestLength = Integer.MAX_VALUE;
    int shortestAncestor = -1;
    for (int k = 0; k < this.G.V(); k++) {
      if (bfsOfV.hasPathTo(k) && bfsOfW.hasPathTo(k)) {
        int lengthOfAncestralPath = bfsOfV.distTo(k) + bfsOfW.distTo(k);
        if (lengthOfAncestralPath < shortestLength) {
          shortestLength = lengthOfAncestralPath;
          shortestAncestor = k;
        }
      }
    }

    return shortestAncestor;
  }

  // do unit testing of this class
  public static void main(String[] args) {

    Digraph g = new Digraph(12);
    g.addEdge(1, 0);
    g.addEdge(2, 0);
    g.addEdge(3, 1);
    g.addEdge(4, 1);
    g.addEdge(5, 1);
    g.addEdge(6, 3);
    g.addEdge(7, 3);
    g.addEdge(8, 5);
    g.addEdge(9, 5);
    g.addEdge(10, 9);
    g.addEdge(11, 9);
    SAP sap = new SAP(g);
    StdOut.println(sap.ancestor(3, 10));
    StdOut.println(sap.length(3, 10));

    Digraph g1 = new Digraph(25);
    g1.addEdge(1, 0);
    g1.addEdge(2, 0);
    g1.addEdge(3, 1);
    g1.addEdge(4, 1);
    g1.addEdge(5, 2);
    g1.addEdge(6, 2);
    g1.addEdge(3, 1);
    g1.addEdge(4, 1);
    g1.addEdge(5, 2);
    g1.addEdge(6, 2);
    g1.addEdge(7, 3);
    g1.addEdge(8, 3);
    g1.addEdge(9, 3);
    g1.addEdge(10, 5);
    g1.addEdge(11, 5);
    g1.addEdge(12, 5);
    g1.addEdge(13, 7);
    g1.addEdge(14, 7);
    g1.addEdge(15, 9);
    g1.addEdge(16, 9);
    g1.addEdge(17, 10);
    g1.addEdge(18, 10);
    g1.addEdge(19, 12);
    g1.addEdge(20, 12);
    g1.addEdge(21, 16);
    g1.addEdge(22, 16);
    g1.addEdge(23, 20);
    g1.addEdge(24, 20);
    ArrayList<Integer> a = new ArrayList<Integer>();
    a.add(13);
    a.add(23);
    a.add(24);
    ArrayList<Integer> b = new ArrayList<Integer>();
    b.add(6);
    b.add(16);
    b.add(17);
    SAP sap1 = new SAP(g1);
    StdOut.println(sap1.ancestor(a, b));
    StdOut.println(sap1.length(a, b));
    StdOut.println(sap1.ancestor(1, 6));
    StdOut.println(sap1.length(1, 6));
  }

}