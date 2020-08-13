package phase2.trade.view;


import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.TextField;

public class NodeFactory {

    public NodeFactory() {

    }

    public TextField getDefaultTextField(String promptText) {
        JFXTextField jfxTextField = new JFXTextField();
        jfxTextField.setPromptText(promptText);
        jfxTextField.setLabelFloat(true);
        return jfxTextField;
    }
}
