package whatsasi.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import whatsasi.serveur.conversations.MessagerieInterface;
import whatsasi.serveur.conversations.Conversation;

public class ClientTerminal{

    private static final int portRMI = 1099;
    private static final int portSocket = 2009;
    private static final String endPoint = "localhost";

    public static void main(String[] args) {
        try{
            Registry registry = LocateRegistry.getRegistry(endPoint, portRMI);
            MessagerieInterface messagerie = (MessagerieInterface) registry.lookup("Messagerie");
            conectionStep();
            System.out.println(messagerie.sayHi());
            System.out.println("Pseudo : ");
            Scanner s = new Scanner(System.in);
            while (!messagerie.creerCompte(s.next(),null,null,null)){
                System.out.println("Pseudo deja pris !\nPseudo :");
            }
            int choix = choixAccueil();
            if (choix == 1)
                showListConv(messagerie);
            else
                createNewConv(messagerie);
        }catch(Exception e) {
            System.out.println("[Exception Client] : ");
            e.printStackTrace();
        }

    }

    public static void createNewConv(MessagerieInterface messagerie) throws RemoteException{
        System.out.println("Nom de la nouvelle conversation :");
        Scanner s = new Scanner(System.in);
        messagerie.creerConversation(null,null,s.next(),null,null);
    }

    public static void showListConv(MessagerieInterface messagerie) throws RemoteException {
        System.out.println("Liste des conversations existantes :\n");
        for (Conversation c : messagerie.getConversations()){
            System.out.println(c.getTitre());
        }
    }

    public static int choixAccueil(){
        System.out.println("Vous pouvez rejoindre une conversation ou en créer une nouvelle :\n");
        System.out.println("1 - Rejoindre une conversation");
        System.out.println("2 - Créer une nouvelle conversation");
        int choix = 0;
        Scanner s = new Scanner(System.in);
        while (choix != 1 && choix != 2){
            choix = s.nextInt();
        }
        return choix;
    }

    public static void conectionStep(){
        try {
            Socket socket = new Socket(endPoint,portSocket);
            System.out.println("Demande de connexion...");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message_distant = in.readLine();
            System.out.println(message_distant);
            socket.close();
        }
        catch (UnknownHostException e) {
            System.err.println("L'adresse du serveur est fausse.");
        }
        catch (IOException e) {
            System.err.println("Le Serveur est offline, veuillez réésayez ultérieurement.");
            e.printStackTrace();
        }
    }

}
