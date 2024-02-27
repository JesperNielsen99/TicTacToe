import java.util.Scanner;

public class TicTacToe {
    static final int SIZE = 3;
    static final char EMPTY = ' ';
    static final char PLAYER_X = 'X';
    static final char PLAYER_O = 'O';
    char[][] board;
    final int[][] fieldValues = {{3,2,3}, {2,4,2}, {3,2,3}};
    Scanner scanner;

    TicTacToe() {
        board = new char[SIZE][SIZE];
        setupBoard();
        scanner = new Scanner(System.in);
    }

    void setupBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    boolean isValidMove(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE && board[row][col] == EMPTY;
    }

    // Method to make a move
    void makeMove(int row, int col, char player) {
        if (isValidMove(row, col)) {
            board[row][col] = player;
            isGameOver();
        }
    }

    // Method to check if the game is over
    boolean isGameOver() {
        return isWin(PLAYER_X) || isWin(PLAYER_O) || isDraw();
    }

    boolean isWin(char player) {
        // Check rows
        for (int i = 0; i < SIZE; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }
        }
        // Check columns
        for (int j = 0; j < SIZE; j++) {
            if (board[0][j] == player && board[1][j] == player && board[2][j] == player) {
                return true;
            }
        }
        // Check diagonals
        if ((board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
                (board[0][2] == player && board[1][1] == player && board[2][0] == player)) {
            return true;
        }
        return false;
    }

    boolean isDraw() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    //////////////////////////////////
    // Minimax algorithm with alpha-beta pruning
    int[] minimaxAlphaBeta(char[][] board, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (isWin(PLAYER_O)) {
            // Assign a very high score to encourage this move
            return new int[] {Integer.MAX_VALUE, -1, -1}; // Assuming MAX_SCORE is a large positive value
        }

        if (isWin(PLAYER_X)) {
            // Assign a very low score to avoid this move
            return new int[] {Integer.MIN_VALUE, -1, -1}; // Assuming MIN_SCORE is a large negative value
        }

        if (depth >= 2 || isGameOver()) {
            return new int[] {evaluate(board), -1, -1}; // Evaluate current board state
        }

        int bestScore;
        int[] bestMove = new int[] {-1, -1};

        if (maximizingPlayer) {
            bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = PLAYER_X;
                        if (!aiMoveLeadsToLoss(board, i, j)) { // Check if AI's move leads to a loss
                            int[] score = minimaxAlphaBeta(board, depth + 1, alpha, beta, false);
                            System.out.println("score 0: " + score[0]);
                            if (score[0] > bestScore) {
                                bestScore = score[0];
                                bestMove[0] = i;
                                bestMove[1] = j;
                            }
                            alpha = Math.max(alpha, bestScore);
                            if (beta <= alpha) {
                                board[i][j] = EMPTY; // Reset board
                                break; // Beta cutoff
                            }
                        }
                        board[i][j] = EMPTY; // Reset board
                    }
                }
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = PLAYER_O;
                        int[] score = minimaxAlphaBeta(board, depth + 1, alpha, beta, true);
                        board[i][j] = EMPTY;
                        if (score[0] < bestScore) {
                            bestScore = score[0];
                            bestMove[0] = i;
                            bestMove[1] = j;
                        }
                        beta = Math.min(beta, bestScore);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
        }
        return new int[] {bestScore, bestMove[0], bestMove[1]};
    }

    int evaluate(char[][] board) {
        int aiScore = 0;
        int opponentScore = 0;

        // Calculate score based on field values
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == PLAYER_O) {
                    aiScore += fieldValues[i][j];
                } else if (board[i][j] == PLAYER_X) {
                    opponentScore += fieldValues[i][j];
                }
            }
        }
        //System.out.println("player: " + opponentScore);

        // Return the difference between AI's score and opponent's score
        System.out.println("AI score: " + aiScore);
        System.out.println("Player score: " + opponentScore);
        printBoard();
        return aiScore - opponentScore;
    }

    boolean aiMoveLeadsToLoss(char[][] board, int row, int col) {
        // Simulate AI's move
        board[row][col] = PLAYER_O;

        // Check opponent's possible moves after AI's move
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    // Simulate opponent's move
                    board[i][j] = PLAYER_X;

                    // Check if opponent wins after this move
                    if (isWin(PLAYER_X)) {
                        // Reset the board to its original state
                        board[i][j] = EMPTY;
                        board[row][col] = EMPTY;
                        return true; // AI's move can lead to a loss
                    }

                    // Reset the board for the next simulation
                    board[i][j] = EMPTY;
                }
            }
        }

        // Reset the board to its original state
        board[row][col] = EMPTY;

        return false; // AI's move does not lead to a loss
    }
    //////////////////////////////////

    void printBoard() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        String boardString = "#######" + "\n" +
                "#" + board[0][0] + "#" + board[0][1] + "#" + board[0][2] + "#" + "\n" +
                "#######" + "\n" +
                "#" + board[1][0] + "#" + board[1][1] + "#" + board[1][2] + "#" + "\n" +
                "#######" + "\n" +
                "#" + board[2][0] + "#" + board[2][1] + "#" + board[2][2] + "#" + "\n" +
                "#######" + "\n";
        System.out.println(boardString);
    }

    void displayOutcome() {
        if (isWin(TicTacToe.PLAYER_X)) {
            System.out.println("Congratulations! You win!");
        } else if (isWin(TicTacToe.PLAYER_O)) {
            System.out.println("Sorry, you lose. The AI wins!");
        } else {
            System.out.println("It's a draw!");
        }
    }

    void playerMove() {
        System.out.println("Enter your move (row and column, separated by space):");
        int row = scanner.nextInt();
        int col = scanner.nextInt();
        if (board[row][col] == EMPTY) {
            makeMove(row, col, TicTacToe.PLAYER_X);
        } else {
            System.out.println("That cell is occupied");
            playerMove();
        }
    }

    void aiMove() {
        int[] bestMove = minimaxAlphaBeta(board, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
        System.out.println(bestMove[0] + " : (" + bestMove[1] + ", " + bestMove[2] + ")");
        makeMove(bestMove[1], bestMove[2], TicTacToe.PLAYER_O);
    }

    void resetGame() {
        setupBoard();
        run();
    }

    void run() {
        while (!isGameOver()) {
            this.printBoard();
            playerMove();
            if (isGameOver()) {
                break;
            }
            this.printBoard();
            aiMove();
            if (isGameOver()) {
                break;
            }
        }

        // Display the final state of the board and the outcome
        printBoard();
        displayOutcome();
        resetGame();
    }

    public static void main(String[] args) {
        TicTacToe t1 = new TicTacToe();
        t1.run();
    }
}
