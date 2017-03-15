package serveur.conversations;

import serveur.utilisateurs.Compte;

public class Message{

  Compte compte;
  Date date;
  String message;

  public Message(Compte compte, Date date, String message){
    this.compte = compte;
    this.date = date;
    this.message = message;
  }

  public setCompte(Compte compte){
    this.compte = compte;
  }

  public Compte getCompte(){
    return this.comtpe;
  }

  public setDate(Date date){
    this.date = date;
  }

  public Date getDate(){
    return this.date;
  }

  public setMessage(Message msg){
    this.message = msg;
  }

  public String getMessage(){
    return this.message;
  }

}
