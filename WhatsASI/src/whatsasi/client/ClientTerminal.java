package whatsasi.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientTerminal{

    private static final int portRMI = 1099;
    private static final int portSocket = 2009;
    private static final String endPoint = "localhost";
    private static Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;
    private static PrintWriter outflux;

    public static void main(String[] args) {
        try {
            socket = new Socket(endPoint,portSocket);
            System.out.println("Demande de connexion...");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
