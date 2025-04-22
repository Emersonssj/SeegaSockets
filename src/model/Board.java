// model/Board.java
package model;

public class Board {
    public static final int SIZE = 5;
    public static final int EMPTY = -1;
    public static final int PLAYER1 = 0;
    public static final int PLAYER2 = 1;

    private int[][] board = new int[SIZE][SIZE];
    private boolean placementPhase = true;
    private int piecesPlacedP1 = 0;
    private int piecesPlacedP2 = 0;

    public Board() {
        // Inicializa o tabuleiro com -1
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                board[i][j] = EMPTY;

        // Espaço central sempre vazio na fase de colocação
        board[SIZE/2][SIZE/2] = EMPTY;
    }

    public int[][] getBoard() {
        return board;
    }

    public boolean isPlacementPhase() {
        return placementPhase;
    }

    public boolean placePiece(int row, int col, int player) {
        if (!placementPhase) return false;
        if (board[row][col] != EMPTY) return false;

        board[row][col] = player;

        if (player == PLAYER1) piecesPlacedP1++;
        else piecesPlacedP2++;

        if (piecesPlacedP1 == 12 && piecesPlacedP2 == 12) {
            placementPhase = false;
        }

        return true;
    }

    public boolean movePiece(int fromRow, int fromCol, int toRow, int toCol, int player) {
        if (placementPhase) return false;
        if (!isValidMove(fromRow, fromCol, toRow, toCol, player)) return false;

        board[fromRow][fromCol] = EMPTY;
        board[toRow][toCol] = player;

        capture(toRow, toCol, player);

        return true;
    }

    private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, int player) {
        if (board[fromRow][fromCol] != player) return false;
        if (board[toRow][toCol] != EMPTY) return false;

        // Só pode andar para cima/baixo/esquerda/direita uma casa
        if (Math.abs(fromRow - toRow) + Math.abs(fromCol - toCol) != 1) return false;

        return true;
    }

    private void capture(int row, int col, int player) {
        int opponent = (player == PLAYER1) ? PLAYER2 : PLAYER1;

        // Checa as quatro direções
        checkCapture(row, col, -1, 0, player, opponent); // cima
        checkCapture(row, col, 1, 0, player, opponent);  // baixo
        checkCapture(row, col, 0, -1, player, opponent); // esquerda
        checkCapture(row, col, 0, 1, player, opponent);  // direita
    }

    private void checkCapture(int row, int col, int dRow, int dCol, int player, int opponent) {
        int middleRow = row + dRow;
        int middleCol = col + dCol;
        int beyondRow = row + 2 * dRow;
        int beyondCol = col + 2 * dCol;

        if (isInside(middleRow, middleCol) && isInside(beyondRow, beyondCol)) {
            if (board[middleRow][middleCol] == opponent && board[beyondRow][beyondCol] == player) {
                board[middleRow][middleCol] = EMPTY;
            }
        }
    }

    private boolean isInside(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    public int countPieces(int player) {
        int count = 0;
        for (int[] row : board)
            for (int cell : row)
                if (cell == player) count++;
        return count;
    }
}
