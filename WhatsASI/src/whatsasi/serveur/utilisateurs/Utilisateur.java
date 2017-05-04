package whatsasi.serveur.utilisateurs;

import whatsasi.serveur.filtrage.Filtre;
import whatsasi.serveur.conversations.Mode;
import javafx.scene.image.Image;
import java.util.ArrayList;

public class Utilisateur extends Compte {

    private Filtre filtre;
    private boolean estModerateur;

  	public Utilisateur(String pseudo, Image avatar, Mode mode, Filtre filtre) {
      super(pseudo, avatar, mode);
      this.filtre = (filtre!=null) ? filtre : (new Filtre(new ArrayList<String>()));
  	}

    public Utilisateur(String pseudo, Image avatar, Mode mode, Filtre filtre, boolean tmp) {
      this(pseudo, avatar, mode, filtre);
      this.estModerateur = tmp;
  	}

    public Filtre getFiltre(){
        return this.filtre;
    }

    public void setFiltre(Filtre filtre){
        this.filtre = filtre;
    }

    public void addMotInterdit(String mot){
        this.filtre.ajouterMotInterdit(mot);
    }

    public void removeMotInterdit(String mot){
        this.filtre.supprimerMotInterdit(mot);
    }

    public boolean getEstModerateur(){
      return this.estModerateur;
    }

    public void setEstModerateur(boolean statut){
      this.estModerateur = statut;
    }

}
