package whatsasi.serveur.conversations;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import whatsasi.serveur.utilisateurs.Compte;
import whatsasi.serveur.utilisateurs.Utilisateur;
import whatsasi.serveur.filtrage.Filtre;
import whatsasi.serveur.conversations.Mode;
import java.rmi.Remote;
import java.rmi.RemoteException;
import whatsasi.client.MessageCallbackInterface;

public interface MessagerieInterface extends Remote {

    public boolean creerCompte(String pseudo,ImageIcon avatar,Mode mode,Filtre filtre) throws RemoteException;

    public int creerConversation(List<Message> messages,String pseudo,String titre,Mode mode,List<MessageDeModeration> messagesDeModeration, MessageCallbackInterface callback) throws RemoteException;

    public Compte getCompte(String pseudo) throws RemoteException;

    public void addMotInterdit(String mot,String pseudo) throws RemoteException;

    public void removeMotInterdit(String mot,String pseudo) throws RemoteException;

    public void addUserToConv(String pseudo,int refConv, MessageCallbackInterface callback) throws RemoteException;

    public void removeUserFromConv(String pseudo,int refConv) throws RemoteException;

    public boolean modifierPseudo(String old,String newPseudo) throws RemoteException;

    public List<String> getPseudos(int refConv) throws RemoteException;

    public boolean isPseudoAvailable(String pseudo) throws RemoteException;

    public List<Message> getContenu(int refConv) throws RemoteException;

    public void addMessage(String msg, int refConv, String pseudo) throws RemoteException;

    public void setPseudo(String pseudo, String nouveauPseudo) throws RemoteException;

    public void setAvatar(String pseudo, ImageIcon avatar) throws RemoteException;

    public ImageIcon getAvatar(String pseudo) throws RemoteException;

    public void setMode(String pseudo, Mode mode) throws RemoteException;

    public Mode getMode(String pseudo) throws RemoteException;

    public void addCompte(Compte compte) throws RemoteException;

    public void removeCompte(String pseudo) throws RemoteException;

    public void addConversation(Conversation conv) throws RemoteException;

    public void removeConversation(Conversation conv) throws RemoteException;

    public List<Compte> getComptes() throws RemoteException;

    public void setComptes(List<Compte> comptes) throws RemoteException;

    public List<Conversation> getConversations() throws RemoteException;

    public Conversation getConversation(int refConv) throws RemoteException;

    public void setConversations(List<Conversation> conversations) throws RemoteException;

    public String getTitreConv(int refConv) throws RemoteException;

    public String sayHi() throws RemoteException;

    public String contenuToString(int refConv, String pseudo) throws RemoteException;
}
