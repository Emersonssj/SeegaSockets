package client;

import model.*;
import utils.Message;

import javax.swing.*;
import java.awt.*;

public class GameUI extends JFrame {
    private JButton[][] buttons = new JButton[5][5];
    private SeegaClient client;
    private GameState gameState;
    private int playerId;
    private int selectedRow = -1, selectedCol = -1;

    public GameUI(SeegaClient client, GameState gameState, int playerId) {
        this.client = client;
        this.gameState = gameState;
        this.playerId = playerId;

        setTitle("Seega - Jogador " + playerId);
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 5));

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton btnDesistir = new JButton("Desistir");
        btnDesistir.addActionListener(e -> handleDesistencia());
        topPanel.add(btnDesistir, BorderLayout.EAST);
        add(topPanel);

        getContentPane().setLayout(new BorderLayout());
        JPanel boardPanel = new JPanel(new GridLayout(5, 5));
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                JButton btn = new JButton();
                buttons[i][j] = btn;
                int row = i;
                int col = j;
                btn.addActionListener(e -> handleClick(row, col));
                boardPanel.add(btn);
            }
        }
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(boardPanel, BorderLayout.CENTER);

        updateBoard();
        setVisible(true);
    }

    private void handleDesistencia() {
        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja desistir?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            client.sendMessage(new Message(Message.Type.FORFEIT, playerId));
            JOptionPane.showMessageDialog(this, "Você desistiu.");
            System.exit(0);
        }
    }

    private void handleClick(int row, int col) {
        Board board = gameState.getBoard();

        if (gameState.getCurrentPlayer() != playerId) {
            JOptionPane.showMessageDialog(this, "Não é seu turno.");
            return;
        }

        if (board.isPlacementPhase()) {
            boolean placed = board.placePiece(row, col, playerId);
            if (placed) {
                sendMove("place:" + row + "," + col);
                gameState.switchTurn();
                updateBoard();
            }
        } else {
            if (selectedRow == -1) {
                // seleciona a peça
                if (board.getBoard()[row][col] == playerId) {
                    selectedRow = row;
                    selectedCol = col;
                    buttons[row][col].setBackground(Color.YELLOW);
                }
            } else {
                // tenta mover
                boolean moved = board.movePiece(selectedRow, selectedCol, row, col, playerId);
                if (moved) {
                    sendMove("move:" + selectedRow + "," + selectedCol + "->" + row + "," + col);
                    selectedRow = selectedCol = -1;
                    updateBoard();

                    if (gameState.isGameOver()) {
                        int winner = gameState.getWinner();
                        JOptionPane.showMessageDialog(this, "Jogador " + winner + " venceu!");
                        client.sendMessage(new Message(Message.Type.WINNER, winner));  // <--- isso é essencial
                        System.exit(0);  // encerra o cliente vencedor
                    } else {
                        gameState.switchTurn();
                    }
                }
            }
        }
    }

    private void sendMove(String text) {
        client.sendMessage(new Message(Message.Type.MOVE, text));
    }

    public void updateBoard() {
        int[][] board = gameState.getBoard().getBoard();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                JButton btn = buttons[i][j];
                if (board[i][j] == Board.EMPTY) {
                    btn.setText("");
                    btn.setBackground(Color.LIGHT_GRAY);
                } else if (board[i][j] == Board.PLAYER1) {
                    btn.setText("●");
                    btn.setForeground(Color.RED);
                } else {
                    btn.setText("●");
                    btn.setForeground(Color.BLUE);
                }
            }
        }
    }
}
