package e_commerce;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ServerThreadCOMMERCE extends Thread {
    ArrayList<Product> articoli = new ArrayList<>();
    ServerSocket server = null;
    Socket client = null;
    ArrayList<Product> carrello = new ArrayList<>();
    String stringaRicevuta = null;
    BufferedReader inDalClient;
    DataOutputStream outVersoClient;

    public ServerThreadCOMMERCE(Socket client) throws IOException {
        this.client = client;
        inDalClient = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
        outVersoClient = new DataOutputStream(this.client.getOutputStream());
    }

    public void comunica() throws Exception {
        articoli.addAll(getData());
        System.out.println("Esecuzione partita!");
        stringaRicevuta = inDalClient.readLine();
        System.out.println(stringaRicevuta);
        while (stringaRicevuta != null && Integer.parseInt(stringaRicevuta) != 0) {
            int prodotto = Integer.parseInt(stringaRicevuta);
            for (Product articolo : articoli) {
                if (articolo.getId_product() == prodotto)
                    carrello.add(articolo);
            }

            System.out.println("Stringa ricevuta e trasmessa. " + prodotto);
            stringaRicevuta = inDalClient.readLine();
        }
        String scontrino = calcolaScontrino(carrello);
        System.out.println(scontrino);
        outVersoClient.writeBytes(scontrino + "\n");
        outVersoClient.close();
        inDalClient.close();
        client.close();
    }

    private List<Product> getData() {
        List<Product> products = new ArrayList<>();
        Product product;

        product = new Product();
        product.setId_product(1);
        product.setName("iphone");
        product.setPrice(899.99);
        product.setImgSrc("ESERCIZISOCKETFERRABOLI\\src\\e_commerce\\src/img/iphone.png");
        products.add(product);

        product = new Product();
        product.setId_product(2);
        product.setName("fitbit");
        product.setPrice(30.99);
        product.setImgSrc("ESERCIZISOCKETFERRABOLI\\src\\e_commerce\\src/img/fitbit.png");
        products.add(product);

        product = new Product();
        product.setId_product(3);
        product.setName("jbl speaker");
        product.setPrice(150.50);
        product.setImgSrc("ESERCIZISOCKETFERRABOLI\\src\\e_commerce\\src/img/jbl_speaker.png");
        products.add(product);

        product = new Product();
        product.setId_product(4);
        product.setName("smartphone");
        product.setPrice(200.99);
        product.setImgSrc("ESERCIZISOCKETFERRABOLI\\src\\e_commerce\\src/img/smartphone.png");
        products.add(product);

        product = new Product();
        product.setId_product(5);
        product.setName("speakers");
        product.setPrice(400.99);
        product.setImgSrc("ESERCIZISOCKETFERRABOLI\\src\\e_commerce\\src/img/speakers.png");
        products.add(product);

        product = new Product();
        product.setId_product(6);
        product.setName("smartwatch");
        product.setPrice(100.99);
        product.setImgSrc("ESERCIZISOCKETFERRABOLI\\src\\e_commerce\\src/img/smartwatch.png");
        products.add(product);

        product = new Product();
        product.setId_product(7);
        product.setName("tv samsung");
        product.setPrice(1500.99);
        product.setImgSrc("ESERCIZISOCKETFERRABOLI\\src\\e_commerce\\src/img/tv.png");
        products.add(product);


        return products;
    }

    private String calcolaScontrino(ArrayList<Product> carrelloFinale) throws IOException {
        StringBuilder scontrino = new StringBuilder();
        double prezzo = 0.00;
        for (Product i : carrelloFinale) {
            scontrino.append(i.getName()).append(" ").append(i.getPrice()).append("\n");
            prezzo += i.getPrice();
        }
        return scontrino + "\n" + "totale spesa:" + prezzo;
    }


    public void run() {
        try {
            comunica();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
