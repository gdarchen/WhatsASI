package whatsasi.serveur.conversations;

import whatsasi.serveur.utilisateurs.Compte;
import java.util.Date;

public class MessageDeModeration extends Message{

  public MessageDeModeration(Compte compte, Date date, String message){
    super(compte,date,message);
  }

}
