package group.menu.node;

import java.util.logging.Level;

public class OptionNode extends Node {

    private final int id;

    OptionNode(Builder builder) {
        super(builder);
        this.id = builder.id;
    }

    public OptionNode(String translatable,int id) {
        this(new Builder(translatable).id(id));
    }

    @Override
    public void display(){

    }

    @Override
    boolean acceptInput() {
        return false;
    }

    void displaySafe() {
        LOGGER.log(Level.INFO, getTranslatable(), getId());
    }

    public int getId() {
        return id;
    }

    public static class Builder extends NodeBuilder<Builder> {

        private int id;

        public Builder(String translatable) {
            super(translatable);
        }

        public Builder id(int id) {
            this.id = id;
            return getThis();
        }

        @Override
        Builder getThis() {
            return this;
        }

        @Override
        public OptionNode build() {
            return new OptionNode(this);
        }

    }

}