package whatsasi.serveur;

import java.util.Map;
import java.rmi.RemoteException;
import whatsasi.client.MessageCallbackInterface;
import whatsasi.serveur.conversations.Message;
import whatsasi.serveur.conversations.Messagerie;

public class InformateurMessages extends Thread {

    private Messagerie messagerie;
    private int refConv, index;
    private Message message;
    private boolean supprimer;

    public InformateurMessages(Messagerie messagerie, int refConv, Message message) {
        this.messagerie = messagerie;
        this.refConv = refConv;
        this.message = message;
        this.supprimer = false;
        this.index = -1;
    }

    public InformateurMessages(Messagerie messagerie, int refConv, Message message, boolean supprimer, int index) {
        this(messagerie, refConv, message);
        this.supprimer = supprimer;
        this.index = index;
    }

    public void run() {
        try {
            if (!supprimer) {
                this.messagerie.informerClients(refConv,message);
            }
            else {
                this.messagerie.informerClientsSuppression(refConv, index);
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
