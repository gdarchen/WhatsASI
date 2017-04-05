package whatsasi.serveur.conversations;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import whatsasi.serveur.utilisateurs.Compte;
import whatsasi.serveur.utilisateurs.Utilisateur;
import whatsasi.serveur.filtrage.Filtre;
import whatsasi.serveur.conversations.Mode;

public class Messagerie implements MessagerieInterface {

    private List<Compte> comptes;
    private List<Conversation> conversations;

<<<<<<< HEAD
    public Messagerie(){
        this.comptes = new ArrayList<Compte>();
=======
    public Messagerie() {
        this.utilisateurs = new ArrayList<Utilisateur>();
>>>>>>> 56e3b251c9a153955623db10d8889d8902ed6ce3
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

    public Conversation getConversation(Conversation conv){
        for (Conversation c : conversations){
            if (c.getRefConv() == conv.getRefConv())
                return c ;
        }
        return null;
    }

    public List<Message> getContenu(Conversation conv){
        if (getConversation(conv) != null)
            return getConversation(conv).getContenu();
        return null;
    }

    public void addMessage(String msg, Conversation conv, String pseudo){
        if (getConversation(conv) != null){
            getConversation(conv).addMessage(msg, getCompte(pseudo));
        }
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

    public void setUtilisateurs(List<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    public List<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }
}
