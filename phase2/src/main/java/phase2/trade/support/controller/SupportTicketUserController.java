package phase2.trade.support.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import phase2.trade.alert.SplitAlert;
import phase2.trade.controller.ControllerResources;
import phase2.trade.refresh.ReType;
import phase2.trade.support.TicketPriority;
import phase2.trade.support.TicketType;
import phase2.trade.support.command.OpenSupportTicket;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The Support ticket user controller.
 *
 * @author Dan Lyu
 */
public class SupportTicketUserController extends SupportTicketController implements Initializable {

    /**
     * Constructs a new Support ticket user controller.
     *
     * @param controllerResources the controller resources
     */
    public SupportTicketUserController(ControllerResources controllerResources) {
        super(controllerResources, true);
    }

    @Override
    public void refresh() {
        reloadNewDisplayData(getAccountManager().getLoggedInUser().getSupportTickets());
        tableView.refresh();
        super.refresh();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        setDisplayData(FXCollections.observableArrayList(getAccountManager().getLoggedInUser().getSupportTickets()));
        addSubmitterColumn();
        addPriorityColumn();
        addStateColumn(false);
        addContentColumn();
        addTypeColumn();
        addHandlerColumn();

        addSearchContent();
        addStateComboBox();
        addPriorityComboBox();
        addTypeComboBox();

        Button addButton = new JFXButton("Open Support Ticket");

        addButton(addButton);

        addButton.setOnAction(event -> {

            SplitAlert addAlert = getNotificationFactory().splitAlert("Open SupportTicket", "");

            TextArea content = getNodeFactory().getDefaultTextArea("Content");
            ComboBox<String> type = getNodeFactory().getComboBox(TicketType.class);
            ComboBox<String> priority = getNodeFactory().getComboBox(TicketPriority.class);

            addAlert.addLeft(content);
            addAlert.addRight(type, priority);
            addAlert.setConfirmHandler(event12 -> {
                OpenSupportTicket command = getCommandFactory().getCommand(OpenSupportTicket::new);
                command.execute((result, resultStatus) -> {
                    resultStatus.setSucceeded(() -> {
                        publish(ReType.REFRESH);
                    });
                    resultStatus.handle(getNotificationFactory());
                }, content.getText(), priority.getSelectionModel().getSelectedItem(), type.getSelectionModel().getSelectedItem());
            });
            addAlert.display();
        });
        tableViewGenerator.build();
    }


}
