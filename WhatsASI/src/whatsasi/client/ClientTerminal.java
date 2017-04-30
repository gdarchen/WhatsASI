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
import whatsasi.serveur.conversations.Mode;
import whatsasi.serveur.filtrage.Filtre;

public class ClientTerminal{

    private static final int PORTRMI = 1099;
    private static final int PORTSOCKET = 2009;
    private static final String ENDPOINT = "localhost";
    private static final String BACKCHAR = "/b";
    private static String pseudo;
    private static int refConv;
    private static int maxKey = 0;

    public static void main(String[] args) {
        try{
            Registry registry = LocateRegistry.getRegistry(ENDPOINT, PORTRMI);
            MessagerieInterface messagerie = (MessagerieInterface) registry.lookup("Messagerie");
            connection();
            interceptShutdown();
            System.out.println(messagerie.sayHi());
            displayBACKCHAR();
            createAccount(messagerie);
            indexActions(messagerie);
        }catch(RemoteException e) {
            e.toString();
            e.printStackTrace();
        }catch(Exception e) {
            e.toString();
            e.printStackTrace();
        }

    }

    public static void displayBACKCHAR(){
        System.out.println("Vous pouvez retourner à la vue précédente à tout moment en saisissant  la commande '/b' .\n");
    }

    public static void createAccount(MessagerieInterface messagerie) throws RemoteException{
        System.out.println("Vous devez d'abord choisir un pseudo pour rejoindre une conversation.\n");
        pseudo = enterPseudo(messagerie);
        Filtre filtre = new Filtre(enterFiltre(messagerie));
        messagerie.creerCompte(pseudo,null,Mode.DEFAUT,filtre);
    }

    public static void manageAccount(MessagerieInterface messagerie) throws RemoteException{
        System.out.println("****************       Mon Compte       *********************");
        String tmp = pseudo;
        messagerie.modifierPseudo(tmp,pseudo = enterPseudo(messagerie));
    }

    public static String enterPseudo(MessagerieInterface messagerie) throws RemoteException {
        System.out.println("Pseudo : ");
        Scanner s = new Scanner(System.in);
        String nom;
        while (!messagerie.isPseudoAvailable(nom = s.nextLine())){
            System.out.println("\nPseudo deja pris !\nPseudo :");
        }
        return nom;
    }

    public static List<String> enterFiltre(MessagerieInterface messagerie) throws RemoteException {
        System.out.println("\nVos filtres : \n");
        System.out.println("Entrez 'ok' pour finir d'ajouter vos filtres.");
        Scanner s = new Scanner(System.in);
        List<String> filtre = new ArrayList<String>();
        String mot;
        int i = 1;
        System.out.println("Filtre "+i+" : ");
        while (!((mot = s.nextLine()).equals("ok"))){
            if (!(mot.equals("ok"))){
                filtre.add(mot);
                i++;
                System.out.println("Filtre "+i+" : ");
            }
        }
        return filtre;
    }

    public static void indexActions(MessagerieInterface messagerie) throws RemoteException{
        int choix = choixAccueil();
        switch (choix){
            case 1 : showListConv(messagerie);
            openConv(messagerie);
            break;
            case 2 : createNewConv(messagerie);
            break;
            case 3 : manageAccount(messagerie);
                     backToMenu(messagerie);
            break;
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
            int i = 0;
            for (String pseudo : c.getPseudos()){
                if (i == 0)
                    System.out.print(pseudo);
                else
                    System.out.print(", "+pseudo);
                i++;
            }
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
        while (true){
            if (s.hasNextInt()){
                if ((choix = s.nextInt()) > 0 && choix <= maxKey)
                    return choix;
                else
                    System.out.println("Choix impossible");
            }
            else{
                if (s.nextLine().equals(BACKCHAR))
                    return -666;
            }
        }
    }

    public static void loadMessages(MessagerieInterface messagerie,int refConv) throws RemoteException{
        System.out.println("*********     "+messagerie.getTitreConv(refConv)+ "     **********");
        System.out.println(messagerie.contenuToString(refConv));
    }

    public static void chat(MessagerieInterface messagerie,int refConv) throws RemoteException{
        Timer timer = new Timer();
        timer.schedule(getTask(messagerie), 2000, 2000);
        Scanner s = new Scanner(System.in);
        String message = "";
        while (!(message.equals(BACKCHAR))){
            System.out.println(pseudo + " : \n");
            message = s.nextLine();
            if (!(message.equals(BACKCHAR))) {
                messagerie.addMessage(message,refConv,pseudo);
                System.out.println("\n");
                refresh(messagerie);
            }
        }
        stopTimer(timer);
        deconectionFromConv(messagerie);
        backToMenu(messagerie);
    }

    public static void deconectionFromConv(MessagerieInterface messagerie) throws RemoteException{
        messagerie.removeUserFromConv(pseudo,refConv);
    }

    public static void stopTimer(Timer t){
        t.cancel();
        t.purge();
    }

    public static TimerTask getTask(MessagerieInterface messagerie){
        return new TimerTask() {
                @Override
                public void run() {
                    try{
                        refresh(messagerie);
                    }catch(RemoteException e){
                        e.printStackTrace();
                    }
                }
            };
    }

    public static void refresh(MessagerieInterface messagerie) throws RemoteException{
        System.out.print("\033[H\033[2J");
        loadMessages(messagerie,refConv);
    }

    public static void openConv(MessagerieInterface messagerie) throws RemoteException{
        refConv = chooseConversation(messagerie);
        if (refConv == -666 )
            backToMenu(messagerie);
        else {
            messagerie.addUserToConv(pseudo,refConv);
            loadMessages(messagerie,refConv);
            chat(messagerie,refConv);
        }
    }

    public static int choixAccueil(){
        System.out.println("*******************      MENU      *****************\n");
        System.out.println("Vous pouvez rejoindre une conversation ou en créer une nouvelle :\n");
        System.out.println("1 - Rejoindre une conversation");
        System.out.println("2 - Créer une nouvelle conversation");
        System.out.println("3 - Changer les paramètres de mon compte\n");
        int choix = 0;
        Scanner s = new Scanner(System.in);
        while (choix <= 0 || choix > 3){
            choix = s.nextInt();
        }
        return choix;
    }

    public static void interceptShutdown(){
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                logOut();
            }
        });
    }

    public static void logOut(){
        System.out.println("Deconnexion...");
        sendRequest("logout");
    }

    public static void connection(){
        System.out.println("Demande de connexion...");
        sendRequest("login");
    }

    public static void sendRequest(String msg){
        try {
            Socket socket = new Socket(ENDPOINT,PORTSOCKET);
            PrintWriter outflux = new PrintWriter(socket.getOutputStream());
            outflux.println(msg);
            outflux.flush();
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
