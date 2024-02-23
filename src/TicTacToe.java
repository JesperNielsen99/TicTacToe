public class TicTacToe {
    char[][] board = new char[3][3];
    int[][] fieldValues = {{3,2,3}, {2,4,2}, {3,2,3}};

    TicTacToe() {
        setupBoard();
        setupFieldValues();
    }

    void setupBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
    }

    void setupFieldValues() {

    }

    void printBoard() {
        StringBuilder boardString = new StringBuilder();
        boardString.append("#######").append("\n");
        boardString.append("#").append(board[0][0]).append("#").append(board[0][1]).append("#").append(board[0][2]).append("#").append("\n");
        boardString.append("#######").append("\n");
        boardString.append("#").append(board[1][0]).append("#").append(board[1][1]).append("#").append(board[1][2]).append("#").append("\n");
        boardString.append("#######").append("\n");
        boardString.append("#").append(board[2][0]).append("#").append(board[2][1]).append("#").append(board[2][2]).append("#").append("\n");
        boardString.append("#######").append("\n");
        System.out.println(boardString.toString());
    }

    void printValueBoard() {
        StringBuilder boardString = new StringBuilder();
        boardString.append("#######").append("\n");
        boardString.append("#").append(fieldValues[0][0]).append("#").append(fieldValues[0][1]).append("#").append(fieldValues[0][2]).append("#").append("\n");
        boardString.append("#######").append("\n");
        boardString.append("#").append(fieldValues[1][0]).append("#").append(fieldValues[1][1]).append("#").append(fieldValues[1][2]).append("#").append("\n");
        boardString.append("#######").append("\n");
        boardString.append("#").append(fieldValues[2][0]).append("#").append(fieldValues[2][1]).append("#").append(fieldValues[2][2]).append("#").append("\n");
        boardString.append("#######").append("\n");
        System.out.println(boardString.toString());
    }

    public static void main(String[] args) {
        TicTacToe t1 = new TicTacToe();
        //t1.printBoard();
        t1.printValueBoard();
    }
}
