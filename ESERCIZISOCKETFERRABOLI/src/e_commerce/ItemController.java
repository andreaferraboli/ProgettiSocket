package e_commerce;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


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
    private Product product;

    @FXML
    private void click(MouseEvent mouseEvent) throws IOException {
//        myListener.onClickListener(product);
        Controller.outVersoServer.writeBytes(product.getId_product() + "\n");

    }

    public void setData(Product product) throws IOException {
        this.product = product;
        nameLabel.setText(product.getName());
        priceLable.setText(product.getPrice() + "$");
        Label numberOfProducts = Controller.getInstance().numberOfProducts;

        buyButton.setOnMouseClicked(mouseEvent -> {
            numberOfItem.setText(String.valueOf(Integer.parseInt(numberOfItem.getText()) + 1));
            numberOfProducts.setText(String.valueOf(Integer.parseInt(numberOfProducts.getText()) + 1));
            try {
                Controller.outVersoServer.writeBytes(product.getId_product() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        sellButton.setOnMouseClicked(mouseEvent -> {
            if(Integer.parseInt(numberOfItem.getText()) != 0){
                numberOfItem.setText(String.valueOf(Integer.parseInt(numberOfItem.getText()) - 1));
                numberOfProducts.setText(String.valueOf(Integer.parseInt(numberOfProducts.getText()) - 1));
                try {
                    Controller.outVersoServer.writeBytes(-product.getId_product() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        Image image = new Image(new FileInputStream(product.getImgSrc()));
        img.setImage(image);
    }
}
