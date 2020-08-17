package phase2.trade.support.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.ResultStatus;
import phase2.trade.controller.ControllerResources;
import phase2.trade.refresh.ReType;
import phase2.trade.support.TicketPriority;
import phase2.trade.support.TicketType;
import phase2.trade.support.command.OpenSupportTicket;
import phase2.trade.user.User;
import phase2.trade.user.command.ReloadUser;
import phase2.trade.view.window.GeneralSplitAlert;

import java.net.URL;
import java.util.ResourceBundle;

public class SupportTicketUserController extends SupportTicketController implements Initializable {

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

        Button addButton = new JFXButton("Add");

        addButton(addButton);

        addButton.setOnAction(event -> {

            GeneralSplitAlert addAlert = getPopupFactory().splitAlert("Open SupportTicket", "");

            TextArea content = getNodeFactory().getDefaultTextArea("Content");
            ComboBox<String> type = getNodeFactory().getComboBox(TicketType.class);
            ComboBox<String> priority = getNodeFactory().getComboBox(TicketPriority.class);

            addAlert.addLeft(content);
            addAlert.addRight(type, priority);
            addAlert.setEventHandler(event12 -> {
                OpenSupportTicket command = getCommandFactory().getCommand(OpenSupportTicket::new);
                command.execute((result, resultStatus) -> {
                    resultStatus.setSucceeded(() -> {
                        publish(ReType.REFRESH);
                    });
                    resultStatus.handle(getPopupFactory());
                }, content.getText(), priority.getSelectionModel().getSelectedItem(), type.getSelectionModel().getSelectedItem());
            });
            addAlert.display();
        });
        tableViewGenerator.build();
    }


}
