package whatsasi.serveur.filtrage;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import whatsasi.serveur.conversations.Message;

public class Filtre implements Serializable {

  private List<String> motsInterdits;
  private boolean actif = true;

  public Filtre(List<String> motsInterdits){
      if (motsInterdits != null) {
          this.motsInterdits = new ArrayList<String>(motsInterdits);
      }
      else {
          this.motsInterdits = new ArrayList<String>();
      }
  }

  public void activerFiltre(){
      this.actif = true;
  }

  public void desactiverFiltre(){
      this.actif = false;
  }

  public boolean estActif(){
      return this.actif;
  }

  public void updateMot(String ancien,String nouveau){
      this.motsInterdits.set(this.motsInterdits.indexOf(ancien),nouveau);
  }

  public void setMotsInterdits(List<String> motsInterdits){
    this.motsInterdits = motsInterdits;
  }

  public List<String> getMotsInterdits(){
    return this.motsInterdits;
  }

  public void ajouterMotInterdit(String mot){
    this.motsInterdits.add(mot);
  }

  public void supprimerMotInterdit(String mot){
    this.motsInterdits.remove(mot);
  }

  public List<Message> filtrerMessages(List<Message> messages) {
      List<Message> messagesFiltres = new ArrayList<Message>();
      for (Message message: messages) {
          messagesFiltres.add(filtrerMessage(message));
      }
      return messagesFiltres;
  }

public Message filtrerMessage(Message message) {
    Message messageFiltre = new Message(message);
    String msg, msglow, stars;
    int begin, end, l;
    msg = message.getMessage();
    for (String mot: this.getMotsInterdits()) {
        l = mot.length();
        stars = new String(new char[l]).replace("\0", "*");
        msglow = msg.toLowerCase();
        while (msglow.contains(mot)) {
            begin = msglow.indexOf(mot);
            end = begin + l;
            msg = msg.substring(0, begin) + stars + msg.substring(end);
            msglow = msg.toLowerCase();
        }
    }
    messageFiltre.setMessage(msg);
    return messageFiltre;
}

}
