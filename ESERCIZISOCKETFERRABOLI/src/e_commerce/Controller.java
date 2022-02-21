package e_commerce;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public static int numberOfProductsCounter;
    public static DataOutputStream outVersoServer;
    private final List<Product> products = new ArrayList<>();
    public int index = -1;
    String ipServer = "localhost";
    int portaServer = 6789;
    Socket clientSocket;
    String stringaRicevutaDalServer;
    BufferedReader inDalServer;
    @FXML
    private VBox chosenProductsCard;
    @FXML
    private Label productNameLable;
    @FXML
    private Label productPriceLabel;
    @FXML
    private Label numberOfProducts;
    @FXML
    private ImageView productImg;
    @FXML
    private Group shoppingCart;
    @FXML
    private ListView receiptBuilder;
    @FXML
    private ListView receiptPriceBuilder;
    @FXML
    private GridPane grid;
    private Image image;

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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //connection
        try {
            System.out.println("Client partito in esecuzione");
            clientSocket = new Socket(ipServer, portaServer);
            outVersoServer = new DataOutputStream(clientSocket.getOutputStream());
            inDalServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Host sconosciuto");
        } catch (Exception e) {
            System.out.println("Errore durante la connessione");
            System.exit(1);
        }

        products.addAll(getData());
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
                    anchorPane.setStyle("-fx-background-color: #137903");
                    PauseTransition pause = new PauseTransition(
                            Duration.seconds(0.2)
                    );
                    pause.setOnFinished(event -> {
                        anchorPane.setStyle("-fx-background-color: rgba(255,255,255,0)");
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
                        while ((line = inDalServer.readLine()) != null) {
//                            if(line.contains("totale spesa")) {
//                                receiptBuilder.getItems().add(new Label("SPESA TOTALE:"));
//                                receiptPriceBuilder.getItems().add(new Label(line.replace("totale spesa:", "")));
//                            }else{
//                            String[] parts = line.split(";");
//                            String part1 = parts[0];
//                            String part2 = parts[1];
//                            scontrino.append(line).append("\n");
//                            receiptBuilder.getItems().add(new Label(part1));
//                            receiptPriceBuilder.getItems().add(new Label(part2));
                                receiptBuilder.getItems().add(new Label(line));
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
}
