package template;

import java.io.*;
import java.net.*;

public class ClientStr {

    String ipServer = "10.1.78.8";
    int portaServer = 6789;

    Socket clientSocket;
    BufferedReader tastiera;
    String stringaUtente;
    String stringaRicevutaDalServer;
    DataOutputStream outVersoServer;
    BufferedReader inDalServer;
   
    
    protected Socket connetti() {
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
    
    public void comunica(){
        try{
            System.out.println("Inserisci la stringa da trasmettere al server: ");
            stringaUtente = tastiera.readLine();
            System.out.println( ".. invio la stringa al server e attendo..");


            outVersoServer.writeBytes(stringaUtente + '\n');
            stringaRicevutaDalServer = inDalServer.readLine();
            System.out.println("ricevuto dal server: " + stringaRicevutaDalServer);

            if(stringaRicevutaDalServer.equals("Server in chiusura...")) {
                System.out.println( "CLIENT: termina elaborazione e chiudi connessione");
                clientSocket.close();
                System.exit(2);
            }




        }
        catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("Errore durante la comunicazione con il server");
            System.exit(1);
        }
    }
    
}
