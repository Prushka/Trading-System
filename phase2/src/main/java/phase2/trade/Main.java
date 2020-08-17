package phase2.trade;

import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import javafx.application.Application;

public class Main {

    public static void main(String[] args) {
        SvgImageLoaderFactory.install();
        Application.launch(TradeApplication.class, args);
    }
}