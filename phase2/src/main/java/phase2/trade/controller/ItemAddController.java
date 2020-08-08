package phase2.trade.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import phase2.trade.item.Category;

import java.net.URL;
import java.util.ResourceBundle;

public class ItemAddController implements Initializable {

    public JFXComboBox<Category> category;
    public JFXTextField name;
    public JFXTextArea description;


    public ItemAddController() {
        category = new JFXComboBox<>();
        category.getItems().addAll(Category.BOOK, Category.ELECTRONIC, Category.EQUIPMENT, Category.FURNITURE,
                Category.MOVIE, Category.VIDEO_GAME, Category.MISCELLANEOUS);
    }

    public void submitItem(ActionEvent actionEvent) {
        // itemManager.createAndAddItemTo(InventoryType.INVENTORY, new Callback<Item>() {
        //     @Override
        //     public void call(Item result) {
        //         System.out.println(result.getOwnership());
        //     }
        // }, category.getValue(), name.getText(), description.getText());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
