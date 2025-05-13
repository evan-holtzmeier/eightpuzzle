package eightpuzzle;

import java.util.*;
import java.io.*;

public class EightPuzzle {

    private int[][] state;

    public EightPuzzle() {
        this.state = new int[][] {
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8}
        };
    }

    public static void main(String args[]) {
        EightPuzzle puzzle = new EightPuzzle(); // Create an instance of EightPuzzle

        if (args.length > 0) {
            puzzle.cmdfile(args[0]); // Call the instance method on the object
        } else {
            System.out.println("Error: No command file provided.");
        }
    }

    public void cmd(String command) {
        String[] tokens = command.split(" ");
        switch (tokens[0]) {
            case "setState":
                if (tokens.length != 10) {
                    System.out.println("Error: invalid puzzle state");
                    return;
                }
                int[] newState = new int[9];
                try {
                    for (int i = 1; i <= 9; i++) {
                        newState[i - 1] = Integer.parseInt(tokens[i]);
                    }
                    setState(newState);
                } catch (NumberFormatException e) {
                    System.out.println("Error: invalid puzzle state");
                }
                break;

            case "printState":
                printState();
                break;

            case "move":
                if (tokens.length != 2) {
                    System.out.println("Error: invalid command: " + command);
                } else {
                    move(tokens[1]);
                }
                break;

            case "scrambleState":
                if (tokens.length != 2) {
                    System.out.println("Error: invalid command: " + command);
                } else {
                    try {
                        int moves = Integer.parseInt(tokens[1]);
                        scrambleState(moves);
                    } catch (NumberFormatException e) {
                        System.out.println("Error: invalid scramble count");
                    }
                }
                break;

            case "solveDFS":
                if (tokens.length != 2) {
                    System.out.println("Error: invalid command: " + command);
                    return;
                }
                try {
                    int maxnodes = Integer.parseInt(tokens[1]);
                    solveDFS(maxnodes);
                } catch (NumberFormatException e) {
                    System.out.println("Error: invalid maxnodes value");
                }
                break;

            case "solveBFS":
                if (tokens.length != 2) {
                    System.out.println("Error: invalid command: " + command);
                    return;
                }
                try {
                    int maxnodes = Integer.parseInt(tokens[1]);
                    solveBFS(maxnodes);
                } catch (NumberFormatException e) {
                    System.out.println("Error: invalid maxnodes value");
                }
                break;

            case "solveAStar":
                if (tokens.length < 3) {
                    System.out.println("Error: invalid command: " + command);
                    return;
                }
                String heuristic = tokens[1];
                try {
                    int maxnodes = Integer.parseInt(tokens[2]);
                    solveAStar(heuristic, maxnodes);
                } catch (NumberFormatException e) {
                    System.out.println("Error: invalid maxnodes value");
                }
                break;

            case "#":
            case "//":
                // Ignore comments
                break;

            default:
                System.out.println("Error: invalid command: " + command);
        }
    }


    public void setState(int[] newState) {
        int[] digitCounts = new int[9];
        boolean isValid = true;

        // Invalid if not nine digits total
        if (newState.length != 9) {
            isValid = false;
            System.out.println("Error: invalid puzzle state");
        }

        // Invalid if a digit is not between 0 and 8, or if a digit occurs more than once
        for (int i = 0; i < 9; i++) {
            int value = newState[i];
            if (value < 0 || value > 8) {
                isValid = false;
                System.out.println("Error: invalid puzzle state");
                break;
            }
            digitCounts[value]++;
        }
        for (int count : digitCounts) {
            if (count != 1) {
                isValid = false;
                System.out.println("Error: invalid puzzle state");
                break;
            }
        }

        // Set state
        if (isValid) {
            for (int i = 0; i < 9; i++) {
                this.state[i / 3][i % 3] = newState[i];
            }
        }
    }

    public void printState() {
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 3; k++) {
                if (state[j][k] == 0) {
                    System.out.print("  "); // Use space for blank tile
                } else {
                    System.out.print(state[j][k] + " ");
                }
            }
            System.out.println();
        }
    }

    private int[] findBlankTile(int[][] currState) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (currState[i][j] == 0) {
                    return new int[] { i, j }; // Return row and column
                }
            }
        }
        return null;
    }

    public void move(String direction) {
        int[] blankPos = findBlankTile(this.state);
        int row = blankPos[0];
        int col = blankPos[1];

        switch (direction) {
            case "up":
                if (row == 0) {
                    System.out.println("Error: Invalid move");
                } else {
                    state[row][col] = state[row - 1][col];
                    state[row - 1][col] = 0;
                }
                break;

            case "down":
                if (row == 2) {
                    System.out.println("Error: Invalid move");
                } else {
                    state[row][col] = state[row + 1][col];
                    state[row + 1][col] = 0;
                }
                break;

            case "left":
                if (col == 0) {
                    System.out.println("Error: Invalid move");
                } else {
                    state[row][col] = state[row][col - 1];
                    state[row][col - 1] = 0;
                }
                break;

            case "right":
                if (col == 2) {
                    System.out.println("Error: Invalid move");
                } else {
                    state[row][col] = state[row][col + 1];
                    state[row][col + 1] = 0;
                }
                break;

            default:
                System.out.println("Error: Invalid move");
        }
    }

    public void scrambleState(int n) {
        this.setState(new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 }); // Set to goal state
        Random rand = new Random(123); // Fixed seed for reproducibility
        for (int i = 0; i < n; i++) {
            int[] blankPos = findBlankTile(this.state);
            int row = blankPos[0];
            int col = blankPos[1];
            List<String> validMoves = new ArrayList<>();
            if (row > 0)
                validMoves.add("up");
            if (row < 2)
                validMoves.add("down");
            if (col > 0)
                validMoves.add("left");
            if (col < 2)
                validMoves.add("right");
            move(validMoves.get(rand.nextInt(validMoves.size())));
        }
    }

    public void cmdfile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;
                System.out.println(line);
                if (!line.startsWith("#") && !line.startsWith("//")) {
                    cmd(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error: unable to read file " + filename);
            e.printStackTrace();
        }
    }

    public void solveDFS(int maxnodes) {
        int nodesCreated = 0;
        Stack<SearchNode> stack = new Stack<>();
        SearchNode rootNode = new SearchNode(copyState(state), new ArrayList<>());
        stack.push(rootNode);
        nodesCreated++;

        Set<String> visited = new HashSet<>(); // Track visited states
        visited.add(serializeState(rootNode.state));

        while (!stack.isEmpty()) {
            if (nodesCreated > maxnodes) {
                System.out.println("Error: maxnodes limit (" + maxnodes + ") reached");
                return;
            }
            SearchNode currentNode = stack.pop();
            if (isGoalState(currentNode.state)) {
                System.out.println("Nodes created during search: " + nodesCreated);
                System.out.println("Solution length: " + currentNode.moves.size());
                for (String move : currentNode.moves) {
                    System.out.println(move);
                }
                return;
            }
            for (String move : new String[] { "up", "down", "left", "right" }) {
                int[][] newState = copyState(currentNode.state);
                EightPuzzle tempPuzzle = new EightPuzzle();
                tempPuzzle.state = newState;
                tempPuzzle.move(move);
                String serializedNewState = serializeState(tempPuzzle.state);
                if (!visited.contains(serializedNewState)) { // Check if state has been visited
                    visited.add(serializedNewState); // Mark new state as visited
                    List<String> newMoves = new ArrayList<>(currentNode.moves);
                    newMoves.add(move);
                    SearchNode newNode = new SearchNode(copyState(tempPuzzle.state), newMoves);
                    stack.push(newNode);
                    nodesCreated++;
                }
            }
        }
        System.out.println("Error: no solution found");
    }

    private String serializeState(int[][] state) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : state) {
            for (int val : row) {
                sb.append(val);
            }
        }
        return sb.toString();
    }

    public void solveBFS(int maxnodes) {
        int nodesCreated = 0;
        Queue<SearchNode> queue = new LinkedList<>();
        SearchNode rootNode = new SearchNode(copyState(state), new ArrayList<>());
        queue.add(rootNode);
        nodesCreated++;
        while (!queue.isEmpty()) {
            if (nodesCreated > maxnodes) {
                System.out.println("Error: maxnodes limit (" + maxnodes + ") reached");
                return;
            }
            SearchNode currentNode = queue.poll();
            if (isGoalState(currentNode.state)) {
                System.out.println("Nodes created during search: " + nodesCreated);
                System.out.println("Solution length: " + currentNode.moves.size());
                for (String move : currentNode.moves) {
                    System.out.println(move);
                }
                return;
            }
            List<String> possibleMoves = Arrays.asList("left", "right", "up", "down");
            for (String move : possibleMoves) {
                int[][] newState = applyMove(currentNode.state, move);
                if (newState != null) {
                    List<String> newMoves = new ArrayList<>(currentNode.moves);
                    newMoves.add("move " + move);
                    queue.add(new SearchNode(newState, newMoves));
                    nodesCreated++;
                }
            }
        }
        System.out.println("No solution found");
    }

    public void solveAStar(String heuristic, int maxnodes) {
        int nodesCreated = 0;
        PriorityQueue<SearchNode> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.cost));
        SearchNode rootNode = new SearchNode(copyState(state), new ArrayList<>()/*, 0*/);
        pq.add(rootNode);
        nodesCreated++;
        Set<String> visited = new HashSet<>();
        visited.add(serializeState(rootNode.state));

        while (!pq.isEmpty()) {
            if (nodesCreated > maxnodes) {
                System.out.println("Error: maxnodes limit (" + maxnodes + ") reached");
                return;
            }
            SearchNode currentNode = pq.poll();
            if (isGoalState(currentNode.state)) {
                System.out.println("Nodes created during search: " + nodesCreated);
                System.out.println("Solution length: " + currentNode.moves.size());
                for (String move : currentNode.moves) {
                    System.out.println(move);
                }
                return;
            }
            List<String> possibleMoves = Arrays.asList("left", "right", "up", "down");
            for (String move : possibleMoves) {
                int[][] newState = applyMove(currentNode.state, move);
                if (newState != null && !visited.contains(serializeState(newState))) {
                    List<String> newMoves = new ArrayList<>(currentNode.moves);
                    newMoves.add("move " + move);
                    int g = currentNode.g + 1; // Cost from start
                    int h = heuristic.equals("h1") ? misplacedTiles(newState) : manhattanDistance(newState);
                    int f = g + h; // Total cost
                    pq.add(new SearchNode(newState, newMoves, f, g));
                    nodesCreated++;
                    visited.add(serializeState(newState));
                }
            }
        }
        System.out.println("No solution found");
    }

    // Helper method to check if a given state is the goal state
    private boolean isGoalState(int[][] state) {
        int[][] goalState = {
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8}
        };
        return Arrays.deepEquals(state, goalState);
    }

    // Helper method to copy the current state
    private int[][] copyState(int[][] state) {
        int[][] newState = new int[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(state[i], 0, newState[i], 0, 3);
        }
        return newState;
    }

    /*
    // Helper method to serialize state for hashing
    private String serializeState(int[][] state) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(state[i][j]);
            }
        }
        return sb.toString();
    }
     */

    // Heuristic 1: Number of misplaced tiles
    private int misplacedTiles(int[][] state) {
        int misplaced = 0;
        int[][] goalState = {
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8}
        };
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] != 0 && state[i][j] != goalState[i][j]) {
                    misplaced++;
                }
            }
        }
        return misplaced;
    }

    // Heuristic 2: Manhattan distance
    private int manhattanDistance(int[][] state) {
        int distance = 0;
        int[][] goalPositions = {
                {0, 1}, {0, 2}, {0, 0}, // positions for tiles 1, 2, 0
                {1, 0}, {1, 1}, {1, 2}, // positions for tiles 3, 4, 5
                {2, 0}, {2, 1}, {2, 2}  // positions for tiles 6, 7, 8
        };
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int tile = state[i][j];
                if (tile != 0) {
                    int goalRow = goalPositions[tile][0];
                    int goalCol = goalPositions[tile][1];
                    distance += Math.abs(i - goalRow) + Math.abs(j - goalCol);
                }
            }
        }
        return distance;
    }

    // Apply move on state and return new state or null if invalid
    private int[][] applyMove(int[][] state, String move) {
        int[][] newState = copyState(state);
        int[] blankPos = findBlankTile(newState);
        int row = blankPos[0];
        int col = blankPos[1];
        switch (move) {
            case "up":
                if (row == 0)
                    return null;
                newState[row][col] = newState[row - 1][col];
                newState[row - 1][col] = 0;
                break;
            case "down":
                if (row == 2)
                    return null;
                newState[row][col] = newState[row + 1][col];
                newState[row + 1][col] = 0;
                break;
            case "left":
                if (col == 0)
                    return null;
                newState[row][col] = newState[row][col - 1];
                newState[row][col - 1] = 0;
                break;
            case "right":
                if (col == 2)
                    return null;
                newState[row][col] = newState[row][col + 1];
                newState[row][col + 1] = 0;
                break;
        }
        return newState;
    }

    public static double calculateBranchingFactor(int totalNodes, int depth) {
        double epsilon = 0.0001;  // Threshold for convergence
        double low = 1.0;
        double high = totalNodes;
        double bStar = (low + high) / 2;

        while (true) {
            double estimatedNodes = calculateTotalNodes(bStar, depth);
            if (Math.abs(estimatedNodes - totalNodes) < epsilon) {
                break;
            } else if (estimatedNodes < totalNodes) {
                low = bStar;
            } else {
                high = bStar;
            }
            bStar = (low + high) / 2;
        }

        return bStar;
    }

    private static double calculateTotalNodes(double branchingFactor, int depth) {
        double totalNodes = 0;
        for (int i = 0; i <= depth; i++) {
            totalNodes += Math.pow(branchingFactor, i);
        }
        return totalNodes;
    }

    // Inner class to represent a search node
    private static class SearchNode {
        int[][] state;
        List<String> moves;
        int cost; // For A* f = g + h
        int g; // Cost from start

        SearchNode(int[][] state, List<String> moves) {
            this.state = state;
            this.moves = moves;
        }

        SearchNode(int[][] state, List<String> moves, int cost, int g) {
            this.state = state;
            this.moves = moves;
            this.cost = cost;
            this.g = g;
        }
    }
/*
    public void printSearchComparisonTable() {
        // Array to hold different depths
        int[] depths = {6, 8, 10, 12, 14}; // You can add more depths if needed
        // Table headers
        System.out.printf("%-5s %-10s %-10s %-10s %-10s %-10s %-10s%n",
                "d", "BFS", "A*(h1)", "A*(h2)", "BFS", "A*(h1)", "A*(h2)");
        System.out.printf("%-5s %-10s %-10s %-10s %-10s %-10s %-10s%n",
                "", "Cost", "Cost", "Cost", "Branching", "Branching", "Branching");

        // Loop through the different depths
        for (int depth : depths) {
            // Reset puzzle to initial state or scrambled state with a specific solution length (depth)
            EightPuzzle puzzle = new EightPuzzle();
            puzzle.scrambleState(depth);  // Assuming scrambleToDepth is a function that scrambles the puzzle to a solvable state of given depth

            // Solve using BFS and get the number of nodes generated
            int bfsNodes = puzzle.solveBFS(1000);
            // Solve using A* with h1 and get the number of nodes generated
            int astarH1Nodes = puzzle.solveAStar(1);  // Assuming 1 is for h1 (misplaced tiles)
            // Solve using A* with h2 and get the number of nodes generated
            int astarH2Nodes = puzzle.solveAStar(2);  // Assuming 2 is for h2 (Manhattan distance)

            // Calculate effective branching factor for each
            double bfsBranching = calculateBranchingFactor(bfsNodes, depth);
            double astarH1Branching = calculateBranchingFactor(astarH1Nodes, depth);
            double astarH2Branching = calculateBranchingFactor(astarH2Nodes, depth);

            // Print the results for the current depth
            System.out.printf("%-5d %-10d %-10d %-10d %-10.2f %-10.2f %-10.2f%n",
                    depth, bfsNodes, astarH1Nodes, astarH2Nodes,
                    bfsBranching, astarH1Branching, astarH2Branching);
        }
    }

 */
}
