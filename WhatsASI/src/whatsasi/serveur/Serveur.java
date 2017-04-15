package whatsasi.serveur;

import whatsasi.serveur.conversations.Messagerie;
//import whatsasi.serveur.conversations.MessagerieInterface;
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
    private static Registry registry;
    private static ServerSocket socketServer;
    private static Socket socket;

    public static void main(String[] args) {
        // args = {JavaFX , terminal}
        if (args[0].equals("terminal")){
            try{
                System.out.println("Server is now running in terminal mode.");
                socketServer = new ServerSocket(portSocket);
                while (true){
                    socket = socketServer.accept();
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
            /*
            try {
                MessagerieInterface skeleton = (MessagerieInterface) UnicastRemoteObject.exportObject(new Messagerie(),0);
                System.out.println("Server is ready.");
                registry = LocateRegistry.getRegistry (port);
                System.out.println("Service Messagerie enregistré");
                if (!Arrays.asList(registry.list()).contains("Messagerie"))
                    registry.bind("Messagerie", skeleton);
                else
                    registry.rebind ("Messagerie", skeleton);
            } catch (Exception ex) {
                ex.printStackTrace ();
            }*/
        }
    }
}
