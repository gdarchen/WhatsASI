package whatsasi.serveur.conversations;

import whatsasi.serveur.utilisateurs.Compte;
import java.util.Date;

public class Message {

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

}
