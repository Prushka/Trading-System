package phase2.trade.support.controller;


import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.ComboBoxTableCell;
import phase2.trade.command.Command;
import phase2.trade.controller.AbstractEditableTableController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.editor.ItemEditor;
import phase2.trade.editor.SupportTicketEditor;
import phase2.trade.editor.UserEditor;
import phase2.trade.item.Category;
import phase2.trade.item.Ownership;
import phase2.trade.refresh.ReType;
import phase2.trade.support.SupportTicket;
import phase2.trade.support.TicketPriority;
import phase2.trade.support.TicketState;
import phase2.trade.support.TicketType;
import phase2.trade.support.command.UpdateSupportTickets;

import java.util.List;

@ControllerProperty(viewFile = "general_table_view.fxml")
public class SupportTicketController extends AbstractEditableTableController<SupportTicket, SupportTicketEditor> implements Initializable {


    public SupportTicketController(ControllerResources controllerResources, boolean ifMultipleSelection) {
        super(controllerResources, ifMultipleSelection, true, SupportTicketEditor::new);
    }

    @Override
    protected void updateEntity(List<SupportTicket> entities) {
        disableButtons(true);
        UpdateSupportTickets command = getCommandFactory().getCommand(UpdateSupportTickets::new, c -> {
            c.setToUpdate(entities);
        });
        command.execute((result, resultStatus) -> {
            resultStatus.setSucceeded(() -> {
                publish(ReType.REFRESH);
                getPopupFactory().toast("affect.next.log.in");
            });
            resultStatus.setAfter(() -> {
                disableButtons(false);
            });
            resultStatus.handle(getPopupFactory());
        });
    }

    protected void addContentColumn() {
        tableViewGenerator.addColumn("Content", "content");
    }

    protected void addPriorityColumn() {
        tableViewGenerator.addColumn("Priority", "ticketPriority");
    }

    protected void addTypeColumn() {
        tableViewGenerator.addColumn("Type", "type");
    }

    protected void addSubmitterColumn() {
        tableViewGenerator.addColumn("Submitter",
                param -> new SimpleStringProperty(param.getValue().getSubmitter().getName()));
    }

    protected void addHandlerColumn() {
        tableViewGenerator.addColumn("Handler",
                param -> {
                    if (param.getValue() == null || param.getValue().getHandler() == null) {
                        return new SimpleStringProperty("null");
                    }
                    return new SimpleStringProperty(param.getValue().getHandler().getName());
                });
    }

    protected void addStateColumn(boolean editable) {
        if (editable) {
            tableViewGenerator.addColumnEditable("State", "ticketState",
                    event -> shortenAlter(event.getRowValue(), event.getNewValue(), resultStatus -> {
                    }, SupportTicketEditor::alterTicketState),
                    getNodeFactory().getEnumAsObservableString(TicketState.class));
        } else {
            tableViewGenerator.addColumn("State", "ticketState");
        }
    }

    protected void addSearchContent() {
        addSearchField("Search Content", (entity, toMatch) -> {
            String lowerCaseFilter = toMatch.toLowerCase();
            return String.valueOf(entity.getContent()).toLowerCase().contains(lowerCaseFilter);
        });
    }

    protected void addStateComboBox() {
        addComboBox(
                getNodeFactory().getEnumAsObservableString(TicketState.class),
                "Ticket State", "ALL",
                (entity, toMatch) -> entity.getTicketState().name().equalsIgnoreCase(toMatch));
    }

    protected void addPriorityComboBox() {
        addComboBox(
                getNodeFactory().getEnumAsObservableString(TicketPriority.class),
                "Ticket Priority", "ALL",
                (entity, toMatch) -> entity.getTicketPriority().name().equalsIgnoreCase(toMatch));
    }

    protected void addTypeComboBox() {
        addComboBox(
                getNodeFactory().getEnumAsObservableString(TicketType.class),
                "Ticket Type", "ALL",
                (entity, toMatch) -> entity.getType().name().equalsIgnoreCase(toMatch));
    }


}
