package whatsasi.client;

import whatsasi.serveur.conversations.Message;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessageCallbackInterface extends Remote {
    public void nouveauMessage(int refConv, Message message) throws RemoteException;
    public void setRefConv(int refConv) throws RemoteException;
}
