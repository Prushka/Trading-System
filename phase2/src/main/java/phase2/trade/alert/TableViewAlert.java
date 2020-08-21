package phase2.trade.alert;

import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * The alert that contains a TableView.
 *
 * @param <T> the type parameter
 * @author Dan Lyu
 */
public class TableViewAlert<T> extends AlertWindow {

    /**
     * Constructs a new Table view alert.
     *
     * @param parent        the parent
     * @param title         the title
     * @param header        the header
     * @param confirmButton the confirm button
     */
    public TableViewAlert(Stage parent, String title, String header, String confirmButton) {
        super(parent, title, header, confirmButton);
        body.setSpacing(35);
    }

    /**
     * Add table view.
     *
     * @param tableView the table view
     */
    public void addTableView(TableView<T> tableView) {
        body.getChildren().addAll(tableView);
    }

    public void display() {
        layout.setActions(confirmButton);
        layout.setPrefWidth(1000);
        super.display();
    }

}
