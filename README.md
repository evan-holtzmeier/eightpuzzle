# EightPuzzle Solver

This is a Java-based implementation of the classic 8-puzzle problem, supporting user-defined commands, multiple solving algorithms (DFS, BFS, A*), and test automation through a command file. The project was cleaned and restructured as a Maven-based IntelliJ project for better portability and maintainability.

## Features

- Accepts command-driven input via file
- Solves puzzles using:
  - Depth-First Search (DFS)
  - Breadth-First Search (BFS)
  - A* with two heuristics:
    - h1: number of misplaced tiles
    - h2: total Manhattan distance
- Validates and manipulates puzzle states
- Reports errors for invalid moves, states, or commands
- Outputs solution paths and statistics

## Folder Structure

    EightPuzzle_AI_v2/
    ├── pom.xml
    ├── src/
    │   ├── main/
    │   │   ├── java/
    │   │   │   └── eightpuzzle/
    │   │   │       └── EightPuzzle.java
    │   └── resources/
    │       └── testcmds.txt

## Build Instructions

This project uses Apache Maven for compiling and running.

1. Install Maven and ensure it's available in your PATH:
       mvn -v

2. Compile the project:
       mvn compile

## Run Instructions

To execute the program using the provided test command file:

    mvn exec:java "-Dexec.mainClass=eightpuzzle.EightPuzzle" "-Dexec.args=src/main/resources/testcmds.txt"

Note: If you're using PowerShell, quote the entire `-Dexec...` expressions as shown. In Git Bash or Linux/macOS terminals, use single quotes instead.

## About the Test File

The file `testcmds.txt` runs through a complete test suite:
- Verifies initial state printing
- Sets valid and invalid puzzle configurations
- Moves tiles with valid and invalid commands
- Scrambles the puzzle with 10 random moves
- Attempts to solve the puzzle using all three algorithms

### Why DFS and BFS Fail

After scrambling the puzzle, the state remains solvable, but both DFS and BFS often hit the `maxnodes = 100` limit without reaching the goal. This behavior is intentional and included to demonstrate that A* search, guided by heuristics, is significantly more efficient than blind search strategies like DFS and BFS for large or complex search spaces.

## Example Output

Sample A* output:

    solveAStar h2 100
    Nodes created during search: 22
    Solution length: 6
    move up
    move right
    move right
    move up
    move left
    move left

## Troubleshooting

- If `mvn` is not recognized, make sure Maven is installed and your system’s PATH includes the `bin` folder.
- If you see an error like `Unknown lifecycle phase`, quote the entire `-Dexec.mainClass=...` and `-Dexec.args=...` expressions when running the command.
- If DFS and BFS return errors or fail to solve, this is expected behavior for scrambled puzzles under limited node caps.

## License

This project was developed for educational and demonstration purposes.

## Author

Evan Holtzmeier  
GitHub: https://github.com/evan-holtzmeier
