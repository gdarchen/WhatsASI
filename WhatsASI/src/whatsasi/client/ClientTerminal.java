package whatsasi.client;

import java.util.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
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
    private static String pseudo = null;
    private static int maxKey = 0;
    private static int refConv;

    public static void main(String[] args) {
        try{
            Registry registry = LocateRegistry.getRegistry(endPoint, portRMI);
            MessagerieInterface messagerie = (MessagerieInterface) registry.lookup("Messagerie");
            connection();
            System.out.println(messagerie.sayHi());
            enterPseudo(messagerie);
            indexActions(messagerie);
        }catch(RemoteException e) {
            e.toString();
            e.printStackTrace();
        }catch(Exception e) {
            e.toString();
            e.printStackTrace();
        }

    }

    public static void indexActions(MessagerieInterface messagerie) throws RemoteException{
        int choix = choixAccueil();
        if (choix == 1) {
            showListConv(messagerie);
            openConv(messagerie);
        }
        else
            createNewConv(messagerie);
    }

    public static void enterPseudo(MessagerieInterface messagerie) throws RemoteException {
        System.out.println("Pseudo : ");
        Scanner s = new Scanner(System.in);
        while (!messagerie.creerCompte(pseudo = s.nextLine(),null,null,null)){
            System.out.println("Pseudo deja pris !\nPseudo :");
        }
    }

    public static void createNewConv(MessagerieInterface messagerie) throws RemoteException{
        System.out.println("\n -- Nom de la nouvelle conversation : --\n");
        Scanner s = new Scanner(System.in);
        String titre;
        refConv = messagerie.creerConversation(null,pseudo,titre = s.nextLine(),null,null);
        System.out.println("*********     "+titre+ "     **********\n");
        chat(messagerie,refConv);
    }

    public static void showListConv(MessagerieInterface messagerie) throws RemoteException {
        System.out.println("Liste des conversations existantes :\n");
        Map liste = getConversationsList(messagerie);
        Iterator it = liste.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Conversation c = (Conversation)(pair.getValue());
            System.out.print(pair.getKey() + " : " + c.getTitre() + " avec ");
            for (String pseudo : c.getPseudos())
                System.out.println(pseudo + ", ");
            System.out.println("");
        }
    }

    public static Map getConversationsList(MessagerieInterface messagerie) throws RemoteException{
        Map<Integer,Conversation> liste = new HashMap<Integer,Conversation>();
        int i = 0;
        for (Conversation c : messagerie.getConversations()){
            i++;
            liste.put(i,c);
        }
        maxKey = i;
        return liste;
    }

    public static void backToMenu(MessagerieInterface messagerie) throws RemoteException{
        indexActions(messagerie);
    }

    public static int chooseConversation(MessagerieInterface messagerie) throws RemoteException {
        Scanner s = new Scanner(System.in);
        int choix = 0;
        while ((choix = s.nextInt()) < 0 || choix > maxKey ){
            System.out.println("Choix impossible");
        }
        return choix;
    }

    public static void loadMessages(MessagerieInterface messagerie,int refConv) throws RemoteException{
        System.out.println("*********     "+messagerie.getTitreConv(refConv)+ "     **********");
        System.out.println(messagerie.contenuToString(refConv));
    }

    public static void chat(MessagerieInterface messagerie,int refConv) throws RemoteException{
        Timer timer = new Timer();
        TimerTask myTask = new TimerTask() {
            @Override
            public void run() {
                try{
                    refresh(messagerie);
                }catch(RemoteException e){
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(myTask, 2000, 2000);
        Scanner s = new Scanner(System.in);
        String message = "";
        while (!(message.equals("exit"))){
            System.out.println(pseudo + " : \n");
            message = s.nextLine();
            messagerie.addMessage(message,refConv,pseudo);
            System.out.println("\n");
            refresh(messagerie);
        }
        backToMenu(messagerie);
    }

    public static void refresh(MessagerieInterface messagerie) throws RemoteException{
        System.out.print("\033[H\033[2J");
        loadMessages(messagerie,refConv);
    }

    public static void openConv(MessagerieInterface messagerie) throws RemoteException{
        refConv = chooseConversation(messagerie);
        loadMessages(messagerie,refConv);
        chat(messagerie,refConv);
    }

    public static int choixAccueil(){
        System.out.println("*******************      MENU      *****************");
        System.out.println("Vous pouvez rejoindre une conversation ou en créer une nouvelle :\n");
        System.out.println("1 - Rejoindre une conversation");
        System.out.println("2 - Créer une nouvelle conversation\n");
        int choix = 0;
        Scanner s = new Scanner(System.in);
        while (choix != 1 && choix != 2){
            choix = s.nextInt();
        }
        return choix;
    }

    public static void connection(){
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
        }
    }

}
