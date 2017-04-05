package whatsasi.serveur.utilisateurs;

import whatsasi.serveur.filtrage.Filtre;
import whatsasi.serveur.conversations.Mode;
import javax.swing.ImageIcon;

public class Utilisateur extends Compte {

    private Filtre filtre;

  	public Utilisateur(String pseudo, ImageIcon avatar, Mode mode, Filtre filtre) {
      super(pseudo,avatar,mode);
      this.filtre = filtre;
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


}