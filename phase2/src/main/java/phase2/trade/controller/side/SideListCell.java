package phase2.trade.controller.side;

import com.jfoenix.controls.JFXListCell;
import javafx.scene.Node;
import phase2.trade.view.ImageFactory;

import java.util.ResourceBundle;

public class SideListCell extends JFXListCell<SideOption> {

    private final ResourceBundle resourceBundle;

    public SideListCell(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @Override
    protected void updateItem(SideOption item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            System.out.println(item.language);
            Node graphic = new ImageFactory().generateGraphic(item.iconPath);
            if (graphic != null) {
                setGraphic(graphic);
                setGraphicTextGap(10);
            }
            setText(resourceBundle.getString(getItem().language));
        }
    }
}
