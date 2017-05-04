package whatsasi.serveur.conversations;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.rmi.RemoteException;
import javafx.scene.image.Image;

import whatsasi.serveur.InformateurDeClients;
import whatsasi.serveur.utilisateurs.Compte;
import whatsasi.serveur.utilisateurs.Utilisateur;
import whatsasi.serveur.utilisateurs.IA;
import whatsasi.serveur.filtrage.Filtre;
import whatsasi.serveur.conversations.Mode;
import whatsasi.serveur.conversations.Message;
import whatsasi.client.MessageCallbackInterface;


public class Messagerie implements MessagerieInterface{

    private IA ia;
    private Map<String, Compte> comptes;
    private Map<Integer, Conversation> conversations;
    private Map<String, MessageCallbackInterface> callbacks;

    public Messagerie() {
        this.comptes = new HashMap<String, Compte>();
        this.conversations = new HashMap<Integer, Conversation>();
        this.callbacks = new HashMap<String, MessageCallbackInterface>();
        this.ia = new IA(null);
        this.comptes.put(ia.getPseudo(), ia);
    }

    public boolean creerCompte(String pseudo, Image avatar, Mode mode, Filtre filtre) {
        if (isPseudoAvailable(pseudo)) {
            this.addCompte(new Utilisateur(pseudo, avatar, mode, filtre));
            return true;
        }
        else
            return false;
    }

    public boolean modifierPseudo(String old, String newPseudo) {
        if (isPseudoAvailable(newPseudo)) {
            this.setPseudo(old, newPseudo);
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
        return c.getRefConv();
    }

    public Compte getCompte(String pseudo) {
        if (this.comptes.containsKey(pseudo)) {
            return this.comptes.get(pseudo);
        }
        return null;
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

    public void addMessage(String msg, int refConv, String pseudo) throws RemoteException {
        this.getConversation(refConv).addMessage(msg, getCompte(pseudo));
        System.out.println(getCompte(pseudo).getPseudo() + " sent : "+msg);
        InformateurDeClients thread = new InformateurDeClients(this, refConv, new Message(this.getCompte(pseudo), msg));
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
      InformateurDeClients thread = new InformateurDeClients(this, refConv, new Message(this.getCompte(pseudo), msg));
      thread.start();
    }

    public String supprimerMessage(String msg) throws RemoteException {
      return ajouterMessageModeration();

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

    public void setAvatar(String pseudo, Image avatar) {
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
    }

    public void addConversation(Conversation conv) {
        this.conversations.put(conv.getRefConv(), conv);
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

    public List<Conversation> getConversations() {
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
        return "\n\n**********************      Bienvenue sur WhatsASI !      ***********************\n******************************       V 1.0      ***********************************\n";
    }

    public String contenuToString(int refConv, String pseudo) {
        StringBuilder res = new StringBuilder();
        Conversation c = this.getConversation(refConv);
        for (Message m : this.getContenu(refConv, pseudo)) {
            res.append(m.getPseudo());
            res.append(" : \n");
            res.append("       "+m.getMessage());
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
        // else
            return conv.getContenu();
    }

    public void informerClients(int refConv, Message msg) throws RemoteException {
        for (String cbPseudo: this.callbacks.keySet()) {
            Utilisateur u = (Utilisateur)this.getCompte(cbPseudo);
            Filtre filtre = u.getFiltre();
            MessageCallbackInterface callback = this.callbacks.get(cbPseudo);
            callback.nouveauMessage(refConv, filtre.filtrerMessage(msg));
        }
    }

}
