package whatsasi.serveur.conversations;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import whatsasi.serveur.utilisateurs.Utilisateur;
import whatsasi.serveur.filtrage.Filtre;

public class Messagerie {

    private List<Utilisateur> utilisateurs;
    private List<Conversation> conversations;

    public Messagerie(){
        this.utilisateurs = new ArrayList<Utilisateur>();
        this.conversations = new ArrayList<Conversation>();
    }

    public boolean creerUtilisateur(String pseudo,ImageIcon avatar,Mode mode,Filtre filtre){
        if (isPseudoAvailable(pseudo)){
            this.addUtilisateur(new Utilisateur(pseudo,avatar,mode,filtre));
            return true;
        }
        else
            return false;
    }

    public Utilisateur getUtilisateur(String pseudo){
        for (Utilisateur c : utilisateurs){
            if (c.getPseudo().equals(pseudo))
                return c;
        }
    }

    public void addMotInterdit(String mot,String pseudo){
        getUtilisateur(pseudo).addMotInterdit(mot);
    }

    public void removeMotInterdit(String mot,String pseudo){
        getUtilisateur(pseudo).removeMotInterdit(mot);
    }


    public boolean isPseudoAvailable(String pseudo){
        if (getUtilisateur(pseudo) == null)
            return true;
        else
            return false;
    }

    public void addUtilisateur(Utilisateur compte){
        this.utilisateurs.add(compte);
    }

    public void removeUtilisateur(String pseudo){
        this.utilisateurs.remove(getUtilisateur(pseudo));
    }

    public void addConversation(Conversation conv){
        this.conversations.add(conv);
    }

    public void removeConversation(Conversation conv){
        this.conversations.remove(conv);
    }

	public List<Utilisateur> getUtilisateurs() {
		return utilisateurs;
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
