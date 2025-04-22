package client;

import java.io.*;
import java.net.*;
import utils.Message;
import model.GameState;

import javax.swing.*;

public class SeegaClient {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private int playerId;

    private GameState gameState;
    private GameUI gameUI;
    private ChatUI chatUI;


    public SeegaClient(String serverAddress) {
        try {
            socket = new Socket(serverAddress, 12345);
            out = new ObjectOutputStream(socket.getOutputStream());
            in  = new ObjectInputStream(socket.getInputStream());

            new Thread(() -> listen()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listen() {
        try {
            while (true) {
                Message msg = (Message) in.readObject();
                switch (msg.type) {
                    case INIT:
                        playerId = (int) msg.content;
                        gameState = new GameState();
                        gameUI = new GameUI(this, gameState, playerId);
                        chatUI = new ChatUI(this);
                        break;
                    case CHAT:
                        chatUI.receiveMessage("Adversário: " + msg.content);
                        break;
                    case FORFEIT:
                        JOptionPane.showMessageDialog(null, "O adversário desistiu. Você venceu!");
                        System.exit(0);
                        break;
                    case MOVE:
                        // reexecuta o movimento do outro jogador
                        String move = (String) msg.content;
                        if (move.startsWith("place:")) {
                            String[] parts = move.substring(6).split(",");
                            int r = Integer.parseInt(parts[0]);
                            int c = Integer.parseInt(parts[1]);
                            gameState.getBoard().placePiece(r, c, 1 - playerId);
                        } else if (move.startsWith("move:")) {
                            String[] parts = move.substring(5).split("->");
                            String[] from = parts[0].split(",");
                            String[] to = parts[1].split(",");
                            int fr = Integer.parseInt(from[0]);
                            int fc = Integer.parseInt(from[1]);
                            int tr = Integer.parseInt(to[0]);
                            int tc = Integer.parseInt(to[1]);
                            gameState.getBoard().movePiece(fr, fc, tr, tc, 1 - playerId);
                        }
                        gameState.switchTurn();
                        gameUI.updateBoard();
                        break;
                    case WINNER:
                        int winner = (int) msg.content;
                        JOptionPane.showMessageDialog(null, "Jogador " + winner + " venceu!");
                        System.exit(0);
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Conexão encerrada.");
        }
    }

    public void sendMessage(Message msg) {
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            System.out.println("Erro ao enviar.");
        }
    }
}
