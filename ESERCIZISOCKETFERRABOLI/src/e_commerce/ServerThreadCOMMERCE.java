package e_commerce;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ServerThreadCOMMERCE extends Thread {
    ArrayList<Product> articoli = new ArrayList<>();
    ServerSocket server = null;
    Socket client = null;
    Socket products = null;
    ArrayList<Product> carrello = new ArrayList<>();
    String stringaRicevuta = null;
    BufferedReader inDalClient;
    DataOutputStream outVersoClient;
    ObjectOutputStream productsToClient;

    public ServerThreadCOMMERCE(Socket client,Socket products) throws IOException {
        this.client = client;
        this.products = products;
        inDalClient = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
        outVersoClient = new DataOutputStream(this.client.getOutputStream());
        productsToClient=new ObjectOutputStream(this.products.getOutputStream());
    }

    public void comunica() throws Exception {
        articoli.addAll(getData());

            productsToClient.writeObject(articoli);

        System.out.println("Esecuzione partita!");

        stringaRicevuta = inDalClient.readLine();
        System.out.println(stringaRicevuta);
        while (stringaRicevuta != null && Integer.parseInt(stringaRicevuta) != 0) {
            int prodotto = Integer.parseInt(stringaRicevuta);
            if(prodotto > 0) {
                for (Product articolo : articoli) {
                    if (articolo.getId_product() == prodotto)
                        carrello.add(articolo);
                }
            }else{
                prodotto=-prodotto;
                for (Product articolo : articoli) {
                    if (articolo.getId_product() == prodotto)
                        carrello.remove(articolo);
                }
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
        product.setName("iPhone");
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
        product.setName("JBL speaker");
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
        product.setName("TV Samsung");
        product.setPrice(1500.99);
        product.setImgSrc("ESERCIZISOCKETFERRABOLI\\src\\e_commerce\\src/img/tv.png");
        products.add(product);

        product = new Product();
        product.setId_product(8);
        product.setName("PS5");
        product.setPrice(500.99);
        product.setImgSrc("ESERCIZISOCKETFERRABOLI\\src\\e_commerce\\src/img/ps5.png");
        products.add(product);

        product = new Product();
        product.setId_product(9);
        product.setName("lavatrice");
        product.setPrice(700.99);
        product.setImgSrc("ESERCIZISOCKETFERRABOLI\\src\\e_commerce\\src/img/lavatrice.png");
        products.add(product);

        return products;
    }

    private String calcolaScontrino(ArrayList<Product> carrelloFinale) throws IOException {
        StringBuilder scontrino = new StringBuilder();
        double prezzo = 0.00;
        for (Product i : carrelloFinale) {
            scontrino.append(i.getName()).append(";").append(i.getPrice()).append("\n");
            prezzo += i.getPrice();
        }
        double temp = Math.pow(10, 2);
        prezzo= Math.ceil(prezzo * temp) / temp;
//        return scontrino + "\n";
        return scontrino + "\n"+"totale;"+prezzo+"\n" ;
    }


    public void run() {
        try {
            comunica();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
