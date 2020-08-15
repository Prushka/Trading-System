package phase2.trade.editor;

import java.util.ArrayList;
import java.util.List;

public class Editor<T> {

    protected final List<T> entities;
    
    public Editor(List<T> entities){
        this.entities = entities;
    }
    
    public Editor(T entity){
        entities = new ArrayList<>();
        entities.add(entity);
    }

    protected boolean isInt(String toExam) {
        return toExam.matches("[0-9]+");
    }

    protected boolean isNumber(String toExam) {
        return toExam.matches("-?\\d+(\\.\\d+)?");
    }
}
