package serveur.utilisateurs;

import serveur.filtrage.Filtre;

public class IA extends Compte {

	public IA(ImageIcon avatar) {
		this.pseudo = "sophisme";
		this.avatar = avatar;
		this.video = FALSE;
		this.audio = FALSE;
	}
}
