package whatsasi.client;

import whatsasi.serveur.conversations.Message;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class IHMMessageCallback extends UnicastRemoteObject implements MessageCallbackInterface {

    private int refConv = -1;
    private String pseudo;

    public IHMMessageCallback(String pseudo) throws RemoteException {
        this.pseudo = pseudo;
    }

    public IHMMessageCallback(int refConv, String pseudo) throws RemoteException {
        this.refConv = refConv;
        this.pseudo = pseudo;
    }

    public void nouveauMessage(int refConv, Message message) throws RemoteException {
        if (this.refConv == refConv) {
            if (!(message.getPseudo()).equals(this.pseudo)) {
                StringBuilder res = new StringBuilder();
                res.append(message.getPseudo());
                res.append(" : \n");
                res.append("       "+message.getMessage());
                res.append("\n\n");
                System.out.println(res.toString());
            }
        }
    }

    public void setRefConv(int refConv) throws RemoteException {
        this.refConv = refConv;
    }
}
