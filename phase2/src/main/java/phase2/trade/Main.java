package phase2.trade;


import io.datafx.controller.flow.Flow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import phase2.trade.controller.LoginController;
import phase2.trade.controller.UserRepository;
import phase2.trade.repository.SaveHook;
import phase2.trade.view.SceneFactory;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        SaveHook saveHook = new SaveHook();
        Flow flow = new Flow(LoginController.class);


        SceneFactory sceneFactory = new SceneFactory();
        FXMLLoader login = sceneFactory.getLoader("login.fxml");

        LoginController loginController = new LoginController(new UserRepository(saveHook));
        login.setController(loginController);

        Scene scene = new Scene(login.load());

        primaryStage.setTitle("Trade");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}