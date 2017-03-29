package whatsasi.serveur.utilisateurs;

import whatsasi.serveur.filtrage.Filtre;
import javax.swing.ImageIcon;
import whatsasi.serveur.conversations.Mode;

public class IA extends Compte {

	public IA(ImageIcon avatar) {
		super("sophisme",avatar,Mode.valueOf("DEFAULT"));
	}

}
