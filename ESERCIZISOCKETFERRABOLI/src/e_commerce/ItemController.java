package e_commerce;

import javafx.fxml.FXML;
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
        Image image = new Image(new FileInputStream(product.getImgSrc()));
        img.setImage(image);
    }
}
