package phase2.trade.widget;

import javafx.scene.control.Label;
import phase2.trade.controller.ControllerResources;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public abstract class LittleTextWidgetController extends WidgetControllerBase {

    protected final String title;
    protected final List<String> contents;

    public LittleTextWidgetController(ControllerResources controllerResources, String title, String... contents) {
        super(controllerResources);
        this.title = title;
        this.contents = Arrays.asList(contents);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addTitle(new Label(title));
        for (String content : contents) {
            addContent(new Label(content));
        }
    }
}
