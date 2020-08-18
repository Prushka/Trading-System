package phase2.trade.alert;

import javafx.stage.Stage;

public class PopupAlert extends AlertWindow<Void> {

    public PopupAlert(Stage parent, String title, String header) {
        super(parent, title, header);
    }

    public Void display(String... args) {
        layout.setActions(confirmButton);
        alert.setContent(layout);
        alert.showAndWait();
        return null;
    }

}
