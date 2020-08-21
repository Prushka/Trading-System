package phase2.trade.alert;

import javafx.stage.Stage;

/**
 * The Alert used to prompt a Confirmation. Has buttons Cancel and Confirm.
 *
 * @author Dan Lyu
 */
public class ConfirmAlert extends AlertWindow {

    /**
     * Constructs a new Confirm alert.
     *
     * @param parent            the parent
     * @param title             the title
     * @param header            the header
     * @param confirmButtonText the confirm button text
     * @param cancelButtonText  the cancel button text
     */
    public ConfirmAlert(Stage parent, String title, String header, String confirmButtonText, String cancelButtonText) {
        super(parent, title, header, confirmButtonText, cancelButtonText);
    }

    public void display() {
        layout.setActions(cancelButton, confirmButton);
        super.display();
    }

}
