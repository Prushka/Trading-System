package phase2.trade.trade.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.item.controller.UserCell;
import phase2.trade.item.controller.UserStringConverter;
import phase2.trade.user.User;

import java.util.Collection;

@ControllerProperty(viewFile = "trade.fxml")
public abstract class TradeController extends AbstractController implements Initializable {

    @FXML
    BorderPane root;

    @FXML
    HBox topLeftHBox, topRightHBox;

    @FXML
    Pane leftTableArea, rightTableArea;

    @FXML
    VBox left, right;

    @FXML
    HBox buttonPane, globalPane;

    ComboBox<User> rightComboBox, leftComboBox;


    public TradeController(ControllerResources controllerResources) {
        super(controllerResources);
    }


    ComboBox<User> getUserComboBox(Collection<User> users) {
        ComboBox<User> comboBox = getNodeFactory().getDefaultComboBox(users);
        comboBox.setConverter(new UserStringConverter(comboBox, getAccountManager().getLoggedInUser().getUid()));
        comboBox.setCellFactory(e -> new UserCell(getAccountManager().getLoggedInUser().getUid()));
        return comboBox;
    }

}
