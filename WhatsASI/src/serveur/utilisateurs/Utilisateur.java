package serveur.utilisateurs;

import serveur.filtrage.Filtre;

public class Utilisateur extends Compte {
    private Filtre filtre;

	public Utilisateur(String pseudo, ImageIcon avatar, boolean video, boolean audio, Filtre filtre) {
		this.pseudo = pseudo;
		this.avatar = avatar;
		this.video = video;
		this.audio = audio;
        this.filtre = filtre;
	}

    public Filtre getFiltre(){
        return filtre;
    }

    public void setFiltre(Filtre filtre){
        this.filtre = filtre;
    }


}
