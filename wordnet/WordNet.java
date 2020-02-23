import java.util.ArrayList;
import edu.princeton.cs.algs4.Digraph;
import java.util.HashMap;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.DirectedCycle;


public class WordNet {

   private HashMap<String, ArrayList<Integer>> nounToSynsetIds;
   private HashMap<Integer, String> synsetIdToSynSet;
   private Digraph G;
   private SAP sap;

   // constructor takes the name of the two input files
   public WordNet(String synsets, String hypernyms) {

      validateArgument(synsets);
      validateArgument(hypernyms);

      In synsetsContent = new In(synsets);
      In hypernymsContent = new In(hypernyms);
      
      this.synsetIdToSynSet = new HashMap<Integer, String>();
      this.nounToSynsetIds = new HashMap<String, ArrayList<Integer>>();
      String line = synsetsContent.readLine();
      int numberOfSynset = 0;
      while (line != null) {
         String[] cols = line.split(",");
         int synsetId = Integer.parseInt(cols[0]);
         this.synsetIdToSynSet.put(synsetId, cols[1]);
         String[] nouns = cols[1].split(" ");
         for (String noun: nouns) {
            if (this.nounToSynsetIds.get(noun) == null) {
               this.nounToSynsetIds.put(noun, new ArrayList<Integer>());
            }
            this.nounToSynsetIds.get(noun).add(synsetId);
         }
         numberOfSynset++;
         line = synsetsContent.readLine();
      }

      this.G = new Digraph(numberOfSynset);

      line = hypernymsContent.readLine();
      while (line != null) {
         String[] cols = line.split(",");
         int synsetId = Integer.parseInt(cols[0]);
         for (int i = 1; i < cols.length; i++) {
            this.G.addEdge(synsetId, Integer.parseInt(cols[i]));
         }
         line = hypernymsContent.readLine();
      }

      DirectedCycle dc = new DirectedCycle(this.G);
      if (dc.hasCycle()) {
         throw new IllegalArgumentException();
      }

      this.sap = new SAP(this.G);

   }

   private void validateArgument(Object arg) {
      if (arg == null) {
         throw new IllegalArgumentException();
      }
   }

   // returns all WordNet nouns
   public Iterable<String> nouns() {
      return this.nounToSynsetIds.keySet();
   }

   // is the word a WordNet noun?
   public boolean isNoun(String word) {
      validateArgument(word);
      return this.nounToSynsetIds.get(word) != null;
   }

   // distance between nounA and nounB (defined below)
   public int distance(String nounA, String nounB) {
      validateArgument(nounA);
      validateArgument(nounB);
      if (!this.isNoun(nounA) || !this.isNoun(nounA)) {
         throw new IllegalArgumentException();
      }
      Iterable<Integer> synsetIdOfA = this.nounToSynsetIds.get(nounA);
      Iterable<Integer> synsetIdOfB = this.nounToSynsetIds.get(nounB);
      return this.sap.length(synsetIdOfA, synsetIdOfB);
   }

   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
   // in a shortest ancestral path (defined below)
   public String sap(String nounA, String nounB) {
      validateArgument(nounA);
      validateArgument(nounB);
      if (!this.isNoun(nounA) || !this.isNoun(nounA)) {
         throw new IllegalArgumentException();
      }
      Iterable<Integer> synsetIdOfA = this.nounToSynsetIds.get(nounA);
      Iterable<Integer> synsetIdOfB = this.nounToSynsetIds.get(nounB);
      int commonAncestor = this.sap.ancestor(synsetIdOfA, synsetIdOfB);
      return this.synsetIdToSynSet.get(commonAncestor);
   }

   // do unit testing of this class
   public static void main(String[] args) {
      WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");
      // StdOut.println(wn.nouns().toString());
      StdOut.printf("%s %s%n", "contains `word`?", wn.isNoun("word"));
      StdOut.printf("%s %s%n", "contains `osdfosdf`?", wn.isNoun("osdfosdf"));
      StdOut.printf("%s %s%n", "distance of `jump` and `action`", wn.distance("jump", "action"));
      StdOut.printf("%s %s%n", "distance of `jump` and `jump`", wn.distance("jump", "jump"));
      StdOut.printf("%s %s%n", "sap of `jump` and `action`", wn.sap("jump", "action"));
   }
}