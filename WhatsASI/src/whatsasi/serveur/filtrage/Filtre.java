package whatsasi.serveur.filtrage;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

public class Filtre implements Serializable {

  List<String> motsInterdits;

  public Filtre(List<String> motsInterdits){
    this.motsInterdits = new ArrayList<String>(motsInterdits);
  }

  public void setMotsInterdits(List<String> motsInterdits){
    this.motsInterdits = motsInterdits;
  }

  public List getMotsInterdits(){
    return this.motsInterdits;
  }

  public void ajouterMotInterdit(String mot){
    this.motsInterdits.add(mot);
  }

  public void supprimerMotInterdit(String mot){
    this.motsInterdits.remove(mot);
  }

}
