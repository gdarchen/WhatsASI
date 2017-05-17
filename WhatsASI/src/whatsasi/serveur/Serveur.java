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
import java.rmi.server.*;
import java.net.InetAddress;

public class Serveur {
    private static final int portRMI = 1099;
    private static final int portSocket = 2009;

    public static void main(String[] args) {
        try {
            String hostname = InetAddress.getLocalHost().getHostAddress();
            System.setProperty("java.rmi.server.hostname",hostname);
            //System.out.println(hostname);
            MessagerieInterface skeleton = (MessagerieInterface) UnicastRemoteObject.exportObject(new Messagerie(),0);
            System.out.println("Server is now online.");
            Registry registry = LocateRegistry.getRegistry(portRMI);
            System.out.println("Service Messagerie registered.");
            if (!Arrays.asList(registry.list()).contains("Messagerie"))
                registry.bind("Messagerie", skeleton);
            else
                registry.rebind("Messagerie", skeleton);
        } catch (Exception ex) {
            System.out.println("\nException Serveur\n-----------------");
            ex.printStackTrace();
        }
        try{
            ServerSocket socketServer = new ServerSocket(portSocket);
            while (true){
                Socket socket = socketServer.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String msg = in.readLine();
                String answer = "";
                switch (msg){
                    case "login" : System.out.println("A client has arrived on the server.");
                                   answer = "Vous étes connecté !";
                                   break;
                    case "logout" : System.out.println("A client just left the server.");
                                   answer = "Vous étes déconnecté !";
                                   break;
                    default :      answer = "error";
                                   break;
                }
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                out.println(answer);
                out.flush();
            }
            //socket.close();
            //socketServer.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
