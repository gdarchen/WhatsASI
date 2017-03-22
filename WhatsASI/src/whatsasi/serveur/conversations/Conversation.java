package whatsasi.serveur.conversations;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import whatsasi.serveur.utilisateurs.Utilisateur;
import whatsasi.serveur.utilisateurs.Compte;

public class Conversation {

  private List<Message> messages;
  private List<MessageDeModeration> messagesDeModeration;
  private Map<String,Mode> pipelettes;

  public Conversation(List<Message> messages,String refCompte,Mode mode,List<MessageDeModeration> messagesDeModeration){
    this.messages = new ArrayList<Message>(messages);
    this.pipelettes = new HashMap<String,Mode>();
    this.pipelettes.put(refCompte,mode);
    this.messagesDeModeration = new ArrayList<MessageDeModeration>(messagesDeModeration);
  }

  public void setMessages(List<Message> messages){
    this.messages = messages;
  }

  public List<Message> getContenu(){
    return this.messages;
  }

  public Map<String,Mode> getUtilisateurs(){
    return this.pipelettes;
  }

  public void setUtilisateurs(Map<String,Mode> pipelettes){
    this.pipelettes = pipelettes;
  }

  public void addUtilisateur(String refCompte,Mode mode){
    this.pipelettes.put(refCompte,mode);
  }

  public void setMode(String refCompte,Mode mode){
    this.pipelettes.replace(refCompte,mode);
  }

  public void addMessage(String msg, Compte compte){
    this.messages.add(new Message(compte,msg));
  }

  public void addMessageDeModeration(String msg,Compte compte){
    this.messagesDeModeration.add(new MessageDeModeration(compte,msg));
  }

  public void supprimerMessage(Message message){
    this.messages.remove(message);
  }

  public void envoyerAudio(){/* A IMPLEMENT PLUS TARD*/}

  public void envoyerVideo(){/* A IMPLEMENT PLUS TARD*/}
}
