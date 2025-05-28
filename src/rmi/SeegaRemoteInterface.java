package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import utils.Message;

public interface SeegaRemoteInterface extends Remote {
    void registerClient(Player client) throws RemoteException;
    void sendMessage(Message msg, Player sender) throws RemoteException;
}
