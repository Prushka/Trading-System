package phase2.trade.view.window;

import com.jfoenix.controls.JFXButton;
import javafx.stage.Stage;
import phase2.trade.view.window.AlertWindow;

public class PopupWindow extends AlertWindow<Void> {

    public PopupWindow(Stage parent, String title, String header) {
        super(parent, title, header);
    }

    private Boolean answer;

    public Void display(String... args) {
        JFXButton okButton = new JFXButton("Ok");
        okButton.setOnAction(event -> {
            alert.hideWithAnimation();
        });
        okButton.setFocusTraversable(false);
        layout.setActions(okButton);
        alert.setContent(layout);
        alert.showAndWait();
        return null;
    }

}
