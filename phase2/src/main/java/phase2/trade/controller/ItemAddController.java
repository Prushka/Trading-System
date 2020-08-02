package phase2.trade.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.Initializable;
import phase2.trade.database.Callback;
import phase2.trade.item.Item;
import phase2.trade.item.ItemManager;

import java.net.URL;
import java.util.ResourceBundle;

public class ItemAddController implements Initializable {

    public JFXComboBox<String> category;
    public JFXTextField name;
    public JFXTextArea description;

    private ItemManager itemManager;

    public ItemAddController() {

    }

    public void submitItem() {
        itemManager.addItem(result -> {

        }, category.getSelectionModel().getSelectedItem(), name.getText(), description.getText());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
