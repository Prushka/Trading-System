package phase2.trade.view;

import javafx.stage.Stage;

public abstract class CustomWindow<T> {

    protected final Stage parent;
    public CustomWindow(Stage parent){
        this.parent = parent;
    }

    public abstract T display(String... args);
}
