package phase2.trade.support.controller;

import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import phase2.trade.command.Command;
import phase2.trade.controller.ControllerResources;
import phase2.trade.support.SupportTicket;
import phase2.trade.support.command.GetAllSupportTickets;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SupportTicketAdminController extends SupportTicketController implements Initializable {

    public SupportTicketAdminController(ControllerResources controllerResources) {
        super(controllerResources, true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        Command<List<SupportTicket>> command = getCommandFactory().getCommand(GetAllSupportTickets::new);
        command.execute((result, resultStatus) -> {
            resultStatus.setSucceeded(() -> {
                setDisplayData(FXCollections.observableArrayList(result));
                afterFetch();
            });
            resultStatus.handle(getPopupFactory());
        });
    }


    private void afterFetch() {
        addSubmitterColumn();
        addPriorityColumn();
        addStateColumn(true);
        addContentColumn();
        addTypeColumn();
        addHandlerColumn();

        addSearchContent();
        addStateComboBox();
        addPriorityComboBox();
        addTypeComboBox();

        tableViewGenerator.build();
    }

}
