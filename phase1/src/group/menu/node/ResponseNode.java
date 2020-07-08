package group.menu.node;

import group.menu.data.Response;
import group.menu.data.TranslatablePair;

import java.util.logging.Level;

public class ResponseNode extends Node {

    private final Response response;

    ResponseNode(Builder builder) {
        super(builder);
        this.response = builder.response;
    }

    public ResponseNode(String translatable) {
        this(new ResponseNode.Builder(translatable));
    }

    public ResponseNode(Response response) {
        this(new ResponseNode.Builder(response));
    }


    public void display() {
        if (response == null || response.getTranslatablePairs() == null || response.getTranslatablePairs().size() == 0) {
            LOGGER.log(Level.INFO, getTranslatable());
        } else {
            for (TranslatablePair pair:response.getTranslatablePairs()) {
                LOGGER.log(Level.INFO, pair.getTranslatable(), pair.getParas());
            }
        }
    }

    @Override
    boolean acceptInput() {
        return false;
    }

    public static class Builder extends NodeBuilder<Builder> {

        private Response response;

        public Builder(String translatable) {
            super(translatable);
        }

        public Builder(Response response) {
            super("general.response.node");
            this.response = response;
        }

        public Builder response(Response response) {
            this.response = response;
            return getThis();
        }

        @Override
        Builder getThis() {
            return this;
        }

        @Override
        public ResponseNode build() {
            return new ResponseNode(this);
        }

    }

}
