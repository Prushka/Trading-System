package phase2.trade.view.window;

import com.jfoenix.controls.JFXButton;
import javafx.stage.Stage;
import phase2.trade.view.window.AlertWindow;

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
