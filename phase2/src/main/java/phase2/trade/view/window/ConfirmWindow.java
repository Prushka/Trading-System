package phase2.trade.view.window;

import com.jfoenix.controls.JFXButton;
import javafx.stage.Stage;

public class ConfirmWindow extends AlertWindow<Boolean> {

    public ConfirmWindow(Stage parent, String title, String header) {
        super(parent, title, header);
    }

    private Boolean answer = false;

    public Boolean display(String... args) {
        JFXButton yesButton = new JFXButton("Yes");
        JFXButton noButton = new JFXButton("No");
        yesButton.setOnAction(event -> {
            answer = true;
            alert.hideWithAnimation();
        });
        noButton.setOnAction(event -> {
            answer = false;
            alert.hideWithAnimation();
        });
        yesButton.setFocusTraversable(false);
        noButton.setFocusTraversable(false);
        layout.setActions(yesButton, noButton);
        alert.setContent(layout);
        alert.showAndWait();

        return answer;
    }

}
