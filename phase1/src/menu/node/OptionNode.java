package menu.node;

import menu.node.base.Skippable;

import java.util.logging.Level;

public class OptionNode extends Node implements Skippable {

    private final int id;

    OptionNode(Builder builder) {
        super(builder);
        this.id = builder.id;
    }

    @Override
    public void display() {

    }

    public int getId() {
        return id;
    }

    protected void displaySafe() {
        LOGGER.log(Level.INFO, getTranslatable(), getId());
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
        protected Builder getThis() {
            return this;
        }

        @Override
        public OptionNode build() {
            return new OptionNode(this);
        }

    }

}
