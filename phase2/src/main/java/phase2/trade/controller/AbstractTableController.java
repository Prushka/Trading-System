package phase2.trade.controller;

import javafx.fxml.Initializable;
import phase2.trade.editor.EmptyEditor;

import java.util.List;

@ControllerProperty(viewFile = "general_table_view.fxml")
public abstract class AbstractTableController<T> extends AbstractEditableTableController<T, EmptyEditor<T>> implements Initializable {

    public AbstractTableController(ControllerResources controllerResources, boolean ifMultipleSelection) {
        super(controllerResources, ifMultipleSelection, false, EmptyEditor::new);
    }

    public void updateEntity(List<T> entities){
    }

}
