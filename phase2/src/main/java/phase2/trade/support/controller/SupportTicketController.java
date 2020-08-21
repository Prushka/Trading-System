package phase2.trade.support.controller;


import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.EditableTableController;
import phase2.trade.editor.SupportTicketEditor;
import phase2.trade.refresh.ReType;
import phase2.trade.support.SupportTicket;
import phase2.trade.support.TicketPriority;
import phase2.trade.support.TicketState;
import phase2.trade.support.TicketType;
import phase2.trade.support.command.UpdateSupportTickets;

import java.util.List;

/**
 * The Support ticket controller.
 *
 * @author Dan Lyu
 */
@ControllerProperty(viewFile = "general_table_view.fxml")
public class SupportTicketController extends EditableTableController<SupportTicket, SupportTicketEditor> implements Initializable {


    /**
     * Constructs a new Support ticket controller.
     *
     * @param controllerResources the controller resources
     * @param ifMultipleSelection the if multiple selection
     */
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
                getNotificationFactory().toast("affect.next.log.in");
            });
            resultStatus.setAfter(() -> {
                disableButtons(false);
            });
            resultStatus.handle(getNotificationFactory());
        });
    }

    /**
     * Add content column.
     */
    protected void addContentColumn() {
        tableViewGenerator.addColumn("Content", "content");
    }

    /**
     * Add priority column.
     */
    protected void addPriorityColumn() {
        tableViewGenerator.addColumn("Priority", "ticketPriority");
    }

    /**
     * Add type column.
     */
    protected void addTypeColumn() {
        tableViewGenerator.addColumn("Type", "type");
    }

    /**
     * Add submitter column.
     */
    protected void addSubmitterColumn() {
        tableViewGenerator.addColumn("Submitter",
                param -> new SimpleStringProperty(param.getValue().getSubmitter().getName()));
    }

    /**
     * Add handler column.
     */
    protected void addHandlerColumn() {
        tableViewGenerator.addColumn("Handler",
                param -> {
                    if (param.getValue() == null || param.getValue().getHandler() == null) {
                        return new SimpleStringProperty("null");
                    }
                    return new SimpleStringProperty(param.getValue().getHandler().getName());
                });
    }

    /**
     * Add state column.
     *
     * @param editable the editable
     */
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

    /**
     * Add search content.
     */
    protected void addSearchContent() {
        addSearchField("Search Content", (entity, toMatch) -> {
            String lowerCaseFilter = toMatch.toLowerCase();
            return String.valueOf(entity.getContent()).toLowerCase().contains(lowerCaseFilter);
        });
    }

    /**
     * Add state combo box.
     */
    protected void addStateComboBox() {
        addComboBox(
                getNodeFactory().getEnumAsObservableString(TicketState.class),
                "Ticket State", "ALL",
                (entity, toMatch) -> entity.getTicketState().name().equalsIgnoreCase(toMatch));
    }

    /**
     * Add priority combo box.
     */
    protected void addPriorityComboBox() {
        addComboBox(
                getNodeFactory().getEnumAsObservableString(TicketPriority.class),
                "Ticket Priority", "ALL",
                (entity, toMatch) -> entity.getTicketPriority().name().equalsIgnoreCase(toMatch));
    }

    /**
     * Add type combo box.
     */
    protected void addTypeComboBox() {
        addComboBox(
                getNodeFactory().getEnumAsObservableString(TicketType.class),
                "Ticket Type", "ALL",
                (entity, toMatch) -> entity.getType().name().equalsIgnoreCase(toMatch));
    }


}
