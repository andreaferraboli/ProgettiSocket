package IRPEF;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientStrIRPEF {

    String ipServer = "localhost";
    int portaServer = 6788;

    Socket clientSocket;
    BufferedReader tastiera;
    Scanner scan = new Scanner(System.in);
    int redditoUtente;
    String stringaRicevutaDalServer;
    DataOutputStream outVersoServer;
    BufferedReader inDalServer;


    protected Socket connetti() {
        try {
            System.out.println("Client partito in esecuzione");
            tastiera = new BufferedReader(new InputStreamReader(System.in));
            clientSocket = new Socket(ipServer, portaServer);
            outVersoServer = new DataOutputStream(clientSocket.getOutputStream());
            inDalServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Host sconosciuto");
        } catch (Exception e) {
            System.out.println("Errore durante la connessione");
            System.exit(1);
        }
        return clientSocket;

    }

    public void comunica() {
        try {
            System.out.println("Inserisci il reddito da trasmettere al server: (-1 per uscire)");
            do {
                redditoUtente = scan.nextInt();
                if (redditoUtente < 0 && redditoUtente != -1)
                    System.out.println("è stato inserito un numero negativo,riprovare");
            } while (redditoUtente < 0 && redditoUtente != -1);

            System.out.println(".. invio la stringa al server e attendo..");

            outVersoServer.writeBytes(redditoUtente + "\n");
            stringaRicevutaDalServer = inDalServer.readLine();
            System.out.println("ricevuto dal server: " + stringaRicevutaDalServer);

            if (stringaRicevutaDalServer.equals("Server in chiusura...")) {
                System.out.println("CLIENT: termina elaborazione e chiudi connessione");
                clientSocket.close();
                System.exit(2);
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante la comunicazione con il server");
            System.exit(1);
        }
    }

}
