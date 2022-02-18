package IRPEF;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerThreadIRPEF extends Thread {
    int SCAGLIONE_1 = 15000;
    int SCAGLIONE_2 = 28000;
    int SCAGLIONE_3 = 50000;
    int ALIQUOTA_1 = 23;
    int ALIQUOTA_2 = 25;
    int ALIQUOTA_3 = 35;
    int ALIQUOTA_4 = 43;

    ServerSocket server = null;
    Socket client = null;
    String stringaRicevuta = null;
    String stringaModificata = null;
    BufferedReader inDalClient;
    DataOutputStream outVersoClient;

    public ServerThreadIRPEF(Socket client) throws IOException {
        this.client = client;
        inDalClient = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
        outVersoClient = new DataOutputStream(this.client.getOutputStream());
    }

    public void comunica() throws Exception {
        System.out.println("Esecuzione partita!");
        stringaRicevuta = inDalClient.readLine();
        System.out.println(stringaRicevuta);
        while (stringaRicevuta != null && !stringaRicevuta.equals("FINE")) {
            int redditoRicevuto = Integer.parseInt(stringaRicevuta);
            stringaModificata = calcoloIrpef(redditoRicevuto);
            outVersoClient.writeBytes(stringaModificata + "\n");
            System.out.println("Stringa ricevuta e trasmessa. ");
            stringaRicevuta = inDalClient.readLine();
        }

        outVersoClient.writeBytes("Server in chiusura...");
        outVersoClient.close();
        inDalClient.close();
        client.close();
    }

    private String calcoloIrpef(int redditoRicevuto) {
        int irpef;
        int quota1 = SCAGLIONE_1 * ALIQUOTA_1 / 100;
        int quota2 = (SCAGLIONE_2 - SCAGLIONE_1) * ALIQUOTA_2 / 100;
        int quota3 = (SCAGLIONE_3 - SCAGLIONE_2) * ALIQUOTA_3 / 100;
        if (redditoRicevuto <= SCAGLIONE_1)
            irpef = redditoRicevuto * ALIQUOTA_1 / 100;
        else if (redditoRicevuto <= SCAGLIONE_2)
            irpef = quota1 + (redditoRicevuto - SCAGLIONE_1) * ALIQUOTA_2 / 100;
        else if (redditoRicevuto <= SCAGLIONE_3)
            irpef = quota1 + quota2 + (redditoRicevuto - SCAGLIONE_2) * ALIQUOTA_3 / 100;
        else
            irpef = quota1 + quota2 + quota3 + (redditoRicevuto - SCAGLIONE_3) * ALIQUOTA_4 / 100;
        return String.valueOf(irpef);
    }

    public void run() {
        try {
            comunica();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
