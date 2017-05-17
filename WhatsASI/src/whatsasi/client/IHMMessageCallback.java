package whatsasi.client;

import whatsasi.serveur.conversations.Message;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class IHMMessageCallback extends UnicastRemoteObject implements MessageCallbackInterface {

    private int refConv = -1;
    private String pseudo;
    private MessagerieClient client;

    public IHMMessageCallback(String pseudo, MessagerieClient client) throws RemoteException {
        super();
        this.pseudo = pseudo;
        this.client = client;
    }

    public IHMMessageCallback(int refConv, String pseudo, MessagerieClient client) throws RemoteException {
        this(pseudo,client);
        this.refConv = refConv;
    }

    public void nouveauMessage(int refConv, Message message) throws RemoteException {
        if (this.refConv == refConv && !(message.getPseudo()).equals(this.pseudo)) {
            client.receiveMessage(message);
        }
    }

    public void supprimerMessage(int refConv, int index) throws RemoteException {
        if (this.refConv == refConv) {
            client.removeMessage(index);
        }
    }


    public void setRefConv(int refConv) throws RemoteException {
        this.refConv = refConv;
    }

    public void setPseudo(String pseudo) throws RemoteException {
        this.pseudo = pseudo;
    }
}
