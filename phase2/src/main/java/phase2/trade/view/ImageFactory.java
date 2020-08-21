package phase2.trade.view;

import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import phase2.trade.Main;

import java.io.IOException;
import java.net.URL;

/**
 * The Image factory.
 *
 * @author Dan Lyu
 */
public class ImageFactory {

    /**
     * The Node.
     */
    Node node = null;

    private final double defaultWidth = 25;
    private final double defaultHeight = 25;
    private final double defaultBrightness = 1;
    private final boolean defaultSmoothing = true;

    /**
     * Generate graphic node.
     *
     * @param filePath   the file path
     * @param brightness the brightness
     * @param smoothing  the smoothing
     * @param width      the width
     * @param height     the height
     * @return the node
     */
    public Node generateGraphic(String filePath, double brightness, boolean smoothing, double width, double height) {
        return generateGraphic(Main.class.getResource(filePath), brightness, smoothing, width, height);
    }

    /**
     * Generate graphic node.
     *
     * @param url        the url
     * @param brightness the brightness
     * @param smoothing  the smoothing
     * @param width      the width
     * @param height     the height
     * @return the node
     */
// https://stackoverflow.com/questions/18124364/how-to-change-color-of-image-in-javafx
    public Node generateGraphic(URL url, double brightness, boolean smoothing, double width, double height) {
        try {
            if (url != null) {
                // String extension = url.getPath().substring(url.getPath().lastIndexOf(".")).toLowerCase();
                Image image = new Image(url.openStream(), width, height, true, smoothing);

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

    /**
     * Generate graphic node.
     *
     * @param path the path
     * @return the node
     */
    public Node generateGraphic(String path) {
        return generateGraphic(path, defaultBrightness, this.defaultSmoothing, this.defaultWidth, this.defaultHeight);
    }
}
