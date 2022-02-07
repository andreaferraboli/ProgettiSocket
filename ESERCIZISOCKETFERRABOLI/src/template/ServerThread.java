
package template;

import java.io.*;
import java.net.*;
import java.util.*;


public class ServerThread extends Thread{
    ServerSocket server = null;
    Socket client = null;
    String stringaRicevuta = null;
    String stringaModificata = null;
    BufferedReader inDalClient;
    DataOutputStream outVersoClient;

    public ServerThread (Socket client) throws IOException {
        this.client = client;
        inDalClient = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
        outVersoClient = new DataOutputStream (this.client.getOutputStream());
    }

    public void comunica() throws Exception{
        System.out.println("Esecuzione partita!");
        stringaRicevuta = inDalClient.readLine();
        while(stringaRicevuta != null && !stringaRicevuta.equals("FINE"))
        {
            stringaModificata = stringaRicevuta.toUpperCase();
            outVersoClient.writeBytes( stringaModificata + "\n");
            System.out.println("Stringa ricevuta e trasmessa. ");
            stringaRicevuta = inDalClient.readLine();
        }

        outVersoClient.writeBytes("Server in chiusura...");
        outVersoClient.close();
        inDalClient.close();
        client.close();
    }

    public  void run()
    {
        try {
            comunica();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
