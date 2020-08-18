package phase2.trade.alert;

import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class TableViewAlert<T> extends AlertWindow {

    private Map<TextField, StringProperty> results = new HashMap<>();

    public TableViewAlert(Stage parent, String title, String header, String confirmButton) {
        super(parent, title, header, confirmButton);
        body.setSpacing(35);
    }

    public void addTableView(TableView<T> tableView) {
        body.getChildren().addAll(tableView);
    }

    private EventHandler<ActionEvent> confirmHandler;

    public void setConfirmHandler(EventHandler<ActionEvent> handler) {
        this.confirmHandler = handler;
    }

    public void display(String... args) {
        layout.setActions(confirmButton);
        layout.setPrefWidth(1000);
        super.display(args);
    }

}
