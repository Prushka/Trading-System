package phase2.trade.alert;

import javafx.stage.Stage;

/**
 * The alert that has no button.
 *
 * @author Dan Lyu
 */
public class PopupAlert extends AlertWindow {

    /**
     * Constructs a new Popup alert.
     *
     * @param parent the parent
     * @param title  the title
     * @param header the header
     */
    public PopupAlert(Stage parent, String title, String header) {
        super(parent, title, header);
    }

}
