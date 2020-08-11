package phase2.trade.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import phase2.trade.callback.ResultStatus;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Category;
import phase2.trade.item.Item;
import phase2.trade.item.command.AddItemToItemList;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class AddItemController extends AbstractController implements Initializable {

    public JFXComboBox<String> category;
    public JFXTextField name;
    public JFXTextArea description;
    public JFXButton submitButton;
    public GridPane root;

    private final ItemListType itemListType;
    private final ObservableList<Item> display;

    private Stage window;

    public AddItemController(ControllerResources controllerResources, ItemListType itemListType, ObservableList<Item> display) {
        super(controllerResources);
        this.itemListType = itemListType;
        this.display = display;
    }

    public void submitItem(ActionEvent actionEvent) {
        submitButton.setDisable(true);
        AddItemToItemList itemCommand = getCommandFactory().getCommand(AddItemToItemList::new,
                command -> command.setItemListType(itemListType));

        itemCommand.execute((result, resultStatus) -> {
            if (resultStatus == ResultStatus.NO_PERMISSION) {
                System.out.println("nor permission");
                getPopupFactory().noPermission();
            } else {
                Platform.runLater(() -> {
                    display.add(result);
                });
            }
            Platform.runLater(() -> {
                submitButton.setDisable(false);
                window.close();
            });
        }, name.getText(), description.getText(), category.getValue());

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        category.getItems().addAll(Arrays.asList(Stream.of(Category.values()).map(Category::name).toArray(String[]::new)));
        window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(250);

        Scene scene = new Scene(root);
        window.setScene(scene);
        window.showAndWait();
    }
}
