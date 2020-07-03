package menu.node;

import menu.validator.Validator;
import menu.node.base.*;

import java.util.Optional;

public class InputNode extends RequestableNode implements Valitable {

    private final ResponseNode validateFailResponseNode;
    private final Validator validator;

    public InputNode(AbstractInputNodeBuilder<?> builder) {
        super(builder);
        this.validateFailResponseNode = builder.validateFailResponseNode;
        this.validator = builder.validator;
        if (builder.validateFailNextNode == null) {
            validateFailResponseNode.setChild(this);
        }else{
            validateFailResponseNode.setChild(builder.validateFailNextNode);
        }
    }

    public Optional<ResponseNode> validate() {
        if (validator == null || validator.validate(getValue())) { // pass
            return Optional.empty();
        } else { // fail
            return Optional.of(validateFailResponseNode);
        }
    }

    public Node parseInput(String input) {
        this.value = input;
        Optional<ResponseNode> error = validate();
        if (error.isPresent()) {
            return error.get();
        }
        return getChild();
    }

    // TODO: Multiple Validator & Response Node pairs
    protected abstract static class AbstractInputNodeBuilder<T extends AbstractInputNodeBuilder<T>> extends RequestableNodeBuilder<T> {

        private ResponseNode validateFailResponseNode;
        private Node validateFailNextNode;
        private Validator validator;

        public AbstractInputNodeBuilder(String translatable) {
            super(translatable);
        }

        public T validator(Validator validator) {
            this.validator = validator;
            return getThis();
        }

        public T validateSuccessNext(Node node) {
            child(node);
            return getThis();
        }

        public T validateFailResponse(ResponseNode node) {
            validateFailResponseNode = node;
            return getThis();
        }

        public T validateFailNext(Node node) {
            validateFailNextNode = node;
            return getThis();
        }
    }

    public static class Builder extends AbstractInputNodeBuilder<Builder> {
        public Builder(String translatable) {
            super(translatable);
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public InputNode build() {
            return new InputNode(this);
        }
    }

}
