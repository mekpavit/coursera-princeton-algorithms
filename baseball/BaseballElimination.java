import edu.princeton.cs.algs4.In;
import java.util.HashMap;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FordFulkerson;
import java.util.ArrayList;

public class BaseballElimination {

  private int[] w;
  private int[] l;
  private int[] r;
  private int[][] g;
  private int numberOfTeams;
  private String[] teams;
  private HashMap<String, Integer> nameMap;
  private boolean[] eliminated;
  private ArrayList<ArrayList<Integer>> certificateOf;

  public BaseballElimination(String filename) {

    In baseballData = new In(filename);
    this.numberOfTeams = baseballData.readInt();
    this.w = new int[this.numberOfTeams];
    this.l = new int[this.numberOfTeams];
    this.r = new int[this.numberOfTeams];
    this.g = new int[this.numberOfTeams][this.numberOfTeams];
    this.teams = new String[this.numberOfTeams];
    this.nameMap = new HashMap<String, Integer>();
    this.eliminated = new boolean[this.numberOfTeams];
    this.certificateOf = new ArrayList<ArrayList<Integer>>();
    for (int i = 0; i < this.numberOfTeams; i++) {
      this.certificateOf.add(i, null);
    }
    for (int i = 0; i < this.numberOfTeams; i++) {
      String team = baseballData.readString();
      this.teams[i] = team;
      this.nameMap.put(team, i);
      this.w[i] = baseballData.readInt();
      this.l[i] = baseballData.readInt();
      this.r[i] = baseballData.readInt();
      for (int j = 0; j < this.numberOfTeams; j++) {
        this.g[i][j] = baseballData.readInt();
      }
    }

    for (int x = 0; x < this.numberOfTeams; x++) {
      for (int i = 0; i < this.numberOfTeams; i++) {
        if (i == x) {
          continue;
        }
        if (w[x] + r[x] < w[i]) {
          this.eliminated[x] = true;
          if (this.certificateOf.get(x) == null) {
            this.certificateOf.set(x, new ArrayList<Integer>());
          }
          this.certificateOf.get(x).add(i);
        }
      }

      if (this.eliminated[x]) {
        continue;
      }

      HashMap<String, Integer> teamToFNMap = new HashMap<String, Integer>();
      FlowNetwork fn = new FlowNetwork(
          1 + (this.numberOfTeams - 1) * (this.numberOfTeams - 2) / 2 + (this.numberOfTeams - 1) + 1);
      int fnIdx = 0;

      teamToFNMap.put("s", fnIdx);
      fnIdx++;

      teamToFNMap.put("t", fnIdx);
      fnIdx++;

      for (int i = 0; i < this.numberOfTeams; i++) {
        if (i == x) {
          continue;
        }
        teamToFNMap.put(String.valueOf(i), fnIdx);
        fn.addEdge(new FlowEdge(fnIdx, teamToFNMap.get("t"), (double) w[x] + r[x] - w[i]));
        fnIdx++;
      }

      double sumOfLeftGames = 0.0;
      for (int i = 0; i < this.numberOfTeams; i++) {
        if (i == x) {
          continue;
        }
        for (int j = i + 1; j < this.numberOfTeams; j++) {
          if (j == x) {
            continue;
          }
          String name = String.valueOf(i) + "-" + String.valueOf(j);
          teamToFNMap.put(name, fnIdx);
          fn.addEdge(new FlowEdge(teamToFNMap.get("s"), fnIdx, (double) g[i][j]));
          fn.addEdge(new FlowEdge(fnIdx, teamToFNMap.get(String.valueOf(i)), Double.POSITIVE_INFINITY));
          fn.addEdge(new FlowEdge(fnIdx, teamToFNMap.get(String.valueOf(j)), Double.POSITIVE_INFINITY));
          fnIdx++;

          sumOfLeftGames += (double) g[i][j];
        }
      }

      FordFulkerson ff = new FordFulkerson(fn, teamToFNMap.get("s"), teamToFNMap.get("t"));
      if (ff.value() != sumOfLeftGames) {
        this.eliminated[x] = true;
        this.certificateOf.set(x, new ArrayList<Integer>());
        for (int i = 0; i < this.numberOfTeams; i++) {
          if (i == x) {
            continue;
          }
          if (ff.inCut(teamToFNMap.get(String.valueOf(i)))) {
            this.certificateOf.get(x).add(i);
          }
        }
      }
      
    }

  }

  public int numberOfTeams() {
    return this.numberOfTeams;
  }

  public Iterable<String> teams() {
    return this.nameMap.keySet();
  }

  private void validateTeam(String team) {
    if (this.nameMap.get(team) == null) {
      throw new IllegalArgumentException();
    }
  }

  public int wins(String team) {
    validateTeam(team);
    return this.w[this.nameMap.get(team)];
  }

  public int losses(String team) {
    validateTeam(team);
    return this.l[this.nameMap.get(team)];
  }

  public int remaining(String team) {
    validateTeam(team);
    return this.r[this.nameMap.get(team)];
  }

  public int against(String team1, String team2) {
    validateTeam(team1);
    validateTeam(team2);
    return this.g[this.nameMap.get(team1)][this.nameMap.get(team2)];
  }

  public boolean isEliminated(String team) {
    validateTeam(team);
    return this.eliminated[this.nameMap.get(team)];
  }

  public Iterable<String> certificateOfElimination(String team) {
    validateTeam(team);
    if (this.certificateOf.get(this.nameMap.get(team)) == null) {
      return null;
    }
    ArrayList<String> coe = new ArrayList<String>();
    for (int teamIdx : this.certificateOf.get(this.nameMap.get(team))) {
      coe.add(this.teams[teamIdx]);
    }
    return coe;
  }

  public static void main(String[] args) {
    BaseballElimination division = new BaseballElimination(args[0]);
    for (String team : division.teams()) {
      if (division.isEliminated(team)) {
        StdOut.print(team + " is eliminated by the subset R = { ");
        for (String t : division.certificateOfElimination(team)) {
          StdOut.print(t + " ");
        }
        StdOut.println("}");
      } else {
        StdOut.println(team + " is not eliminated");
      }
    }
  }

}