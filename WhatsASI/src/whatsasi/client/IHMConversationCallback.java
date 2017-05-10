package whatsasi.client;

import whatsasi.serveur.conversations.Message;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class IHMConversationCallback extends UnicastRemoteObject implements ConversationCallbackInterface {

    private MessagerieClient client;

    public IHMConversationCallback(MessagerieClient client) throws RemoteException {
        this.client = client;
    }

    public void nouvelleConversation(int refConv) throws RemoteException {
            client.receiveConversation(refConv);
    }

}
