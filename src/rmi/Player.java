package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import utils.Message;

public interface Player extends Remote {
    void receiveMessage(Message msg) throws RemoteException;
}
