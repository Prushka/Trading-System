package phase2.trade.view.window;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TextFieldAlert extends AlertWindow<Void> {

    public TextFieldAlert(Stage parent, String title, String header) {
        super(parent, title, header);
    }

    private Boolean answer;

    public void addTextField(TextField textField) {
        layout.getBody().addAll(textField);
    }

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
