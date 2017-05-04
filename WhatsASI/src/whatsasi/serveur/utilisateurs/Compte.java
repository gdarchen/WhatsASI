package whatsasi.serveur.utilisateurs;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import javafx.scene.image.Image;
import javafx.embed.swing.SwingFXUtils;
import javax.swing.ImageIcon;

import whatsasi.serveur.conversations.Mode;

public abstract class Compte implements Serializable {
    private String pseudo;
    private ImageIcon avatar;
    private Mode mode;

    public Compte(String pseudo, Image avatar, Mode mode){
        this.pseudo = pseudo;
        this.avatar = new ImageIcon(SwingFXUtils.fromFXImage(avatar, null));
        this.mode = mode;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public Image getAvatar() {
        return SwingFXUtils.toFXImage((BufferedImage) avatar.getImage(), null);
    }

    public void setAvatar(Image avatar) {
        this.avatar = new ImageIcon(SwingFXUtils.fromFXImage(avatar, null));
    }

    public void setMode(Mode mode){
        this.mode = mode;
    }

    public Mode getMode(){
        return this.mode;
    }
}
