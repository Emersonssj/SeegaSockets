package server;

import java.io.*;
import java.net.*;
import utils.Message;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private int playerId;

    public ClientHandler(Socket socket, int playerId) {
        this.socket = socket;
        this.playerId = playerId;
    }

    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in  = new ObjectInputStream(socket.getInputStream());

            while (true) {
                Message msg = (Message) in.readObject();
                System.out.println("Mensagem recebida: " + msg.type);
                SeegaServer.broadcast(msg, this);
            }
        } catch (Exception e) {
            System.out.println("Jogador " + playerId + " desconectado.");
        }
    }

    public void sendInitMessage() {
        sendMessage(new Message(Message.Type.INIT, playerId));
    }

    public void sendMessage(Object message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            System.out.println("Erro ao enviar mensagem.");
        }
    }
}
