package phase2.trade.editor;

import phase2.trade.config.ConfigBundle;

import java.util.ArrayList;
import java.util.List;

public class Editor<T> {

    protected final List<T> entities;
    protected final ConfigBundle configBundle;

    public Editor(List<T> entities, ConfigBundle configBundle){
        this.entities = entities;
        this.configBundle = configBundle;
    }

    protected boolean isInt(String toExam) {
        return toExam.matches("[0-9]+");
    }

    protected boolean isNumber(String toExam) {
        return toExam.matches("-?\\d+(\\.\\d+)?");
    }
}
