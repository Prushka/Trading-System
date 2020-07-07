package menu.node;

import menu.data.InputPreProcessor;
import menu.validator.Validator;
import menu.node.base.*;
import menu.validator.ValidatorPair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InputNode extends RequestableNode implements Valitable {

    private final List<ValidatorPair> validatorPairs;

    private final String defaultValue;

    private final InputPreProcessor processor;

    InputNode(AbstractInputNodeBuilder<?> builder) {
        super(builder);
        this.validatorPairs = builder.validatorPairs;
        this.defaultValue = builder.defaultValue;
        this.processor = builder.processor;
        for (ValidatorPair validatorPair : validatorPairs) {
            validatorPair.setFailResponseNextNodeIfNull(this);
        }
    }

    public Optional<ResponseNode> validate() {
        if (validatorPairs != null) {
            for (ValidatorPair validatorPair : validatorPairs) {
                if (!validatorPair.validate(value)) {
                    return Optional.of(validatorPair.getFailResponseNode());
                }
            }
        }
        return Optional.empty();
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

    protected abstract static class AbstractInputNodeBuilder<T extends AbstractInputNodeBuilder<T>> extends RequestableNodeBuilder<T> {

        private final List<ValidatorPair> validatorPairs;

        private String defaultValue;

        private InputPreProcessor processor;

        public AbstractInputNodeBuilder(String translatable) {
            super(translatable);
            validatorPairs = new ArrayList<>();
        }

        public T inputProcessor(InputPreProcessor processor) {
            this.processor = processor;
            return getThis();
        }

        public T defaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return getThis();
        }

        public T validator(Validator validator, ResponseNode validateFailResponseNode) { // fail next node = this
            return validator(validator, validateFailResponseNode, null);
        }

        public T validator(Validator validator, ResponseNode validateFailResponseNode, Node validateFailNextNode) {
            this.validatorPairs.add(new ValidatorPair(validator, validateFailResponseNode, validateFailNextNode));
            return getThis();
        }

        public T validateSuccessNext(Node node) { // the same as child node
            child(node);
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
