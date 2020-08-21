package phase2.trade.alert;

import javafx.stage.Stage;

/**
 * The VBox alert with a VBox root.
 *
 * @author Dan Lyu
 */
public class VBoxAlert extends AlertWindow {

    /**
     * Constructs a new V box alert.
     *
     * @param parent            the parent
     * @param title             the title
     * @param header            the header
     * @param confirmButtonText the confirm button text
     */
    public VBoxAlert(Stage parent, String title, String header, String confirmButtonText) {
        super(parent, title, header, confirmButtonText);
        body.setSpacing(35);
    }

    /**
     * Constructs a new VBox alert.
     *
     * @param parent            the parent
     * @param title             the title
     * @param header            the header
     * @param confirmButtonText the confirm button text
     * @param cancelButtonText  the cancel button text
     */
    public VBoxAlert(Stage parent, String title, String header, String confirmButtonText, String cancelButtonText) {
        super(parent, title, header, confirmButtonText, cancelButtonText);
        body.setSpacing(35);
    }

}
