package e_commerce;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private static Controller instance;
    public static DataOutputStream outVersoServer;
    private List<Product> products = new ArrayList<>();
    String ipServer = "localhost";
    int portaServer = 6789;
    int portaProduct = 6790;
    Socket clientSocket;
    Socket productSocket;
    String stringaRicevutaDalServer;
    BufferedReader inDalServer;
    ObjectInputStream productsFromServer;
    @FXML
    private Label contoTotale;
    public Label numberOfProducts;
    @FXML
    private Group shoppingCart;
    @FXML
    private TextArea receiptBuilder;
    @FXML
    private TextArea receiptPriceBuilder;
    @FXML
    private GridPane grid;

    private List<Product> getData() throws IOException, ClassNotFoundException {
            products=(List<Product>)productsFromServer.readObject();
        return products;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance=this;
        //connection
        try {
            System.out.println("Client partito in esecuzione");
            clientSocket = new Socket(ipServer, portaServer);
            productSocket=new Socket(ipServer,portaProduct);
            outVersoServer = new DataOutputStream(clientSocket.getOutputStream());
            inDalServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            productsFromServer = new ObjectInputStream(productSocket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Host sconosciuto");
        } catch (Exception e) {
            System.out.println("Errore durante la connessione");
            System.exit(1);
        }

        try {
            products.addAll(getData());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        numberOfProducts.setText(String.valueOf(0));
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < products.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("item.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                anchorPane.setOnMouseClicked(mouseEvent -> {
                    numberOfProducts.setText(String.valueOf(Integer.parseInt(numberOfProducts.getText()) + 1));
                    anchorPane.setId("productPressed");
                    PauseTransition pause = new PauseTransition(
                            Duration.seconds(0.2)
                    );
                    pause.setOnFinished(event -> {
                        anchorPane.setId("product");
                    });
                    pause.play();


                });

                ItemController itemController = fxmlLoader.getController();
                itemController.setData(products.get(i));


                if (column == 3) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row); //(child,column,row)
                //set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));

                shoppingCart.setOnMouseClicked(mouseEvent -> {

                    try {
                        outVersoServer.writeBytes("0\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    StringBuilder scontrino = new StringBuilder();
                    String line;
                    try {
                        StringBuilder descrizione=new StringBuilder();
                        StringBuilder prezzo=new StringBuilder();
                        while ((line = inDalServer.readLine()) != null) {
//                            if(line.contains("totale spesa")) {
//                                receiptBuilder.getItems().add(new Label("SPESA TOTALE:"));
//                                receiptPriceBuilder.getItems().add(new Label(line.replace("totale spesa:", "")));
//                            }else{
                            if (line.contains("totale")){
                                String[] parts = line.split(";");
                                contoTotale.setText(parts[1]+"€");
                            } else if (!line.equals("")){
                                String[] parts = line.split(";");
                                descrizione.append(parts[0]+"\n");
                                prezzo.append(parts[1]+"€\n");
                                receiptBuilder.setText(descrizione.toString());
                                receiptPriceBuilder.setText(prezzo.toString());
                            }
//                            scontrino.append(line).append("\n");

                        }



                        stringaRicevutaDalServer = scontrino.toString();
                        System.out.println(stringaRicevutaDalServer);
                        clientSocket.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Controller getInstance() {
        return instance;
    }
}
