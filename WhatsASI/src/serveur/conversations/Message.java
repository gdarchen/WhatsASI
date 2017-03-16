package serveur.conversations;

import serveur.utilisateurs.Compte;
import java.util.Date;

public class Message {

  Compte compte;
  Date date;
  String message;

  public Message(Compte compte, Date date, String message){
    this.compte = compte;
    this.date = date;
    this.message = message;
  }

  public void setCompte(Compte compte){
    this.compte = compte;
  }

  public Compte getCompte(){
    return this.compte;
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
