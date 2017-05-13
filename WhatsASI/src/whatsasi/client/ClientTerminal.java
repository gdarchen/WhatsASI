package whatsasi.client;

import java.util.*;
import java.io.*;
import java.text.*;
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

public class ClientTerminal {

    private static final int PORTRMI = 1099;
    private static final int PORTSOCKET = 2009;
    private static final String ENDPOINT = "localhost";
    private static final String BACKCHAR = "/b";
    private static String pseudo;
    private static int refConv = -1;
    private static int maxKey = 0;
    private static MessageCallbackInterface callback = null;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void main(String[] args) {
        try{
            Registry registry = LocateRegistry.getRegistry(ENDPOINT, PORTRMI);
            MessagerieInterface messagerie = (MessagerieInterface) registry.lookup("Messagerie");
            refresh();
            connection();
            interceptShutdown(messagerie);
            System.out.println(ANSI_GREEN+messagerie.sayHi()+ANSI_RESET);
            displayBACKCHAR();
            createAccount(messagerie);
            indexActions(messagerie);
        }catch(RemoteException e) {
            //e.toString();
            //e.printStackTrace();
        }catch(Exception e) {
            //e.toString();
            //e.printStackTrace();
        }

    }

    public static void displayBACKCHAR(){
        System.out.println(ANSI_BLUE+"Vous pouvez retourner à la vue précédente à tout moment en saisissant la commande '/b' .\n"+ANSI_RESET);
    }

    public static void createAccount(MessagerieInterface messagerie) throws RemoteException{
        System.out.println("Vous devez d'abord choisir un pseudo pour rejoindre une conversation.\n");
        pseudo = enterPseudo(messagerie);
        Filtre filtre = new Filtre(enterFiltre(messagerie));
        messagerie.creerCompte(pseudo,null,Mode.DEFAUT,filtre);
    }

    public static void manageAccount(MessagerieInterface messagerie) throws RemoteException{
        refresh();
        System.out.println(ANSI_GREEN+"****************       Mon Compte       *********************"+ANSI_RESET);
        switch(selectChoicAccount()){
            case 1: updatePseudo(messagerie);break;
            case 2: updateFilters(messagerie);break;
            case 3: backToMenu(messagerie);break;
        }
    }

    public static void updatePseudo(MessagerieInterface messagerie) throws RemoteException{
        refresh();
        String tmp = pseudo;
        messagerie.modifierPseudo(tmp,pseudo = enterPseudo(messagerie));
        System.out.println("\n");
        manageAccount(messagerie);
    }

    public static int selectChoicAccount(){
        System.out.println(ANSI_BLUE+"\n*** Que voulez-vous modifier ? \n"+ANSI_RESET);
        System.out.println(" 1 - Pseudo");
        System.out.println(" 2 - Filtres");
        System.out.println(" 3 - Retour au menu");
        Scanner s = new Scanner(System.in);
        int choix;
        while ((choix = s.nextInt()) <= 0 || choix > 3)
            System.out.println(ANSI_RED+" Choix non valide"+ANSI_RESET);
        return choix;
    }

    public static void updateFilters(MessagerieInterface messagerie) throws RemoteException {
        refresh();
        System.out.println(ANSI_GREEN+"************    Mes filtres    ************\n\n"+ANSI_RESET);
        int choix = getFilterChoice(messagerie);
        switch (choix) {
            case 1 : messagerie.desactiverFiltre(pseudo);
                     System.out.println("\n Filtres désactivés !");
                     manageAccount(messagerie);
                     break;
            case 2 : manageFilters(messagerie);
                     break;
            case 3 : manageAccount(messagerie);break;
        }
    }

    public static void manageFilters(MessagerieInterface messagerie) throws RemoteException{
        System.out.println(ANSI_BLUE+"Filtres actuels : \n"+ANSI_RESET);
        if (messagerie.getFiltres(pseudo).size() < 1 ){
            System.out.println("Vous n'avez aucun filtre. Appuyez sur n'import quelle touche pour revenir en arriere.");
            Scanner s = new Scanner(System.in);
            s.nextLine();
            manageAccount(messagerie);
        }
        else {
            int i = 0;
            int maxFilter;
            Map<Integer,String> listeFiltres = new HashMap<Integer,String>();
            for (String filtre : messagerie.getFiltres(pseudo)){
                i++;
                System.out.println(i + " : "+filtre);
                listeFiltres.put(i,filtre);
            }
            maxFilter = i;
            int choix;
            Scanner s = new Scanner(System.in);
            while ((choix = s.nextInt()) <= 0 && choix > maxFilter)
                System.out.println(ANSI_RED+" Choix non valide."+ANSI_RESET);
            updateFilter(listeFiltres.get(choix),messagerie);
        }
    }

    public static void updateFilter(String filtre,MessagerieInterface messagerie) throws RemoteException{
        System.out.println(ANSI_CYAN+"filtre "+ filtre + " : \n"+ANSI_RESET);
        System.out.println(" 1 - supprimer");
        System.out.println(" 2 - modifier");
        System.out.println(" 3 - retour\n");
        Scanner s = new Scanner(System.in);
        int choix;
        while ((choix = s.nextInt()) <= 0 || choix > 3)
            System.out.println(ANSI_RED+"Choix non valide."+ANSI_RESET);
        switch(choix){
            case 1 : messagerie.supprimerMotInterdit(pseudo,filtre);
                     System.out.println("Filtre "+filtre + " deséactivé !");
                     break;
            case 2 : modifyFilter(messagerie,filtre);break;
            case 3 : updateFilters(messagerie);break;
        }
        updateFilters(messagerie);
    }

    public static void modifyFilter(MessagerieInterface messagerie,String filtre) throws RemoteException{
        System.out.println(ANSI_BLUE+"\nModifier le filtre "+filtre + " en : \n"+ANSI_RESET);
        Scanner s = new Scanner(System.in);
        messagerie.updateFilter(pseudo,filtre,s.nextLine());
        System.out.println(" Filtre modifié !");
    }

    public static int getFilterChoice(MessagerieInterface messagerie) throws RemoteException{
        if (messagerie.estActifFiltre(pseudo))
            System.out.println(" 1 - Désactiver les filtres");
        else
            System.out.println(" 1 - Activer les filtres");
        System.out.println(" 2 - Modifier les filtres");
        System.out.println(" 3 - Retour\n");
        Scanner s = new Scanner(System.in);
        int choix;
        while((choix = s.nextInt()) <= 0 || choix > 3)
            System.out.println(ANSI_RED+"Choix non valide"+ANSI_RESET);
        return choix;
    }

    public static String enterPseudo(MessagerieInterface messagerie) throws RemoteException {
        System.out.println(ANSI_BLUE+"Pseudo : "+ANSI_RESET);
        Scanner s = new Scanner(System.in);
        String nom;
        while (!messagerie.isPseudoAvailable(nom = s.nextLine())){
            System.out.println(ANSI_RED+"\nPseudo deja pris !\n"+ANSI_RESET + ANSI_BLUE+"Pseudo :"+ANSI_RESET);
        }
        return nom;
    }

    public static List<String> enterFiltre(MessagerieInterface messagerie) throws RemoteException {
        System.out.println(ANSI_BLUE+"\nVos filtres : \n"+ANSI_RESET);
        System.out.println("Entrez 'ok' pour finir d'ajouter vos filtres.");
        Scanner s = new Scanner(System.in);
        List<String> filtre = new ArrayList<String>();
        String mot;
        int i = 1;
        System.out.println(" Filtre "+i+" : ");
        while (!((mot = s.nextLine()).equals("ok"))){
            if (!(mot.equals("ok"))){
                filtre.add(mot);
                i++;
                System.out.println(" Filtre "+i+" : ");
            }
        }
        return filtre;
    }

    public static void indexActions(MessagerieInterface messagerie) throws RemoteException{
        refresh();
        int choix = choixAccueil();
        switch (choix){
            case 1 : showListConv(messagerie);
            openConv(messagerie);
            break;
            case 2 : createNewConv(messagerie);
            break;
            case 3 : manageAccount(messagerie);
            break;
        }
    }

    public static void createNewConv(MessagerieInterface messagerie) throws RemoteException{
        refresh();
        System.out.println(ANSI_BLUE+"\n -- Nom de la nouvelle conversation : --\n"+ANSI_RESET);
        Scanner s = new Scanner(System.in);
        String titre;
        callback = new TerminalMessageCallback(pseudo);
        refConv = messagerie.creerConversation(null,pseudo,titre = s.nextLine(),null,null, callback);
        System.out.println(ANSI_GREEN+"*********     "+titre+ "     **********\n"+ANSI_RESET);
        chat(messagerie,refConv);
    }

    public static void showListConv(MessagerieInterface messagerie) throws RemoteException {
        refresh();
        System.out.println(ANSI_GREEN+"Liste des conversations existantes :\n"+ANSI_RESET);
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
                    System.out.println(ANSI_RED+" Choix impossible"+ANSI_RESET);
            }
            else{
                if (s.nextLine().equals(BACKCHAR))
                    return -666;
            }
        }
    }

    public static void loadMessages(MessagerieInterface messagerie,int refConv) throws RemoteException{
        System.out.println(ANSI_GREEN+"*********     "+messagerie.getTitreConv(refConv)+ "     **********"+ANSI_RESET);
        System.out.println(messagerie.contenuToString(refConv, pseudo));
    }

    public static void chat(MessagerieInterface messagerie,int refConv) throws RemoteException{
        Scanner s = new Scanner(System.in);
        String message = "";
        while (!(message.equals(BACKCHAR))){
            message = s.nextLine();
            for (int i=0;i<message.length();i++)
                System.out.print("\b");
            System.out.println(ANSI_CYAN+pseudo + " : \n"+ANSI_RESET);
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            System.out.print(dateFormat.format(new Date())+"   "+message);
            System.out.println("");
            if (!(message.equals(BACKCHAR))) {
                messagerie.addMessage(message,refConv,pseudo);
                System.out.println("\n");
            }
        }
        deconectionFromConv(messagerie);
        backToMenu(messagerie);
    }

    public static void deconectionFromConv(MessagerieInterface messagerie) throws RemoteException{
        messagerie.removeUserFromConv(pseudo,refConv);
    }

    public static void refresh(){
        System.out.print("\033[H\033[2J");
    }

    public static void openConv(MessagerieInterface messagerie) throws RemoteException{
        refConv = chooseConversation(messagerie);
        if (refConv == -666 )
            backToMenu(messagerie);
        else {
            callback = new TerminalMessageCallback(refConv, pseudo);
            messagerie.addUserToConv(pseudo,refConv,callback);
            loadMessages(messagerie,refConv);
            chat(messagerie,refConv);
        }
    }

    public static int choixAccueil(){
        System.out.println(ANSI_GREEN+"*******************      MENU      *****************\n"+ANSI_RESET);
        System.out.println(ANSI_CYAN+"Vous pouvez rejoindre une conversation ou en créer une nouvelle :\n"+ANSI_RESET);
        System.out.println(" 1 - Rejoindre une conversation");
        System.out.println(" 2 - Créer une nouvelle conversation");
        System.out.println(" 3 - Changer les paramètres de mon compte\n");
        int choix = 0;
        Scanner s = new Scanner(System.in);
        while (choix <= 0 || choix > 3){
            choix = s.nextInt();
        }
        return choix;
    }

    public static void interceptShutdown(MessagerieInterface messagerie) throws RemoteException{
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run(){
                try{
                    logOut(messagerie);
                }catch (RemoteException e) {
                    //e.printStackTrace();
                }
            }
        });
    }

    public static void logOut(MessagerieInterface messagerie) throws RemoteException{
        System.out.println(ANSI_GREEN+"Deconnexion..."+ANSI_RESET);
        if (pseudo != null && refConv != -1)
            messagerie.removeUserFromConv(pseudo,refConv);
        sendRequest("logout");
    }

    public static void connection(){
        System.out.println(ANSI_GREEN+"Demande de connexion..."+ANSI_RESET);
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
            System.err.println(ANSI_RED+"L'adresse du serveur est fausse."+ANSI_RESET);
        }
        catch (IOException e) {
            System.err.println(ANSI_RED+"Le Serveur est offline, veuillez réésayez ultérieurement."+ANSI_RESET);
        }
    }

}
