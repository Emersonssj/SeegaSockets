package client;

import rmi.*;
import utils.*;
import model.*;

import javax.swing.*;
import java.rmi.Naming;

public class SeegaClient {
    private SeegaRemoteInterface serverInterface;
    private Player callback;

    private GameState gameState;
    private GameUI gameUI;
    private ChatUI chatUI;
    private int playerId;

    public SeegaClient(String host) {
        try {
            serverInterface = (SeegaRemoteInterface) Naming.lookup("rmi://" + host + "/SeegaGame");
            callback = new SeegaClientImpl(this);
            serverInterface.registerClient(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleReceivedMessage(Message msg) {
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
            case MOVE:
                processMove((String) msg.content);
                gameState.switchTurn();
                gameUI.updateBoard();
                break;
            case FORFEIT:
                JOptionPane.showMessageDialog(null, "O adversário desistiu. Você venceu!");
                System.exit(0);
                break;
            case WINNER:
                int winner = (int) msg.content;
                JOptionPane.showMessageDialog(null, "Jogador " + winner + " venceu!");
                System.exit(0);
                break;
        }
    }

    private void processMove(String move) {
        if (move.startsWith("place:")) {
            String[] parts = move.substring(6).split(",");
            gameState.getBoard().placePiece(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), 1 - playerId);
        } else if (move.startsWith("move:")) {
            String[] parts = move.substring(5).split("->");
            String[] from = parts[0].split(",");
            String[] to = parts[1].split(",");
            gameState.getBoard().movePiece(
                    Integer.parseInt(from[0]), Integer.parseInt(from[1]),
                    Integer.parseInt(to[0]), Integer.parseInt(to[1]), 1 - playerId);
        }
    }

    public void sendMessage(Message msg) {
        try {
            serverInterface.sendMessage(msg, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
