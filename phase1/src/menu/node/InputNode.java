package menu.node;

import menu.data.InputProcessor;
import menu.validator.Validator;
import menu.node.base.*;

import java.util.Optional;

public class InputNode extends RequestableNode implements Valitable {

    private final ResponseNode validateFailResponseNode;

    private final Validator validator;

    private final String defaultValue;

    private final InputProcessor processor;

    InputNode(AbstractInputNodeBuilder<?> builder) {
        super(builder);
        this.validateFailResponseNode = builder.validateFailResponseNode;
        this.validator = builder.validator;
        this.defaultValue = builder.defaultValue;
        this.processor = builder.processor;
        if (validator != null) {
            if (builder.validateFailNextNode == null) {
                validateFailResponseNode.setChild(this);
            } else {
                validateFailResponseNode.setChild(builder.validateFailNextNode);
            }
        }
    }

    public Optional<ResponseNode> validate() { // making this private will need to remove the valitable interface
        if (validator == null || validator.validate(getValue())) { // pass
            return Optional.empty();
        } else { // fail
            return Optional.of(validateFailResponseNode);
        }
    }

    @Override
    public Node parseInput(String input) {
        this.value = input;
        if (processor != null) {
            this.value = processor.process(value);
        }
        Optional<ResponseNode> error = validate();
        if (error.isPresent()) {
            return error.get();
        }
        if (input == null || input.length() == 0) {
            this.value = defaultValue;
        }
        return getChild();
    }

    // TODO: Multiple Validator & Response Node pairs
    protected abstract static class AbstractInputNodeBuilder<T extends AbstractInputNodeBuilder<T>> extends RequestableNodeBuilder<T> {

        private ResponseNode validateFailResponseNode; // the response node

        private Node validateFailNextNode; // the node after response, set to input node itself if null

        private Validator validator;

        private String defaultValue;

        private InputProcessor processor;

        public AbstractInputNodeBuilder(String translatable) {
            super(translatable);
        }

        public T inputProcessor(InputProcessor processor) {
            this.processor = processor;
            return getThis();
        }

        public T defaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return getThis();
        }

        public T validator(Validator validator, ResponseNode validateFailResponseNode) {
            this.validator = validator;
            this.validateFailResponseNode = validateFailResponseNode;
            return getThis();
        }

        public T validateSuccessNext(Node node) { // the same as child node
            child(node);
            return getThis();
        }

        // Validate fail response node is now part of validator

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
