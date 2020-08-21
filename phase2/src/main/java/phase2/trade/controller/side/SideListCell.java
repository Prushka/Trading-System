package phase2.trade.controller.side;

import com.jfoenix.controls.JFXListCell;
import javafx.scene.Node;
import phase2.trade.view.ImageFactory;

import java.util.ResourceBundle;

/**
 * The Side list cell used in the side menu ListView.
 *
 * @author Dan Lyu
 * @see SideOption
 */
public class SideListCell extends JFXListCell<SideOption> {

    private final ResourceBundle resourceBundle;

    /**
     * Constructs a new Side list cell.
     *
     * @param resourceBundle the resource bundle
     */
    public SideListCell(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    /**
     * {@inheritDoc}
     * The ResourceBundle will be used to set the language of this SideOption
     * An image factory will be used to generate the ImageView that contains the icon of this side option.
     */
    @Override
    protected void updateItem(SideOption item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            Node graphic = new ImageFactory().generateGraphic(item.iconPath);
            if (graphic != null) {
                setGraphic(graphic);
                setGraphicTextGap(10);
            }
            setText(resourceBundle.getString(getItem().language));
        }
    }
}
