package model;

public class GameState {
    private Board board;
    private int currentPlayer;

    public GameState() {
        this.board = new Board();
        this.currentPlayer = Board.PLAYER1;
    }

    public Board getBoard() {
        return board;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchTurn() {
        currentPlayer = (currentPlayer == Board.PLAYER1) ? Board.PLAYER2 : Board.PLAYER1;
    }

    public boolean isGameOver() {
        return board.countPieces(Board.PLAYER1) == 0 || board.countPieces(Board.PLAYER2) == 0;
    }

    public int getWinner() {
        if (board.countPieces(Board.PLAYER1) == 0) return Board.PLAYER2;
        if (board.countPieces(Board.PLAYER2) == 0) return Board.PLAYER1;
        return -1;
    }
}
