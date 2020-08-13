package phase2.trade.view;

import com.jfoenix.svg.SVGGlyph;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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

    private final double defaultWidth = 20;
    private final double defaultHeight = 20;

    public Node generateSVG(String filePath, Paint color, double width, double height) {
        return generateSVG(Main.class.getResource(filePath), color, width, height);
    }

    // The reason why svg is not widely used is because the color and multiple paths don't render correctly.
    // And I do not have the time to find an appropriate library.
    // Currently single path svg can be used without problem.
    public Node generateSVG(URL url, Paint color, double width, double height) {
        try {
            if (url != null) {
                String extension = url.getPath().substring(url.getPath().lastIndexOf(".")).toLowerCase();
                switch (extension) {
                    case ".svg":
                        SVGGlyph svgGlyph = extract(url, color);
                        svgGlyph.setSize(width, height);
                        node = svgGlyph;
                        break;
                    default:
                        ImageView imageView = new ImageView(new Image(url.openStream()));
                        imageView.setFitWidth(width);
                        imageView.setFitHeight(height);
                        node = imageView;
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return node;
    }

    public Node generateSVG(Category input) {
        return generateSVG(input.resourcePath, Color.WHITE, this.defaultWidth, this.defaultHeight);
    }

    public Node generateSVG(String general) {
        String path = null;
        switch (general){
            case "user":
                path = "/svg/user.svg";
                break;
        }
        return generateSVG(path, Color.WHITE, this.defaultWidth, this.defaultHeight);
    }


    // the methods below are modified from SVGGlyphLoader
    // the loader requires the filename to have a specific format
    private SVGGlyph extract(URL url, Paint paint) throws IOException {
        return new SVGGlyph(extractSvgPath(getStringFromInputStream(url.openStream())), paint);
    }

    private String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    private String extractSvgPath(String svgString) {
        return svgString.replaceFirst(".*d=\"", "").replaceFirst("\".*", "");
    }

}
