package whatsasi.serveur.conversations;

import java.util.List;
import java.rmi.Remote;
import java.rmi.RemoteException;

import java.awt.Image;

import whatsasi.serveur.utilisateurs.Compte;
import whatsasi.serveur.filtrage.Filtre;
import whatsasi.client.MessageCallbackInterface;
import whatsasi.client.ConversationCallbackInterface;


import javax.swing.*;


public interface MessagerieInterface extends Remote {

    public boolean creerCompte(String pseudo, ImageIcon avatar, Mode mode, Filtre filtre) throws RemoteException;

    public boolean creerCompte(String pseudo, ImageIcon avatar, Mode mode, Filtre filtre, ConversationCallbackInterface convCallback) throws RemoteException;

    public boolean creerModerateur(String pseudo, ImageIcon avatar, Mode mode, Filtre filtre, ConversationCallbackInterface convCallback) throws RemoteException;

    public int creerConversation(List<Message> messages, String pseudo, String titre, Mode mode, List<MessageDeModeration> messagesDeModeration, MessageCallbackInterface callback) throws RemoteException;

    public void setIAIcon(ImageIcon img) throws RemoteException;

    public void addFilters(List<String> liste,String pseudo) throws RemoteException;

    public Compte getCompte(String pseudo) throws RemoteException;

    public void addMotInterdit(String mot, String pseudo) throws RemoteException;

    public void removeMotInterdit(String mot, String pseudo) throws RemoteException;

    public List<String> getFiltres(String pseudo) throws RemoteException;

    public void addUserToConv(String pseudo, int refConv, MessageCallbackInterface callback) throws RemoteException;

    public void removeUserFromConv(String pseudo, int refConv) throws RemoteException;

    public void removeUser(String pseudo) throws RemoteException;

    public boolean modifierPseudo(String old, String newPseudo) throws RemoteException;

    public void activerFiltre(String pseudo) throws RemoteException;

    public void supprimerMotInterdit(String pseudo,String mot) throws RemoteException;

    public void updateFilter(String pseudo,String ancienMot,String mot) throws RemoteException;

    public void desactiverFiltre(String pseudo) throws RemoteException;

    public boolean estActifFiltre(String pseudo) throws RemoteException;

    public List<String> getPseudos(int refConv) throws RemoteException;

    public boolean isPseudoAvailable(String pseudo) throws RemoteException;

    public List<Message> getContenu(int refConv) throws RemoteException;

    public void addMessage(String msg, int refConv, String pseudo) throws RemoteException;

    public void setPseudo(String pseudo, String nouveauPseudo) throws RemoteException;

    public void setAvatar(String pseudo, ImageIcon avatar) throws RemoteException;

    public Image getAvatar(String pseudo) throws RemoteException;

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

    public List<Message> getContenu(int refConv, String pseudo) throws RemoteException;

    public void supprimerMessage(int refConv, int index) throws RemoteException;
}
