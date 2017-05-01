package whatsasi.serveur.utilisateurs;

import whatsasi.serveur.filtrage.Filtre;
import javax.swing.ImageIcon;
import whatsasi.serveur.conversations.Mode;
import whatsasi.serveur.conversations.Message;
import java.util.List;
import java.util.ArrayList;

public class IA extends Compte {

	List<String> motsInteractionIA = new ArrayList();

	public IA(ImageIcon avatar) {
		super("sophisme",avatar,Mode.valueOf("DEFAULT"));
		this.motsInteractionIA.add("BonjourSophisme");
		this.motsInteractionIA.add("help_profil");
		this.motsInteractionIA.add("help_filtre");
		this.motsInteractionIA.add("help_chat1");
		this.motsInteractionIA.add("help_chat2");
		this.motsInteractionIA.add("help_audio");
		this.motsInteractionIA.add("help_video");
	}

  public IA(){
		this(new ImageIcon());
	}


	public List<String> getMotsInteractionIA(){
		return this.motsInteractionIA;
	}

  /*Cette classe permet de scanner les messages*/
	public String scannerMessagesMessage(String message, String pseudo) {
         String messageIA="";
				switch(message){

				 case "BonjourSophisme" : messageIA = messageBonjourSophisme(pseudo);
									 								break;
				 case "help_profil" : messageIA = messageHelpProfil();
									 						break;
				 case "help_chat1" : messageIA = messagehelpChatUn();
						 										  break;
				 case "help_chat2" : messageIA = messageHelpChatDeux();
						 									break;
				 case "help_audiol" : messageIA = messageHelpAudio();
													     break;
				 case "help_filtre" : messageIA = messageHelpFiltre();
															break;
				 case "help_video" : messageIA = messageHelpVideo();
															 break;
				 default : System.out.println("Problème");
       }
			 return messageIA;

	}

	/*Cette classe affiche le bon message en fonction*/
	public String messageBonjourSophisme(String pseudo){
        String messageMessageIA = pseudo;
	       return messageMessageIA;
			 }

	public String messageHelpProfil(){
		String messageMessageIA = "Bonjour";
		 return messageMessageIA;
					 }


	public String messagehelpChatUn(){
		String messageMessageIA = "Bonjour";
		 return messageMessageIA;
				 }

  public String messageHelpChatDeux(){
		String messageMessageIA = "Bonjour";
		return messageMessageIA;
				 }
	public String messageHelpAudio(){
		String messageMessageIA = "Bonjour";
		 return messageMessageIA;
		 		}

	public String messageHelpVideo(){
		String messageMessageIA = "Bonjour";
		return messageMessageIA;
					}

	public String messageHelpFiltre(){
		String messageMessageIA = "Bonjour";
		 return messageMessageIA;
					}

}
