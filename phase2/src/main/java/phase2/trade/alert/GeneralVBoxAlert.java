package phase2.trade.alert;

import javafx.stage.Stage;

public class GeneralVBoxAlert extends AlertWindow<Void> {

    public GeneralVBoxAlert(Stage parent, String title, String header) {
        super(parent, title, header);
        body.setSpacing(35);
    }

    public Void display(String... args) {
        confirmButton.setOnAction(confirmHandler);

        layout.setActions(confirmButton);

        alert.setContent(layout);
        alert.showAndWait();
        return null;
    }

}
