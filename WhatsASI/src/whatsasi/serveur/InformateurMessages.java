package whatsasi.serveur;

import java.util.Map;
import java.rmi.RemoteException;
import whatsasi.client.MessageCallbackInterface;
import whatsasi.serveur.conversations.Message;
import whatsasi.serveur.conversations.Messagerie;

public class InformateurMessages extends Thread {

    private Messagerie messagerie;
    private int refConv;
    private Message message;

    public InformateurMessages(Messagerie messagerie, int refConv, Message message) {
        this.messagerie = messagerie;
        this.refConv = refConv;
        this.message = message;
    }

    public void run() {
        try {
            this.messagerie.informerClients(refConv,message);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
