package whatsasi.serveur.conversations;

import java.util.ArrayList;
import java.util.List;
import whatsasi.serveur.utilisateurs.Compte;

public class Messagerie {

    private List<Compte> comptes;
    private List<Conversation> conversations;

    public Messagerie(){
        this.comptes = new ArrayList<Compte>();
        this.conversations = new ArrayList<Conversation>();
    }

    public boolean creerCompte(String pseudo,ImageIcon avatar,Mode mode){
        if (isPseudoAvailable(pseudo)){
            this.addCompte(new Compte(pseudo,avatar,mode));
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
    }

    public void addMotInterdit(String mot,String pseudo){
        getCompte(pseudo).addMotInterdit(mot);
    }

    public void removeMotInterdit(String mot,String pseudo){
        getCompte(pseudo).removeMotInterdit(mot);
    }


    public boolean isPseudoAvailable(String pseudo){
        if (getCompte(pseudo) == null)
            return true;
        else
            return false;
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

}
