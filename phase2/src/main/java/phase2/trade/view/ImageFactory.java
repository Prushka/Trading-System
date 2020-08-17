package phase2.trade.view;

import com.jfoenix.svg.SVGGlyph;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import phase2.trade.Main;
import phase2.trade.item.Category;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

//www.svgrepo.com/svg
public class ImageFactory {

    Node node = null;

    private final double defaultWidth = 25;
    private final double defaultHeight = 25;
    private final double defaultBrightness = 1;

    public Node generateGraphic(String filePath, double brightness, double width, double height) {
        return generateGraphic(Main.class.getResource(filePath), brightness, width, height);
    }

    // https://stackoverflow.com/questions/18124364/how-to-change-color-of-image-in-javafx
    public Node generateGraphic(URL url, double brightness, double width, double height) {
        try {
            if (url != null) {
                // String extension = url.getPath().substring(url.getPath().lastIndexOf(".")).toLowerCase();
                Image image = new Image(url.openStream(),defaultWidth,defaultHeight,true,true);

                ImageView imageView = new ImageView(image);

                ColorAdjust blackout = new ColorAdjust();
                blackout.setBrightness(brightness);

                imageView.setEffect(blackout);
                imageView.setCache(true);
                imageView.setCacheHint(CacheHint.SPEED);

                imageView.setFitWidth(width);
                imageView.setFitHeight(height);
                node = imageView;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return node;
    }

    public Node generateGraphic(String path) {
        return generateGraphic(path, defaultBrightness, this.defaultWidth, this.defaultHeight);
    }
}
