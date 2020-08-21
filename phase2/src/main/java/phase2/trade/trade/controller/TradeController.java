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
import phase2.trade.item.controller.UserStringConverter;
import phase2.trade.user.User;

import java.util.Collection;

/**
 * The Trade controller.
 *
 * @author Dan Lyu
 */
@ControllerProperty(viewFile = "trade.fxml")
public abstract class TradeController extends AbstractController implements Initializable {

    /**
     * The Root.
     */
    @FXML
    BorderPane root;

    /**
     * The Top left h box.
     */
    @FXML
    HBox topLeftHBox, /**
     * The Top right h box.
     */
    topRightHBox;

    /**
     * The Left table area.
     */
    @FXML
    Pane leftTableArea, /**
     * The Right table area.
     */
    rightTableArea;

    /**
     * The Left.
     */
    @FXML
    VBox left, /**
     * The Right.
     */
    right;

    /**
     * The Button pane.
     */
    @FXML
    HBox buttonPane, /**
     * The Global pane.
     */
    globalPane;

    /**
     * The Right combo box.
     */
    ComboBox<User> rightComboBox, /**
     * The Left combo box.
     */
    leftComboBox;


    /**
     * Constructs a new Trade controller.
     *
     * @param controllerResources the controller resources
     */
    public TradeController(ControllerResources controllerResources) {
        super(controllerResources);
    }


    /**
     * Gets user combo box.
     *
     * @param users the users
     * @return the user combo box
     */
    ComboBox<User> getUserComboBox(Collection<User> users) {
        ComboBox<User> comboBox = getNodeFactory().getDefaultComboBox(users);
        comboBox.setConverter(new UserStringConverter(comboBox, getAccountManager().getLoggedInUser().getUid()));
        comboBox.setCellFactory(e -> new UserCell(getAccountManager().getLoggedInUser().getUid()));
        return comboBox;
    }

}
