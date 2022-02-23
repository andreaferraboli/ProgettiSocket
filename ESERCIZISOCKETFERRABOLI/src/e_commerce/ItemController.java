package e_commerce;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.IOException;


public class ItemController {
    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLable;
    @FXML
    private Button buyButton;
    @FXML
    private Button sellButton;
    @FXML
    private Button numberOfItem;
    @FXML
    private ImageView img;
    @FXML
    private AnchorPane itemAnchorPane;
    private Product product;

    @FXML
    private void click(MouseEvent mouseEvent) throws IOException {
//        myListener.onClickListener(product);
        Controller.outVersoServer.writeBytes(product.getId_product() + "\n");

    }

    public void setData(Product product) throws IOException {
        this.product = product;
        nameLabel.setText(product.getName());
        priceLable.setText(product.getPrice() + "€");
        Label numberOfProducts = Controller.getInstance().numberOfProducts;

        buyButton.setOnMouseClicked(mouseEvent -> {
            numberOfItem.setText(String.valueOf(Integer.parseInt(numberOfItem.getText()) + 1));
            numberOfProducts.setText(String.valueOf(Integer.parseInt(numberOfProducts.getText()) + 1));
            try {
                Controller.outVersoServer.writeBytes(product.getId_product() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            itemAnchorPane.setId("productBought");
            PauseTransition pause = new PauseTransition(
                    Duration.seconds(0.2)
            );
            pause.setOnFinished(event -> {
                itemAnchorPane.setId("product");
            });
            pause.play();
        });
        sellButton.setOnMouseClicked(mouseEvent -> {
            if (Integer.parseInt(numberOfItem.getText()) != 0) {
                numberOfItem.setText(String.valueOf(Integer.parseInt(numberOfItem.getText()) - 1));
                numberOfProducts.setText(String.valueOf(Integer.parseInt(numberOfProducts.getText()) - 1));
                try {
                    Controller.outVersoServer.writeBytes(-product.getId_product() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                itemAnchorPane.setId("productRemoved");
                PauseTransition pause = new PauseTransition(
                        Duration.seconds(0.2)
                );
                pause.setOnFinished(event -> {
                    itemAnchorPane.setId("product");
                });
                pause.play();
            }

        });
        Image image = new Image(new FileInputStream(product.getImgSrc()));
        img.setImage(image);
    }
}
