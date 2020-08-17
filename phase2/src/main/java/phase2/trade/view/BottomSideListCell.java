package phase2.trade.view;

import com.jfoenix.controls.JFXListCell;
import javafx.scene.Node;
import phase2.trade.controller.side.BottomSideOption;
import phase2.trade.controller.side.SideOption;

public class BottomSideListCell extends JFXListCell<BottomSideOption> {

    @Override
    protected void updateItem(BottomSideOption item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            Node graphic = new ImageFactory().generateGraphic(item.resourcePath);
            if (graphic != null) {
                setGraphic(graphic);
                setGraphicTextGap(10);
            }
            setText(item.language);
        }
    }
}
