package menu.node;

import menu.data.Response;
import menu.node.base.Node;
import menu.node.base.Skippable;

import java.util.logging.Level;

public class ResponseNode extends Node implements Skippable {

    private Response response;
    private Object[] paras;

    public ResponseNode(String translatable) {
        super(translatable);
    }

    public ResponseNode(Response response) {
        super(response.getTranslatable());
        this.paras = response.getParas();
    }

    public void display() {
        LOGGER.log(Level.INFO, getTranslatable(), paras);
    }
}
