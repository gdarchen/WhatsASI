package whatsasi.serveur;

import java.util.Map;
import java.rmi.RemoteException;
import whatsasi.client.MessageCallbackInterface;
import whatsasi.serveur.conversations.Message;
import whatsasi.serveur.conversations.Messagerie;

public class InformateurConversations extends Thread {

    private Messagerie messagerie;
    private int refConv;
    private Message message;

    public InformateurConversations(Messagerie messagerie, int refConv) {
        this.messagerie = messagerie;
        this.refConv = refConv;
    }

    public void run() {
        try {
            this.messagerie.informerClientsNouvelleConv(refConv);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
