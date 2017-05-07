package whatsasi.serveur.utilisateurs;

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import java.lang.StringBuilder;

import whatsasi.serveur.filtrage.Filtre;
import whatsasi.serveur.conversations.Mode;
import whatsasi.serveur.conversations.Message;

import javax.swing.*;

public class IA extends Compte implements Serializable {

	List<String> motsInteractionIA = new ArrayList<String>();

	public IA(ImageIcon avatar) {
        super("Sophisme", avatar, Mode.valueOf("DEFAUT"));
		this.motsInteractionIA.add("\\bonjour");
		this.motsInteractionIA.add("\\profil");
		this.motsInteractionIA.add("\\filtre");
		this.motsInteractionIA.add("\\chat1");
		this.motsInteractionIA.add("\\chat2");
		this.motsInteractionIA.add("\\audio");
		this.motsInteractionIA.add("\\video");
	}


	public List<String> getMotsInteractionIA(){
		return this.motsInteractionIA;
	}

  /*Cette classe permet de scanner les messages*/
	public String scannerMessagesMessage(String message, String pseudo) {
         String messageIA="";
				switch(message){

				 case "\\bonjour" : messageIA = messagebonjour(pseudo);
									 								break;
				 case "\\profil" : messageIA = messageHelpProfil();
									 						break;
				 case "\\chat1" : messageIA = messageHelpChatUn();
						 										  break;
				 case "\\chat2" : messageIA = messageHelpChatDeux();
						 									break;
				 case "\\audio" : messageIA = messageHelpAudio();
													     break;
				 case "\\filtre" : messageIA = messageHelpFiltre();
															break;
				 case "\\video" : messageIA = messageHelpVideo();
															 break;
				 default : System.out.println("Problème");
       }
			 return messageIA;

	}

	/*Cette classe affiche le bon message en fonction*/
	public String messagebonjour(String pseudo){
		StringBuilder msgIA = new StringBuilder();
		msgIA.append("Bonjour "+pseudo +"\n");
		msgIA.append("Affichage du menu : \\bonjour" +"\n" );
		msgIA.append("Aide Profil : \\profil" +"\n");
		msgIA.append("Aide Video : \\video" +"\n");
		msgIA.append("Aide Audio : \\audio" +"\n");
		msgIA.append("Aide Salon de Chat : \\chat1" +"\n");
		msgIA.append("Aide Conversation : \\chat2" +"\n");
		msgIA.append("Aide Filtrel : \\filtre" +"\n");
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
		return "Pas implémenté";
	}

	public String messageHelpVideo(){
		return "Pas implémenté";
	}

	public String messageHelpFiltre(){
		StringBuilder msgIA = new StringBuilder();
		msgIA.append("Se placer dans les filtres : Selection de pane Filtre" +"\n");
		msgIA.append("  Ajouter un mot  : Selection de +" +"\n");
		msgIA.append("  Supprimer un mot  : Selection de -" +"\n");
		return msgIA.toString();
	}

}
