package whatsasi.client;

import java.text.*;
import whatsasi.serveur.conversations.Message;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class TerminalMessageCallback extends UnicastRemoteObject implements MessageCallbackInterface {

    private int refConv = -1;
    private String pseudo;
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";

    public TerminalMessageCallback(String pseudo) throws RemoteException {
        this.pseudo = pseudo;
    }

    public TerminalMessageCallback(int refConv, String pseudo) throws RemoteException {
        this.refConv = refConv;
        this.pseudo = pseudo;
    }

    public void nouveauMessage(int refConv, Message message) throws RemoteException {
        if (this.refConv == refConv) {
            if (!(message.getPseudo()).equals(this.pseudo)) {
                StringBuilder res = new StringBuilder();
                res.append("\n"+ANSI_CYAN+message.getPseudo()+ANSI_RESET);
                res.append(" : \n\n");
                DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                res.append(dateFormat.format(message.getDate())+"   "+message.getMessage());
                res.append("\n\n");
                System.out.println(res.toString());
            }
        }
    }

    public void supprimerMessage(int refConv, int index) throws RemoteException {}


    public void setRefConv(int refConv) throws RemoteException {
        this.refConv = refConv;
    }

    public void setPseudo(String pseudo) throws RemoteException {
        this.pseudo = pseudo;
    }
}
