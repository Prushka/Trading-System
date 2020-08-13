package phase2.trade.view;

import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import javafx.scene.Node;
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
public class SVGFactory {

    SVGGlyph svg = null;

    private final double defaultWidth = 20;
    private final double defaultHeight = 20;

    public Pane generateSVG(String input, Paint color, double width, double height) {
        input = input.toLowerCase();
        switch (input) {
            case "user":
                svg = new SVGGlyph(
                        "M0,15.832c0-4.947,6-3.958,6-5.937a2.881,2.881,0,0,0-.546-1.979A4.532,4.532,0,0,1,4,4.453,4.245,4.245,0,0,1,8,0a4.245,4.245,0,0,1,4,4.453,4.458,4.458,0,0,1-1.474,3.463A3,3,0,0,0,10,9.895c0,1.979,6,.989,6,5.937,0,0-1.593,1.168-8,1.168S0,15.832,0,15.832Z",
                        color);
                break;
        }

        setSize(width, height);
        return svg;
    }

    public Pane generateSVG(Category input, Paint color, double width, double height) {
        try {
            switch (input) {
                case BOOK:
                    svg = extract(Main.class.getResource("/svg/book.svg"), color);
                    break;
                case MOVIE:
                    svg = new SVGGlyph(
                            "M0,15.832c0-4.947,6-3.958,6-5.937a2.881,2.881,0,0,0-.546-1.979A4.532,4.532,0,0,1,4,4.453,4.245,4.245,0,0,1,8,0a4.245,4.245,0,0,1,4,4.453,4.458,4.458,0,0,1-1.474,3.463A3,3,0,0,0,10,9.895c0,1.979,6,.989,6,5.937,0,0-1.593,1.168-8,1.168S0,15.832,0,15.832Z",
                            Color.WHITE);
                    break;
                case ELECTRONIC:
                    break;
                case VIDEO_GAME:
                    break;
                case FURNITURE:
                    break;
                case EQUIPMENT:
                    break;
                case MISCELLANEOUS:
                    break;
            }
            setSize(width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return svg;

    }

    public Pane generateSVG(Category input) {
        return generateSVG(input, Color.WHITE, this.defaultWidth, this.defaultHeight);
    }

    public Pane generateSVG(String input) {
        return generateSVG(input, Color.WHITE, this.defaultWidth, this.defaultHeight);
    }

    public void setSize(double width, double height) {
        svg.setSize(width, height);
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
