package whatsasi.serveur.conversations;

import java.util.*;
import java.io.Serializable;
import whatsasi.serveur.utilisateurs.Utilisateur;
import whatsasi.serveur.utilisateurs.Compte;
import whatsasi.serveur.filtrage.Filtre;

public class Conversation implements Serializable{

  private List<Message> messages;
  private List<MessageDeModeration> messagesDeModeration;
  private Map<String,Mode> pipelettes; /*Lien entre utilisateur identifié via
                                        pseudo et mode choisi pour sa conversation*/
  private static int refConv; /*Des que l'on en crée une cela incrémente*/
  private String titre;

  public Conversation(List<Message> messages,String pseudo,String titre,Mode mode,List<MessageDeModeration> messagesDeModeration){
    if (messages != null )
        this.messages = new ArrayList<Message>(messages);
    else
        this.messages = new ArrayList<Message>();
    this.pipelettes = new HashMap<String,Mode>();
    this.pipelettes.put(pseudo,mode);
    if (messagesDeModeration != null)
        this.messagesDeModeration = new ArrayList<MessageDeModeration>(messagesDeModeration);
    else
        this.messagesDeModeration = new ArrayList<MessageDeModeration>();
    this.refConv++;
    this.titre = titre;
  }

  public String getTitre(){
      return this.titre;
  }

  public void setTitre(String titre){
      this.titre = titre;
  }

  public int getRefConv(){
      return this.refConv;
  }

  public void setMessages(List<Message> messages){
      this.messages = messages;
  }

  public List<Message> getContenu(){
      return this.messages;
  }

  public List<Message> getContenu(Filtre filtre){
    return filtre.filtrerMessages(this.messages);
  }

  public Map<String,Mode> getUtilisateurs(){
      return this.pipelettes;
  }

  public List<String> getPseudos(){
      List<String> liste = new ArrayList<String>();
      Iterator it = this.pipelettes.entrySet().iterator();
      while (it.hasNext()) {
          Map.Entry pair = (Map.Entry)it.next();
          liste.add((String)(pair.getKey()));
      }
      return liste;
  }

  public void setUtilisateurs(Map<String,Mode> pipelettes){
      this.pipelettes = pipelettes;
  }

  public void addUtilisateur(String pseudo,Mode mode){
      this.pipelettes.put(pseudo,mode);
  }

  public void removeUtilisateur(String pseudo){
      this.pipelettes.remove(pseudo);
  }

  public void setMode(String pseudo,Mode mode){
      this.pipelettes.replace(pseudo,mode);
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

  public void supprimerMessage(int index) {
    this.messages.remove(index);
  }

  public void envoyerAudio(){/* A IMPLEMENT PLUS TARD*/}

  public void envoyerVideo(){/* A IMPLEMENT PLUS TARD*/}
}
