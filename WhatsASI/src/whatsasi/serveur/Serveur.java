package whatsasi.serveur;

import whatsasi.serveur.conversations.Messagerie;
import whatsasi.serveur.conversations.MessagerieInterface;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {

    private static final int portRMI = 1099;
    private static final int portSocket = 2009;

    // args = {default = JavaFX , terminal}
    public static void main(String[] args) {

        // 1 - On distribue le service Messagerie via RMI
        try {
            MessagerieInterface skeleton = (MessagerieInterface) UnicastRemoteObject.exportObject(new Messagerie(),0);
            System.out.println("Server is ready.");
            Registry registry = LocateRegistry.getRegistry(portRMI);
            System.out.println("Service Messagerie registered.");
            if (!Arrays.asList(registry.list()).contains("Messagerie"))
                registry.bind("Messagerie", skeleton);
            else
                registry.rebind("Messagerie", skeleton);
        } catch (Exception ex) {
            System.out.println("[Exception Serveur] : "+ex);
        }

        // 2 - On choisit le mode de fonctionnement (terminal - graphique)
        if (args.length > 0 && args[0].equals("terminal")){
            try{
                System.out.println("Server is now running in terminal mode.");
                ServerSocket socketServer = new ServerSocket(portSocket);
                while (true){
                    Socket socket = socketServer.accept();
                    System.out.println("A client has arrived on the Server.");
                    PrintWriter out = new PrintWriter(socket.getOutputStream());
                    out.println("Vous étes connecté !");
                    out.flush();
                }
                //socket.close();
                //socketServer.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Server is now running in JavaFX mode");
        }
    }
}
