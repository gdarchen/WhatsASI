package whatsasi.serveur.utilisateurs;

import whatsasi.serveur.filtrage.Filtre;
import javax.swing.ImageIcon;
import whatsasi.serveur.conversations.Mode;
import whatsasi.serveur.conversations.Message;
import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;

public class IA extends Compte implements Serializable {

	List<String> motsInteractionIA = new ArrayList<String>();

	public IA(ImageIcon avatar) {
		super("sophisme",avatar,Mode.valueOf("DEFAUT"));
		this.motsInteractionIA.add("\\BonjourSophisme");
		this.motsInteractionIA.add("\\help_profil");
		this.motsInteractionIA.add("\\help_filtre");
		this.motsInteractionIA.add("\\help_chat1");
		this.motsInteractionIA.add("\\help_chat2");
		this.motsInteractionIA.add("\\help_audio");
		this.motsInteractionIA.add("\\help_video");
	}


	public List<String> getMotsInteractionIA(){
		return this.motsInteractionIA;
	}

  /*Cette classe permet de scanner les messages*/
	public String scannerMessagesMessage(String message, String pseudo) {
         String messageIA="";
				switch(message){

				 case "\\BonjourSophisme" : messageIA = messageBonjourSophisme(pseudo);
									 								break;
				 case "\\help_profil" : messageIA = messageHelpProfil();
									 						break;
				 case "\\help_chat1" : messageIA = messagehelpChatUn();
						 										  break;
				 case "\\help_chat2" : messageIA = messageHelpChatDeux();
						 									break;
				 case "\\help_audiol" : messageIA = messageHelpAudio();
													     break;
				 case "\\help_filtre" : messageIA = messageHelpFiltre();
															break;
				 case "\\help_video" : messageIA = messageHelpVideo();
															 break;
				 default : System.out.println("Problème");
       }
			 return messageIA;

	}

	/*Cette classe affiche le bon message en fonction*/
	public String messageBonjourSophisme(String pseudo){
		return "Bonjour "+pseudo;
	}

	public String messageHelpProfil(){
		return "Bonjour";
	}


	public String messagehelpChatUn(){
		return "Bonjour";
	}

	public String messageHelpChatDeux(){
		return "Bonjour";
	}
	public String messageHelpAudio(){
		return "Bonjour";
	}

	public String messageHelpVideo(){
		return "Bonjour";
	}

	public String messageHelpFiltre(){
		return "Bonjour";
	}

}
