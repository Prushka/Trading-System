package phase2.trade.alert;

import javafx.stage.Stage;

public class ConfirmAlert extends AlertWindow {

    public ConfirmAlert(Stage parent, String title, String header, String confirmButtonText, String cancelButtonText) {
        super(parent, title, header, confirmButtonText, cancelButtonText);
    }

    public void display(String... args) {
        layout.setActions(cancelButton, confirmButton);
        super.display(args);
    }

}
