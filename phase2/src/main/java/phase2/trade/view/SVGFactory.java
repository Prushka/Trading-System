package phase2.trade.view;

import com.jfoenix.svg.SVGGlyph;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import phase2.trade.item.Category;

public class SVGFactory {

    SVGGlyph svg = null;

    private final double defaultWidth = 20;
    private final double defaultHeight = 20;

    public Pane generateSVG(String input, double width, double height){
        input = input.toLowerCase();
        switch (input){
            case "user":
                svg = new SVGGlyph(
                        "M0,15.832c0-4.947,6-3.958,6-5.937a2.881,2.881,0,0,0-.546-1.979A4.532,4.532,0,0,1,4,4.453,4.245,4.245,0,0,1,8,0a4.245,4.245,0,0,1,4,4.453,4.458,4.458,0,0,1-1.474,3.463A3,3,0,0,0,10,9.895c0,1.979,6,.989,6,5.937,0,0-1.593,1.168-8,1.168S0,15.832,0,15.832Z",
                        Color.WHITE);
                break;
        }

        setSize(width, height);
        return svg;
    }

    public Pane generateSVG(Category input, double width, double height){

        switch (input){
            case BOOK:
                svg = new SVGGlyph("M.22,18.28a.75.75,0,0,1,0-1.06l5.4-5.4a7.258,7.258,0,1,1,1.061,1.061l-5.4,5.4a.75.75,0,0,1-1.061,0ZM5.5,7.25A5.75,5.75,0,1,0,11.25,1.5,5.757,5.757,0,0,0,5.5,7.25Z",
                        Color.WHITE);
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
        return svg;

    }

    public Pane generateSVG(Category input){
        return generateSVG(input, this.defaultWidth, this.defaultHeight);
    }

    public Pane generateSVG(String input){
        return generateSVG(input, this.defaultWidth, this.defaultHeight);
    }

    public void setSize(double width, double height){
        svg.setSize(width, height);
    }
}
