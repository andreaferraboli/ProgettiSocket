package e_commerce;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientStrCOMMERCE {

    String ipServer = "localhost";
    int portaServer = 6789;

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
            System.out.println("Inserisci 0 per stampare lo scontrino: ");
            System.out.println("Inserisci il prodotto da aggiungere al carrello: ");
            do {
                redditoUtente = scan.nextInt();
                if (redditoUtente < 0)
                    System.out.println("è stato inserito un numero negativo,riprovare");
            } while (redditoUtente < 0);

            System.out.println(".. invio la stringa al server e attendo..");

            outVersoServer.writeBytes(redditoUtente + "\n");

            if(redditoUtente ==0){
                System.out.println("\n-----SCONTRINO------");
                StringBuilder scontrino = new StringBuilder();
                String line;
                while( (line = inDalServer.readLine()) != null) {
                    scontrino.append(line).append("\n");
                }
                stringaRicevutaDalServer = scontrino.toString();

            }else{
                stringaRicevutaDalServer="ricevuto dal server: " +inDalServer.readLine();
            }
            System.out.println(stringaRicevutaDalServer);

            if (redditoUtente == 0) {
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
