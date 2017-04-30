package whatsasi.serveur.conversations;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import whatsasi.serveur.utilisateurs.Compte;
import whatsasi.serveur.utilisateurs.Utilisateur;
import whatsasi.serveur.filtrage.Filtre;
import whatsasi.serveur.conversations.Mode;

public class Messagerie implements MessagerieInterface{

    private List<Compte> comptes;
    private List<Conversation> conversations;

    public Messagerie() {
        this.comptes = new ArrayList<Compte>();
        this.conversations = new ArrayList<Conversation>();
    }

    public boolean creerCompte(String pseudo,ImageIcon avatar,Mode mode,Filtre filtre){
        if (isPseudoAvailable(pseudo)){
            this.addCompte(new Utilisateur(pseudo,avatar,mode,filtre));
            return true;
        }
        else
            return false;
    }

    public int creerConversation(List<Message> messages,String pseudo,String titre,Mode mode,List<MessageDeModeration> messagesDeModeration){
        Conversation c = new Conversation(messages,pseudo,titre,mode,messagesDeModeration);
        this.conversations.add(c);
        return c.getRefConv();
    }

    public Compte getCompte(String pseudo){
        for (Compte c : comptes){
            if (c.getPseudo().equals(pseudo))
                return c;
        }
        return null;
    }

    public void addMotInterdit(String mot,String pseudo){
        Compte c = getCompte(pseudo);
        if (c instanceof Utilisateur)
            ((Utilisateur)c).addMotInterdit(mot);
    }

    public void removeMotInterdit(String mot,String pseudo){
        Compte c = getCompte(pseudo);
        if (c instanceof Utilisateur)
            ((Utilisateur)c).removeMotInterdit(mot);
    }

    public boolean isPseudoAvailable(String pseudo){
        if (getCompte(pseudo) == null)
            return true;
        else
            return false;
    }

    public Conversation getConversation(int refConv){
        for (Conversation c : conversations){
            if (c.getRefConv() == refConv)
                return c ;
        }
        return null;
    }

    public List<Message> getContenu(int refConv){
        if (getConversation(refConv) != null)
            return getConversation(refConv).getContenu();
        return null;
    }

    public void addMessage(String msg,int refConv, String pseudo){
        this.getConversation(refConv).addMessage(msg, getCompte(pseudo));
        System.out.println(getCompte(pseudo).getPseudo() + " sent : "+msg);
    }

    public void setPseudo(String pseudo, String nouveauPseudo){
        getCompte(pseudo).setPseudo(nouveauPseudo);
    }

    public void setAvatar(String pseudo, ImageIcon avatar){
        getCompte(pseudo).setAvatar(avatar);
    }

    public ImageIcon getAvatar(String pseudo){
        return getCompte(pseudo).getAvatar();
    }

    public void setMode(String pseudo, Mode mode){
        getCompte(pseudo).setMode(mode);
    }

    public Mode getMode(String pseudo){
        return getCompte(pseudo).getMode();
    }

    public void addCompte(Compte compte){
        this.comptes.add(compte);
    }

    public void removeCompte(String pseudo){
        this.comptes.remove(getCompte(pseudo));
    }

    public void addConversation(Conversation conv){
        this.conversations.add(conv);
    }

    public void removeConversation(Conversation conv){
        this.conversations.remove(conv);
    }

	public List<Compte> getComptes() {
		return comptes;
	}

	public void setComptes(List<Compte> comptes) {
		this.comptes = comptes;
	}

    public List<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    public String getTitreConv(int refConv){
        return this.getConversation(refConv).getTitre();
    }

    public String sayHi(){
        return "\n\n******************   Bienvenue sur WhatsASI ! *****************\nVous devez d'abord choisir un pseudo pour rejoindre une conversation.";
    }

    public String contenuToString(int refConv){
        StringBuilder res = new StringBuilder();
        Conversation c = this.getConversation(refConv);
        for (Message m : c.getContenu()){
            res.append(m.getPseudo());
            res.append(" : \n");
            res.append("       "+m.getMessage());
            res.append("\n\n");
        }
        return res.toString();
    }

}
