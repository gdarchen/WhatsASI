package whatsasi.serveur.utilisateurs;

import java.io.Serializable;
import javax.swing.ImageIcon;
import whatsasi.serveur.conversations.Mode;

public abstract class Compte implements Serializable {

  private String pseudo;
  private ImageIcon avatar;
  private Mode mode;


  public Compte(String pseudo,ImageIcon avatar,Mode mode){
        this.pseudo = pseudo;
		this.avatar = avatar;
		this.mode = mode;
  }

    public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public ImageIcon getAvatar() {
		return avatar;
	}

	public void setAvatar(ImageIcon avatar) {
		this.avatar = avatar;
	}

	public void setMode(Mode mode){
        this.mode = mode;
    }

    public Mode getMode(){
        return this.mode;
    }
}
