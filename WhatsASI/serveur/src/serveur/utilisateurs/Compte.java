package serveur.utilisateurs;

import javax.swing.ImageIcon;

public class Compte {
    private String pseudo;
    private ImageIcon avatar;
    private boolean video;
    private boolean audio;

    // ---------- Constructeurs ----------------
	public Compte() {
		super();
	}

	public Compte(String pseudo, ImageIcon avatar, boolean video, boolean audio) {
		super();
		this.pseudo = pseudo;
		this.avatar = avatar;
		this.video = video;
		this.audio = audio;
	}

    // ---------- Methodes --------------------


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

	public boolean isVideo() {
		return video;
	}

	public void setVideo(boolean video) {
		this.video = video;
	}

	public boolean isAudio() {
		return audio;
	}

	public void setAudio(boolean audio) {
		this.audio = audio;
	}
}
