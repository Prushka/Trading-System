package phase2.trade.view.widget;

import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class SmallTextWidget extends Widget {


    public SmallTextWidget(String gradient, String title, String... contents) {
        super(gradient, 270, 120);
        List<Label> labelList = new ArrayList<>();
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().addAll("butter-header");
        labelList.add(titleLabel);
        for (String content : contents) {
            Label label = new Label(content);
            label.getStyleClass().addAll("butter-body");
            labelList.add(label);
        }
        addNodes(labelList.toArray(new Label[0]));
    }

    public SmallTextWidget(String gradient) {
        super(gradient, 270, 120);
    }

}
