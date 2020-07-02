package menu.node;

import menu.data.Response;
import menu.node.base.Skippable;

import java.util.logging.Level;

public class ResponseNode extends Node implements Skippable {

    private final Response response;

    public ResponseNode(Builder builder) {
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
        if (response == null || response.getParas() == null || response.getParas().length == 0) {
            LOGGER.log(Level.INFO, getTranslatable());

        } else {
            LOGGER.log(Level.INFO, getTranslatable(), response.getParas());
        }
    }

    public static class Builder extends NodeBuilder<Builder> {

        private Response response;

        public Builder(String translatable) {
            super(translatable);
        }

        public Builder(Response response) {
            super(response.getTranslatable());
            this.response = response;
        }

        public Builder response(Response response) {
            this.response = response;
            return getThis();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public ResponseNode build() {
            return new ResponseNode(this);
        }
    }
}
