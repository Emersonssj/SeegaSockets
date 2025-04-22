package server;

import java.net.*;
import java.io.*;
import java.util.*;

public class SeegaServer {
    private static final int PORT = 12345;
    private static final List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor Seega aguardando jogadores...");

            while (clients.size() < 2) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket, clients.size());
                clients.add(handler);
                new Thread(handler).start();
                System.out.println("Jogador " + clients.size() + " conectado.");
            }

            // Inicia o jogo para ambos
            for (ClientHandler client : clients) {
                client.sendInitMessage();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(Object message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }
}
