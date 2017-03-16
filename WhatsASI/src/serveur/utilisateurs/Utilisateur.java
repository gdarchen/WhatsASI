package serveur.utilisateurs;

import serveur.filtrage.Filtre;
import javax.swing.ImageIcon;

public class Utilisateur extends Compte {

  private Filtre filtre;

	public Utilisateur(String pseudo, ImageIcon avatar, boolean video, boolean audio, Filtre filtre) {
    super(pseudo,avatar,video,audio);
    this.filtre = filtre;
	}

    public Filtre getFiltre(){
        return this.filtre;
    }

    public void setFiltre(Filtre filtre){
        this.filtre = filtre;
    }


}
