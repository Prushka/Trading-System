package phase2.trade.controller;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class SideMenuController extends AbstractController implements Initializable {

    public JFXListView<Label> sideList;
    public Label userInfo, market, wishList, settings, logOut;

    private GridPane center;

    public SideMenuController(GridPane center) {
        this.center = center;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sideList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                switch (newValue.getId()) {
                    case "userInfo":
                        Parent userInfo = sceneFactory.getPane("user_info.fxml");
                        GridPane.setConstraints(userInfo, 0, 0);
                        center.getChildren().clear();
                        center.getChildren().addAll(userInfo);
                        break;
                }
            }
        });
    }
}
