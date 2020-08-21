package phase2.trade.view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import phase2.trade.controller.ControllerFactory;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.ControllerSupplier;

/**
 * The Scene manager.
 *
 * @author Dan Lyu
 */
public class SceneManager {

    private final Stage window;
    private final ParentLoader parentLoader;
    private final ControllerFactory controllerFactory;

    /**
     * Constructs a new Scene manager.
     *
     * @param controllerResources the controller resources
     */
    public SceneManager(ControllerResources controllerResources) {
        this.window = controllerResources.getWindow();
        parentLoader = new ParentLoader();
        controllerFactory = new ControllerFactory(controllerResources);
    }

    /***********************
     *                      *
     * Switch Scene         *
     *                      *
     * @param parent the parent
     * @param applyCSS the apply css
     */
    public void switchScene(Parent parent, boolean applyCSS) {
        Scene scene = new Scene(parent);
        if (applyCSS) {
            scene.getStylesheets().add("css/trade.css");
        }
        window.setScene(scene);
    }

    /**
     * Switch scene.
     *
     * @param fileName   the file name
     * @param controller the controller
     * @param applyCSS   the apply css
     */
    public void switchScene(String fileName, Object controller, boolean applyCSS) {
        switchScene(parentLoader.loadParent(fileName, controller), applyCSS);
    }

    /**
     * Switch scene.
     *
     * @param fileName   the file name
     * @param controller the controller
     */
    public void switchScene(String fileName, Object controller) {
        this.switchScene(fileName, controller, true);
    }

    /**
     * Switch scene.
     *
     * @param controller the controller
     */
    public void switchScene(Object controller) {
        this.switchScene(controllerFactory.getViewFileFromController(controller), controller, true);
    }

    /**
     * Switch scene t.
     *
     * @param <T>                the type parameter
     * @param fileName           the file name
     * @param controllerSupplier the controller supplier
     * @return the t
     */
    public <T> T switchScene(String fileName, ControllerSupplier<T> controllerSupplier) {
        T controller = controllerFactory.getController(controllerSupplier);
        this.switchScene(fileName, controller, true);
        return controller;
    }

    /**
     * Switch scene t.
     *
     * @param <T>                the type parameter
     * @param controllerSupplier the controller supplier
     * @return the t
     */
    public <T> T switchScene(ControllerSupplier<T> controllerSupplier) {
        T controller = controllerFactory.getController(controllerSupplier);
        this.switchScene(controllerFactory.getViewFileFromController(controller), controller, true);
        return controller;
    }

    /***********************
     *                      *
     * Load Pane            *
     *                      *
     * @param fileName the file name
     * @param controller the controller
     * @return the parent
     */
    public Parent loadPane(String fileName, Object controller) {
        return parentLoader.loadParent(fileName, controller);
    }

    /**
     * Load pane parent.
     *
     * @param <T>                the type parameter
     * @param fileName           the file name
     * @param controllerSupplier the controller supplier
     * @return the parent
     */
    public <T> Parent loadPane(String fileName, ControllerSupplier<T> controllerSupplier) {
        return parentLoader.loadParent(fileName, controllerFactory.getController(controllerSupplier));
    }

    /**
     * Load pane parent.
     *
     * @param controller the controller
     * @return the parent
     */
    public Parent loadPane(Object controller) {
        return loadPane(controllerFactory.getViewFileFromController(controller), controller);
    }

    /**
     * Load pane parent.
     *
     * @param <T>                the type parameter
     * @param controllerSupplier the controller supplier
     * @return the parent
     */
    public <T> Parent loadPane(ControllerSupplier<T> controllerSupplier) {
        return loadPane(controllerFactory.getController(controllerSupplier));
    }

    /**
     * Gets window.
     *
     * @return the window
     */
    public Stage getWindow() {
        return window;
    }
}
