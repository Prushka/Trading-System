package menu.node.base;

public abstract class RequestableNode extends Node implements Inputable {
    protected final String key;
    protected String value;

    public RequestableNode(String translatable, String key) {
        super(translatable);
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
