package phase2.trade;

import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import javafx.application.Application;

/**
 * The Main.
 *
 * @author Dan Lyu
 */
public class Main {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SvgImageLoaderFactory.install();
        Application.launch(TradeApplication.class, args);
    }
}