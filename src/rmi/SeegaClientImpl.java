package rmi;

import java.rmi.server.UnicastRemoteObject;
import utils.Message;
import client.SeegaClient;

public class SeegaClientImpl extends UnicastRemoteObject implements Player {
    private SeegaClient client;

    public SeegaClientImpl(SeegaClient client) throws Exception {
        this.client = client;
    }

    public void receiveMessage(Message msg) {
        client.handleReceivedMessage(msg);
    }
}
