package whatsasi.serveur;

import whatsasi.serveur.conversations.Messagerie;
import whatsasi.serveur.conversations.MessagerieInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

public class Serveur {

    public static void main(String[] args) {
        try {
            int port = 1099;
            MessagerieInterface skeleton = (MessagerieInterface) UnicastRemoteObject.exportObject(new Messagerie(),
                   0);
            System.out.println("Serveur prêt");
            Registry registry = LocateRegistry.getRegistry (port);
            System.out.println("Service Messagerie enregistré");
            if (!Arrays.asList(registry.list()).contains("Messagerie"))
                registry.bind("Messagerie", skeleton);
            else
                registry.rebind ("Messagerie", skeleton);
        } catch (Exception ex) {
            ex.printStackTrace ();
        }
    }
}
