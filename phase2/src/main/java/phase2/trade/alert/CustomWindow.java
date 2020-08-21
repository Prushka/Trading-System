package phase2.trade.alert;

import javafx.stage.Stage;

/**
 * The base wrapper for anything that can be displayed.
 * It holds the root stage.
 *
 * @author Dan Lyu
 */
public abstract class CustomWindow {

    /**
     * The root Stage.
     */
    protected final Stage parent;

    /**
     * Constructs a new Custom window.
     *
     * @param parent the parent
     */
    public CustomWindow(Stage parent) {
        this.parent = parent;
    }

    /**
     * Display.
     */
    public abstract void display();
}
