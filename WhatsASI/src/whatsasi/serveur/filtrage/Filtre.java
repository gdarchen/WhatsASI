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
      for (String mot: this.getMotsInterdits()) {
          if ((message.getMessage()).contains(mot)) {
              messageFiltre.setMessage("Message filtré automatiquement");
              return messageFiltre;
          }
      }
      return messageFiltre;
  }

}
