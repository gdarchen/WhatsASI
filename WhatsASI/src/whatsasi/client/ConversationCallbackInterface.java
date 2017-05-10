package whatsasi.client;

import whatsasi.serveur.conversations.Message;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ConversationCallbackInterface extends Remote {
    public void nouvelleConversation(int refConv) throws RemoteException;
}
