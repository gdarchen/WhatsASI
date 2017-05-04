package whatsasi.serveur.conversations;

import java.awt.*;
import java.io.Serializable;
import whatsasi.serveur.utilisateurs.Compte;

import javax.swing.*;
import java.util.Date;

public class Message implements Serializable{

    private Compte compte;
    private Date date;
    private String message;

    public Message(Compte compte,String message){
        this.compte = compte;
        this.date = new Date();
        this.message = message;
    }

    public void setCompte(Compte compte){
        this.compte = compte;
    }

    public Compte getCompte(){
        return this.compte;
    }

    public String getPseudo(){
        return this.compte.getPseudo();
    }

    public void setDate(Date date){
        this.date = date;
    }

    public Date getDate(){
        return this.date;
    }

    public void setMessage(String msg){
        this.message = msg;
    }

    public String getMessage(){
        return this.message;
    }

    public Image getAvatar() {
        return compte.getAvatar();
    }

    public int getAvatarWidth() {
        return compte.getAvatarWidth();
    }

    public int getAvatarHeight() {
        return compte.getAvatarHeight();
    }

    public void setAvatar(ImageIcon avatar) {
        compte.setAvatar(avatar);
        compte.setAvatarWidth(avatar.getIconWidth());
        compte.setAvatarHeight(avatar.getIconHeight());
    }
}
