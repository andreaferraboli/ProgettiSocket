
package impiccato;

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
        System.out.println(generateSecretWord());
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
    public String generateSecretWord() throws IOException {
        String randomWord="";

            BufferedReader reader = new BufferedReader(new FileReader("impiccato/dizionario.txt"));
            String line = reader.readLine();
            List<String> words = new ArrayList<String>();
            while(line != null) {
                String[] wordsLine = line.split(" ");
                words.addAll(Arrays.asList(wordsLine));
                line = reader.readLine();
            }

            Random rand = new Random(System.currentTimeMillis());
            randomWord = words.get(rand.nextInt(words.size()));

        return randomWord;
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
