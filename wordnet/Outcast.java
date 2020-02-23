import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;


public class Outcast {

  private WordNet wordnet;

  public Outcast(WordNet wordnet) {
    this.wordnet = wordnet;
  }

  public String outcast(String[] nouns) {

    String outcastNoun = "";
    int maxDist = Integer.MIN_VALUE;
    for (String nouni: nouns) {
      int dist = 0;
      for (String nounj: nouns) {
        dist = dist + this.wordnet.distance(nouni, nounj);
      }
      if (dist > maxDist) {
        maxDist = dist;
        outcastNoun = nouni;
      }
    }
    return outcastNoun;

  }

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