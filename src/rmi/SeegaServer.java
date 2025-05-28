package rmi;

import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.util.*;
import utils.Message;

public class SeegaServer extends UnicastRemoteObject implements SeegaRemoteInterface {
    private final List<Player> clients = new ArrayList<>();

    public SeegaServer() throws Exception {
        super();
    }

    public synchronized void registerClient(Player client) throws java.rmi.RemoteException {
        clients.add(client);
        System.out.println("Cliente registrado. Total: " + clients.size());

        if (clients.size() == 2) {
            clients.get(0).receiveMessage(new Message(Message.Type.INIT, 0));
            clients.get(1).receiveMessage(new Message(Message.Type.INIT, 1));
        }
    }

    public synchronized void sendMessage(Message msg, Player sender) throws java.rmi.RemoteException {
        for (Player c : clients) {
            if (!c.equals(sender)) {
                c.receiveMessage(msg);
            }
        }
    }

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099); // inicia RMI registry
            SeegaServer server = new SeegaServer();
            Naming.rebind("SeegaGame", server);
            System.out.println("Servidor RMI pronto!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
