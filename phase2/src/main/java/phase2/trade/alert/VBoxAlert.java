package phase2.trade.alert;

import javafx.stage.Stage;

public class VBoxAlert extends AlertWindow {

    public VBoxAlert(Stage parent, String title, String header, String confirmButtonText) {
        super(parent, title, header, confirmButtonText);
        body.setSpacing(35);
    }

    public VBoxAlert(Stage parent, String title, String header, String confirmButtonText, String cancelButtonText) {
        super(parent, title, header, confirmButtonText, cancelButtonText);
        body.setSpacing(35);
    }

}
