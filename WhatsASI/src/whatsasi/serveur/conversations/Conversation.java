package whatsasi.serveur.conversations;

import java.util.List;
import java.util.ArrayList;

public class Conversation {

  List<Message> messages;

  public Conversation(List<Message> messages){
    this.messages = new ArrayList<Message>(messages);
  }
}
