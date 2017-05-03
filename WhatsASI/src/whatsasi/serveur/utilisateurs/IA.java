package whatsasi.serveur.utilisateurs;

import whatsasi.serveur.filtrage.Filtre;
import javax.swing.ImageIcon;
import whatsasi.serveur.conversations.Mode;
import whatsasi.serveur.conversations.Message;
import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import java.lang.StringBuilder;
import java.lang.Object;

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
				 case "\\help_chat1" : messageIA = messageHelpChatUn();
						 										  break;
				 case "\\help_chat2" : messageIA = messageHelpChatDeux();
						 									break;
				 case "\\help_audio" : messageIA = messageHelpAudio();
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
		StringBuilder msgIA = new StringBuilder();
		msgIA.append("Bonjour "+pseudo +"\n");
		msgIA.append("Affichage du menu : \\BonjourSophisme" +"\n" );
		msgIA.append("Aide Profil : \\help_profil" +"\n");
		msgIA.append("Aide Video : \\help_video" +"\n");
		msgIA.append("Aide Audio : \\help_audio" +"\n");
		msgIA.append("Aide Salon de Chat : \\help_chat1" +"\n");
		msgIA.append("Aide Conversation : \\help_chat2" +"\n");
		msgIA.append("Aide Filtrel : \\help_filtre" +"\n");
		return msgIA.toString();
	}

	public String messageHelpProfil(){

		return "Pour modifier le filtre sélectionner filtre en haut des conversations \n Pour modifier l'image double cliquer sur la photo ";
	}


	public String messageHelpChatUn(){
		StringBuilder msgIA = new StringBuilder();
		msgIA.append("Pour créer une conversation : Selection de pane chat" +"\n");
		msgIA.append("  Signifier création  : Selection de +" +"\n");
		msgIA.append("  Ouverture d'une pop up  : Entrez nom de la conversation" +"\n");
		return msgIA.toString();
	}

	public String messageHelpChatDeux(){
		StringBuilder msgIA = new StringBuilder();
		msgIA.append("Sélection de la fonctionnalité conversation : Cliquer sur le pan chat"+"\n");
		msgIA.append("Sélection d'une conversation : Cliquer sur la conversation"+"\n");
		return msgIA.toString();
	}
	public String messageHelpAudio(){
		return "Pas implémenter";
	}

	public String messageHelpVideo(){
		return "Pas implémenter";
	}

	public String messageHelpFiltre(){
		StringBuilder msgIA = new StringBuilder();
		msgIA.append("Se placer dans les filtres : Selection de pane Filtre" +"\n");
		msgIA.append("  Ajouter un mot  : Selection de +" +"\n");
		msgIA.append("  Supprimer un mot  : Selection de -" +"\n");
		return msgIA.toString();
	}

}
