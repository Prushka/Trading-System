package phase2.trade.controller.side;

import com.jfoenix.controls.JFXListCell;
import javafx.scene.Node;
import phase2.trade.view.ImageFactory;

public class SideListCell extends JFXListCell<SideOption> {

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
            setText(item.language);
        }
    }
}
