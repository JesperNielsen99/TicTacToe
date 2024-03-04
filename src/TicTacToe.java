import javax.sound.sampled.Line;
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

    void makeMove(int row, int col, char player) {
        if (isValidMove(row, col)) {
            board[row][col] = player;
            isGameOver();
        }
    }

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

    int[] minimaxAlphaBeta(char[][] board, int depth, int alpha, int beta, boolean maximizingPlayer) {
        //Node is leaf
        if (depth >= 5 || isGameOver()) {
            return new int[] {evaluate(board)};
        }

        int bestScore;
        int[] bestMove = new int[] {-1, -1};

        //node is max
         if (maximizingPlayer) {
            bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = PLAYER_O;
                        if (isWin(PLAYER_O)) {
                            bestScore = Integer.MAX_VALUE;
                            bestMove[0] = i;
                            bestMove[1] = j;
                            board[i][j] = EMPTY;
                            break;
                        } else {
                            int[] score = minimaxAlphaBeta(board, depth + 1, alpha, beta, false);
                            if (aiMoveLeadsToLoss(board, i, j)) {
                                if (score[0] > bestScore) {
                                    bestScore = score[0];
                                    bestMove[0] = i;
                                    bestMove[1] = j;
                                }
                            } else {
                                score[0]+=2;
                                if (score[0]> bestScore) {
                                    bestScore = score[0];
                                    bestMove[0] = i;
                                    bestMove[1] = j;
                                }
                            }
                            alpha = Math.max(alpha, bestScore);
                            if (beta <= alpha) {
                                board[i][j] = EMPTY;
                                break;
                            }
                            board[i][j] = EMPTY;
                        }
                        board[i][j] = EMPTY;
                    }
                }
            }
        //node is min
        } else {
            bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = PLAYER_X;
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
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == PLAYER_O) {
                    aiScore += fieldValues[i][j];
                } else if (board[i][j] == PLAYER_X) {
                    opponentScore += fieldValues[i][j];
                }
            }
        }
        return aiScore - opponentScore;
    }

    boolean aiMoveLeadsToLoss(char[][] board, int row, int col) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    board[i][j] = PLAYER_X;
                    if (isWin(PLAYER_X)) {
                        board[i][j] = EMPTY;
                        return true;
                    }
                    board[i][j] = EMPTY;
                }
            }
        }
        return false;
    }

    void printBoard() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        String boardString =
                "_______\n" +
                "|" + board[0][0] + "|" + board[0][1] + "|" + board[0][2] + "|\n" +
                "|-----|\n" +
                "|" + board[1][0] + "|" + board[1][1] + "|" + board[1][2] + "|\n" +
                "|-----|\n" +
                "|" + board[2][0] + "|" + board[2][1] + "|" + board[2][2] + "|\n" +
                "-------\n";
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
        int[] bestMove = minimaxAlphaBeta(board, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        System.out.println("best AI move score: " + bestMove[0]);
        System.out.println("best AI move: (" + bestMove[1] + ", " + bestMove[2] + ")");
        makeMove(bestMove[1], bestMove[2], TicTacToe.PLAYER_O);
    }

    void resetGame() {
        System.out.println();
        System.out.println("Starting new Game");
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
            aiMove();
            if (isGameOver()) {
                break;
            }
        }

        printBoard();
        displayOutcome();
        resetGame();
    }

    public static void main(String[] args) {
        TicTacToe t1 = new TicTacToe();
        t1.run();
    }
}
