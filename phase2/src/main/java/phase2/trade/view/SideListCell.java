package phase2.trade.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListCell;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import phase2.trade.item.Item;
import phase2.trade.item.command.AddToCart;

import java.util.ArrayList;
import java.util.List;

public class SideListCell extends JFXListCell<String> {

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            Pane svg = new SVGFactory().generateSVG("user");
            if(svg!=null){
                setGraphic(svg);
                setGraphicTextGap(10);
            }
            setText(item);
        }
    }
}
