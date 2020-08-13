package phase2.trade.view;

import com.jfoenix.controls.JFXListCell;
import javafx.scene.Node;
import javafx.scene.layout.*;

public class SideListCell extends JFXListCell<String> {

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            Node svg = new ImageFactory().generateSVG("user");
            if(svg!=null){
                setGraphic(svg);
                setGraphicTextGap(10);
            }
            setText(item);
        }
    }
}