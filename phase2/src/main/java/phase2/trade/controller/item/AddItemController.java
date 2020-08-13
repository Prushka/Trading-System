package phase2.trade.controller.item;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Category;
import phase2.trade.item.Item;
import phase2.trade.item.command.AddItemToItemList;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Stream;

@ControllerProperty(viewFile = "add_item.fxml")
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

    // this action does not reside in the ListChangeListener since
    // the unique id can only be set from database, which means an Item has to be created first
    // So if the item has to be created first using Command, then it's unnecessary to put this in listener
    // The listener is supposed to bind the change in display data to the Command
    public void submitItem(ActionEvent actionEvent) {
        submitButton.setDisable(true);
        AddItemToItemList itemCommand = getCommandFactory().getCommand(AddItemToItemList::new,
                command -> command.setItemListType(itemListType));

        itemCommand.execute((result, resultStatus) -> {
            System.out.println("executed");
            resultStatus.setSucceeded(() -> display.add(result));
            resultStatus.setAfter(() -> {
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
