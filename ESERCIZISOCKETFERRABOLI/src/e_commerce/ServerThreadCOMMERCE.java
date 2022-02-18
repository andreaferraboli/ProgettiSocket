package e_commerce;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ServerThreadCOMMERCE extends Thread {
    HashMap<Integer,Double> articoli=new HashMap<>();
    HashMap<Integer,String> mercato=new HashMap<>();
    ServerSocket server = null;
    Socket client = null;
    ArrayList<Integer> carrello=new ArrayList<>();
    String stringaRicevuta = null;
    BufferedReader inDalClient;
    DataOutputStream outVersoClient;

    public ServerThreadCOMMERCE(Socket client) throws IOException {
        this.client = client;
        inDalClient = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
        outVersoClient = new DataOutputStream(this.client.getOutputStream());
    }

    public void comunica() throws Exception {

        articoli.put(1,2.50);
        articoli.put(2,0.99);
        articoli.put(3,1.50);
        articoli.put(5,0.50);
        articoli.put(4,6.00);
        articoli.put(6,3.99);
        mercato.put(1,"sapone");
        mercato.put(2,"carta");
        mercato.put(3,"pasta");
        mercato.put(5,"sugo");
        mercato.put(4,"pizza");
        mercato.put(6,"gelato");
        System.out.println("Esecuzione partita!");
        stringaRicevuta = inDalClient.readLine();
        System.out.println(stringaRicevuta);
        while (stringaRicevuta != null && Integer.parseInt(stringaRicevuta)!=0) {
            int prodotto = Integer.parseInt(stringaRicevuta);
            carrello.add(prodotto);
            outVersoClient.writeBytes("articolo aggiunto correttamente al carrello \n");
            System.out.println("Stringa ricevuta e trasmessa. ");
            stringaRicevuta = inDalClient.readLine();
        }
        String scontrino=calcolaScontrino(carrello);
        System.out.println(scontrino);
        outVersoClient.writeBytes(scontrino+"\n");
        outVersoClient.close();
        inDalClient.close();
        client.close();
    }

    private String calcolaScontrino(ArrayList<Integer> carrelloFinale) throws IOException {
        StringBuilder scontrino= new StringBuilder();
        double prezzo=0;
        for (Integer i:carrelloFinale) {
            scontrino.append(mercato.get(i)).append(" ").append(articoli.get(i)).append("\n");
            prezzo+=articoli.get(i);
        }
        return scontrino+"\n"+"totale spesa:"+prezzo;
    }


    public void run() {
        try {
            comunica();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
