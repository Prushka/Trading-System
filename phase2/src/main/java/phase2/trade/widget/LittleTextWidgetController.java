package phase2.trade.widget;

import javafx.scene.control.Label;
import phase2.trade.controller.ControllerResources;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The Little text widget controller.
 *
 * @author Dan Lyu
 */
public abstract class LittleTextWidgetController extends WidgetControllerBase {

    /**
     * The Title.
     */
    protected final String title;
    /**
     * The Contents.
     */
    protected final List<String> contents;

    /**
     * Constructs a new Little text widget controller.
     *
     * @param controllerResources the controller resources
     * @param title               the title
     * @param contents            the contents
     */
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
