import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;


public class BoggleSolver {

    private AlphabetTrieST<Integer> wordScore;

    private static class AlphabetTrieST<Value> {
        
        private static final int R = 26;
        private HashMap<Character, Integer> map;
        private Node root;
        private int n;

        private static class Node {
            private Object val;
            private Node[] next = new Node[R];
        }

        public AlphabetTrieST() {
            this.map = new HashMap<Character, Integer>();
            char[] alp = new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
            for (int i = 0; i < alp.length; i++) {
                this.map.put(alp[i], i);
            }
        }

        public Value get(String key) {
            Node x = this.get(this.root, key, 0);
            if (x == null) { return null; }
            return (Value) x.val;
        }

        private Node get(Node x, String key, int d) {
            if (x == null) { return null; }
            if (d == key.length()) { return x; }
            char c = key.charAt(d);
            return this.get(x.next[this.map.get(c)], key, d + 1);
        }

        public boolean contains(String key) {
            return this.get(key) != null;
        }

        public void put(String key, Value val) {
            this.root = this.put(this.root, key, val, 0); 
        }

        private Node put(Node x, String key, Value val, int d) {
            if (x == null) { x = new Node(); }
            if (d == key.length()) {
                if (x.val == null) { this.n++; }
                x.val = val;
                return x;
            }
            char c = key.charAt(d);
            x.next[this.map.get(c)] = this.put(x.next[this.map.get(c)], key, val, d + 1);
            return x;
        }

        public boolean containsKeysWithPrefix(String prefix) {
            Node x = this.get(this.root, prefix, 0);
            if (x == null) { return false; }
            for (int i = 0; i < R; i++) {
                if (x.next[i] != null) { return true; }
            }
            return false;
        }

    }

    // Initializes the data structure using the given array of strings as the
    // dictionary.
    // (You can assume each word in the dictionary contains only the uppercase
    // letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.wordScore = new AlphabetTrieST<Integer>();
        for (String word : dictionary) {
            int score = this.calculateScoreOf(word);
            this.wordScore.put(word, score);
        }
    }

    private int calculateScoreOf(String word) {
        if (word.length() >= 8) {
            return 11;
        } else if (word.length() == 7) {
            return 5;
        } else if (word.length() == 6) {
            return 3;
        } else if (word.length() == 5) {
            return 2;
        } else if (word.length() >= 3) {
            return 1;
        } else {
            return 0;
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        HashSet<String> validWords = new HashSet<String>();
        for (int r = 0; r < board.rows(); r++) {
            for (int c = 0; c < board.cols(); c++) {
                boolean[] visited = new boolean[board.rows() * board.cols()];
                traverseTiles(new Tile(r, c), "", visited, board, validWords);
            }
        }
        return validWords;
    }

    private void traverseTiles(Tile currentTile, String currentString, boolean[] visited, BoggleBoard board, HashSet<String> validWords) {
        int currentVisitedIdx = currentTile.row * board.cols() + currentTile.col;
        visited[currentVisitedIdx] = true;
        char currentLetter = board.getLetter(currentTile.row, currentTile.col);
        if (currentLetter == 'Q') {
            currentString += "QU";
        } else {
            currentString += currentLetter;
        }
        // StdOut.println(Arrays.toString(visited));
        // StdOut.println(currentString);
        if (this.wordScore.contains(currentString) && currentString.length() >= 3) {
            validWords.add(currentString);
        }

        if (this.wordScore.containsKeysWithPrefix(currentString)) {
            for (Tile adjTile : this.adjacentTile(currentTile, board)) {
                int adjVisitedIdx = adjTile.row * board.cols() + adjTile.col;
                if (visited[adjVisitedIdx]) { continue; }
                traverseTiles(adjTile, currentString, visited, board, validWords);
            }  
        }
        visited[currentVisitedIdx] = false;
    }

    private Iterable<Tile> adjacentTile(Tile currentTile, BoggleBoard board) {
        ArrayList<Tile> adjTiles = new ArrayList<Tile>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) { continue; }
                if (currentTile.row + i < 0 || currentTile.row + i >= board.rows() || currentTile.col + j < 0 || currentTile.col + j >= board.cols()) {
                    continue;
                }
                adjTiles.add(new Tile(currentTile.row + i, currentTile.col + j));
            }
        }
        return adjTiles;
    }

    private class Tile {
        int col; int row;
        public Tile(int row, int col) {
            this.col = col;
            this.row = row;
        }
    }


    // Returns the score of the given word if it is in the dictionary, zero
    // otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!this.wordScore.contains(word)) {
            return 0;
        }
        return this.wordScore.get(word);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}