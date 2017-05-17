package whatsasi.serveur.conversations;

import java.awt.*;
import java.text.*;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.rmi.RemoteException;

import whatsasi.serveur.InformateurMessages;
import whatsasi.serveur.InformateurConversations;
import whatsasi.serveur.utilisateurs.Compte;
import whatsasi.serveur.utilisateurs.Utilisateur;
import whatsasi.serveur.utilisateurs.IA;
import whatsasi.serveur.filtrage.Filtre;
import whatsasi.serveur.conversations.Mode;
import whatsasi.serveur.conversations.Message;
import whatsasi.client.MessageCallbackInterface;
import whatsasi.client.ConversationCallbackInterface;

import javax.swing.*;


public class Messagerie implements MessagerieInterface {

    private IA ia;
    private Map<String, Compte> comptes;
    private Map<Integer, Conversation> conversations;
    private Map<String, MessageCallbackInterface> callbacks;
    private Map<String, ConversationCallbackInterface> convCallbacks;
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";

    public Messagerie() {
        this.comptes = new HashMap<>();
        this.conversations = new HashMap<>();
        this.callbacks = new HashMap<>();
        this.convCallbacks = new HashMap<String, ConversationCallbackInterface>();
        this.ia = new IA(null);
        this.comptes.put(ia.getPseudo(), ia);
    }

    public void setIAIcon(ImageIcon img){
        this.ia.setAvatar(img);
    }

    @Override
    public boolean creerCompte(String pseudo, ImageIcon avatar, Mode mode, Filtre filtre) {
        if (isPseudoAvailable(pseudo)) {
            addCompte(new Utilisateur(pseudo, avatar, mode, filtre));
            return true;
        }
        else
            return false;
    }

    public boolean creerCompte(String pseudo, ImageIcon avatar, Mode mode, Filtre filtre, ConversationCallbackInterface convCallback) {
        this.convCallbacks.put(pseudo, convCallback);
        return this.creerCompte(pseudo,avatar, mode, filtre);
    }

    public boolean creerModerateur(String pseudo, ImageIcon avatar, Mode mode, Filtre filtre, ConversationCallbackInterface convCallback) {
        this.convCallbacks.put(pseudo, convCallback);
        boolean res = this.creerCompte(pseudo,avatar, mode, filtre);
        ((Utilisateur)this.comptes.get(pseudo)).setEstModerateur(true);
        return res;
    }

    public boolean modifierPseudo(String old, String newPseudo) throws RemoteException {
        if (isPseudoAvailable(newPseudo)) {
            this.setPseudo(old, newPseudo);
            MessageCallbackInterface callback;
            if ((callback = this.callbacks.remove(old)) != null) {
                this.callbacks.put(newPseudo, callback);
                callback.setPseudo(newPseudo);
            }
            this.convCallbacks.put(newPseudo, this.convCallbacks.remove(old));
            return true;
        }
        else
            return false;
    }

    public int creerConversation(List<Message> messages, String pseudo, String titre, Mode mode, List<MessageDeModeration> messagesDeModeration, MessageCallbackInterface callback) throws RemoteException {
        Conversation c = new Conversation(messages, pseudo, titre, mode, messagesDeModeration);
        this.addConversation(c);
        this.getConversation(c.getRefConv()).addUtilisateur(this.ia.getPseudo(), Mode.DEFAUT);
        callback.setRefConv(c.getRefConv());
        this.callbacks.put(pseudo, callback);
        int refConv = c.getRefConv();
        InformateurConversations thread = new InformateurConversations(this, refConv);
        thread.start();
        return refConv;
    }

    public Compte getCompte(String pseudo) {
        if (this.comptes.containsKey(pseudo)) {
            return this.comptes.get(pseudo);
        }
        return null;
    }

    public void addFilters(List<String> liste,String pseudo){
        Utilisateur c = (Utilisateur)getCompte(pseudo);
        for (String mot : liste)
            c.addMotInterdit(mot);
    }

    public void addMotInterdit(String mot, String pseudo) {
        Compte c = getCompte(pseudo);
        if (c instanceof Utilisateur)
            ((Utilisateur)c).addMotInterdit(mot);
    }

    public void removeMotInterdit(String mot, String pseudo) {
        Compte c = getCompte(pseudo);
        if (c instanceof Utilisateur)
            ((Utilisateur)c).removeMotInterdit(mot);
    }

    public boolean isPseudoAvailable(String pseudo) {
        if (getCompte(pseudo) == null)
            return true;
        else
            return false;
    }

    public Conversation getConversation(int refConv) {
        if (this.conversations.containsKey(refConv)) {
            return this.conversations.get(refConv);
        }
        return null;
    }

    public List<Conversation> getConversations() {
        return new ArrayList<Conversation>(this.conversations.values());
    }

    public List<Message> getContenu(int refConv) {
        if (getConversation(refConv) != null)
            return getConversation(refConv).getContenu();
        return null;
    }

    public IA getIA() {
      return this.ia ;
    }

    public List<String> getFiltres(String pseudo) {
        return ((Utilisateur)(this.getCompte(pseudo))).getFiltre().getMotsInterdits();
    }

    public List<String> getPseudos(int refConv) {
        return this.getConversation(refConv).getPseudos();
    }

    public void addUserToConv(String pseudo, int refConv, MessageCallbackInterface callback) {
        Compte compte = getCompte(pseudo);
        this.getConversation(refConv).addUtilisateur(pseudo, compte.getMode());
        this.callbacks.put(pseudo, callback);
    }

    public void removeUserFromConv(String pseudo, int refConv) {
        this.getConversation(refConv).removeUtilisateur(pseudo);
        this.callbacks.remove(pseudo);
    }

    public void removeUser(String pseudo){
        this.comptes.remove(pseudo);
    }

    public void addMessage(String msg, int refConv, String pseudo) throws RemoteException {
        this.getConversation(refConv).addMessage(msg, getCompte(pseudo));
        System.out.println(getCompte(pseudo).getPseudo() + " sent : "+msg);
        InformateurMessages thread = new InformateurMessages(this, refConv, new Message(this.getCompte(pseudo), msg));
        thread.start();
        for (String mot: this.getIA().getMotsInteractionIA()) {
            if (msg.equals(mot)) {
                String msgIA = this.getIA().scannerMessagesMessage(msg, pseudo);
                addMessageInteraction(msgIA, refConv, this.ia.getPseudo());
            }
        }
    }

    public void addMessageInteraction(String msg, int refConv, String pseudo) throws RemoteException {
    /*  Utilisateur compte = (Utilisateur)this.comptes.get(pseudo);*/
      this.getConversation(refConv).addMessage(msg, getCompte(pseudo));
      InformateurMessages thread = new InformateurMessages(this, refConv, new Message(this.getCompte(pseudo), msg));
      thread.start();
    }

    public void supprimerMessage(int refConv, int index) throws RemoteException {
        this.getConversation(refConv).supprimerMessage(index);
        InformateurMessages thread = new InformateurMessages(this, refConv, null, true, index);
        thread.start();
    }

    public String ajouterMessageModeration() {
          return ("Contenu modéré");
    }
    public void setPseudo(String pseudo, String nouveauPseudo) {
        Compte c = getCompte(pseudo);
        c.setPseudo(nouveauPseudo);
        this.comptes.remove(pseudo);
        addCompte(c);
    }

    public void setAvatar(String pseudo, ImageIcon avatar) {
        getCompte(pseudo).setAvatar(avatar);
    }

    public Image getAvatar(String pseudo) {
        return getCompte(pseudo).getAvatar();
    }

    public void setMode(String pseudo, Mode mode) {
        getCompte(pseudo).setMode(mode);
    }

    public Mode getMode(String pseudo) {
        return getCompte(pseudo).getMode();
    }

    public void addCompte(Compte compte) {
        this.comptes.put(compte.getPseudo(), compte);
    }

    public void removeCompte(String pseudo) {
        this.comptes.remove(pseudo);
        this.convCallbacks.remove(pseudo);
    }

    public void addConversation(Conversation conv) {
        this.conversations.put(conv.getRefConv(), conv);
    }

    public void activerFiltre(String pseudo){
        Utilisateur u = (Utilisateur)this.getCompte(pseudo);
        u.getFiltre().activerFiltre();
    }

    public void supprimerMotInterdit(String pseudo,String mot){
        Utilisateur u = (Utilisateur)this.getCompte(pseudo);
        u.getFiltre().supprimerMotInterdit(mot);
    }

    public void updateFilter(String pseudo,String ancienMot,String mot){
        Utilisateur u = (Utilisateur)this.getCompte(pseudo);
        u.getFiltre().updateMot(ancienMot,mot);
    }

    public void desactiverFiltre(String pseudo){
        Utilisateur u = (Utilisateur)this.getCompte(pseudo);
        u.getFiltre().desactiverFiltre();
    }

    public boolean estActifFiltre(String pseudo){
        Utilisateur u = (Utilisateur)this.getCompte(pseudo);
        return u.getFiltre().estActif();
    }

    public void removeConversation(Conversation conv) {
        this.conversations.remove(conv.getRefConv());
    }

	public List<Compte> getComptes() {
		return new ArrayList<Compte>(comptes.values());
	}

	public void setComptes(List<Compte> comptes) {
        this.comptes.clear();
        for (Compte compte: comptes) {
            this.addCompte(compte);
        }
	}

    public List<Conversation> versations() {
        return new ArrayList<Conversation>(conversations.values());
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations.clear();
        for (Conversation conv: conversations) {
            this.addConversation(conv);
        }
    }

    public String getTitreConv(int refConv) {
        return this.getConversation(refConv).getTitre();
    }

    public String sayHi() {
        return "\n\n**********************       Bienvenue sur WhatsASI !       ***********************\n******************************       V 1.0      ***********************************\n";
    }

    public String contenuToString(int refConv, String pseudo) {
        StringBuilder res = new StringBuilder();
        Conversation c = this.getConversation(refConv);
        for (Message m : this.getContenu(refConv, pseudo)) {
            res.append(ANSI_CYAN+m.getPseudo()+ANSI_RESET);
            res.append(" : \n\n");
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            res.append(dateFormat.format(m.getDate())+"   "+m.getMessage());
            res.append("\n\n");
        }
        return res.toString();
    }

    public List<Message> getContenu(int refConv, String pseudo) {
        Conversation conv = this.getConversation(refConv);
        Compte compte = this.getCompte(pseudo);
        if (compte instanceof Utilisateur) {
            Filtre filtre = ((Utilisateur)compte).getFiltre();
            return conv.getContenu(filtre);
        }
        return conv.getContenu();
    }

    public void informerClients(int refConv, Message msg) throws RemoteException {
        for (String cbPseudo: this.callbacks.keySet()) {
            Utilisateur u = (Utilisateur)this.getCompte(cbPseudo);
            Filtre filtre = u.getFiltre();
            MessageCallbackInterface callback = this.callbacks.get(cbPseudo);
            if (filtre.estActif())
                callback.nouveauMessage(refConv,filtre.filtrerMessage(msg));
            else
                callback.nouveauMessage(refConv,msg);
        }
    }

    public void informerClientsNouvelleConv(int refConv) throws RemoteException {
        for (String cbPseudo: this.convCallbacks.keySet()) {
            ConversationCallbackInterface convCallback = this.convCallbacks.get(cbPseudo);
            convCallback.nouvelleConversation(refConv);
        }
    }

    public void informerClientsSuppression(int refConv, int index) throws RemoteException {
        for (String cbPseudo: this.callbacks.keySet()) {
            MessageCallbackInterface callback = this.callbacks.get(cbPseudo);
            callback.supprimerMessage(refConv, index);
        }
    }
}
