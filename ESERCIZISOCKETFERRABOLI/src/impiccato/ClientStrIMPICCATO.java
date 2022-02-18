package impiccato;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientStrIMPICCATO {

    String ipServer = "localhost";
    int portaServer = 6789;

    Socket clientSocket;
    BufferedReader tastiera;
    String stringaUtente;
    String stringaRicevutaDalServer;
    DataOutputStream outVersoServer;
    BufferedReader inDalServer;
   
    
    public Socket connetti() {
      try{
        System.out.println("Client partito in esecuzione");
          tastiera = new BufferedReader (new InputStreamReader(System.in));
          clientSocket = new Socket (ipServer, portaServer);
          outVersoServer = new DataOutputStream (clientSocket.getOutputStream());
        inDalServer = new BufferedReader (new InputStreamReader(clientSocket.getInputStream()));


      }
      catch (UnknownHostException e){
          System.err.println("Host sconosciuto");
      }
      catch (Exception e){
          System.out.println("Errore durante la connessione");
          System.exit(1);
      }
      return clientSocket;

    }

    public void comunica() throws IOException {

        try{
            do {
                System.out.println("DIGITARE LA LETTERA PER INDOVINARE LA PAROLA | DIGITARE 'fine' PER ARRENDERSI | DIGITARE 'ricomincia' PER RICOMINCIARE: ");
                System.out.println("Inserisci la lettera o parola da trasmettere al server: ");
                    stringaUtente = tastiera.readLine().toLowerCase(Locale.ROOT);

            }while(!(stringaUtente.length() ==1 || stringaUtente.equals("fine") || stringaUtente.equals("ricomincia")));


            outVersoServer.writeBytes(stringaUtente + "\n");
            stringaRicevutaDalServer = inDalServer.readLine();
            if (stringaRicevutaDalServer.contains("grazie per aver giocato...") || stringaRicevutaDalServer.contains("numero tentativi finito...")){
                System.out.println(stringaRicevutaDalServer);
                clientSocket.close();
                System.exit(2);
            }else{
                System.out.println(stringaRicevutaDalServer);
            }

        }
        catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("Errore durante la comunicazione con il server");
            System.exit(1);
        }
    }

}
