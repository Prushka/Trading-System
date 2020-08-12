package phase2.trade.view.window;

import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class AlertWindow<T> extends CustomWindow<T> {

    private final String title, content;

    protected JFXAlert alert;

    protected JFXDialogLayout layout;

    public AlertWindow(Stage parent, String title, String content) {
        super(parent);
        this.title = title;
        this.content = content;
        generateAlert();
    }

    protected void generateAlert() {
        alert = new JFXAlert(parent);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(true);
        alert.setAnimation(JFXAlertAnimation.NO_ANIMATION);
        layout = new JFXDialogLayout();
        layout.setHeading(new Label(title));
        layout.setBody(new Label(content));
    }

}
