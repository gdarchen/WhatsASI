package whatsasi.serveur.utilisateurs;

import java.awt.*;
import java.io.Serializable;

import whatsasi.serveur.conversations.Mode;

import javax.swing.*;

public abstract class Compte implements Serializable {
    private String pseudo;
    private ImageIcon avatar;
    private int avw, avh;
    private Mode mode;

    public Compte(String pseudo, ImageIcon avatar, Mode mode){
        this.pseudo = pseudo;
        setAvatar(avatar);
        this.mode = mode;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public Image getAvatar() {
        return avatar.getImage();
    }

    public void setAvatar(ImageIcon avatar) {
        this.avatar = avatar;
        if (avatar != null){
            setAvatarWidth(avatar.getIconWidth());
            setAvatarWidth(avatar.getIconHeight());
        }
    }

    public void setAvatarWidth(int w) {
        avw = w;
    }

    public void setAvatarHeight(int h) {
        avh = h;
    }

    public int getAvatarHeight() {
        return avw;
    }

    public int getAvatarWidth() {
        return avw;
    }

    public void setMode(Mode mode){
        this.mode = mode;
    }

    public Mode getMode(){
        return this.mode;
    }
}
