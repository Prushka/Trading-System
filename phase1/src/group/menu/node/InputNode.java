package group.menu.node;

import group.menu.data.Response;
import group.menu.processor.InputPreProcessor;
import group.menu.validator.Validator;
import group.menu.validator.ValidatorPair;

import java.util.ArrayList;
import java.util.List;

/**
 * The Node that handles user input.<p>
 * This node accepts input and displays on its own.
 *
 * @author Dan Lyu
 */
public class InputNode extends RequestableNode {

    /**
     * A list of paris that contain validators and ResponseNode.
     * Used to validate user input.
     */
    private final List<ValidatorPair> validatorPairs;

    /**
     * The default value of the InputNode (if no user input)
     */
    private final String defaultValue;

    /**
     * The processor used to process raw user input before validation.
     * The precessed text will be saved as value.
     */
    private final InputPreProcessor processor;

    /**
     * Constructs an InputNode from a {@link AbstractInputNodeBuilder}
     *
     * @param builder the {@link AbstractInputNodeBuilder}
     */
    InputNode(AbstractInputNodeBuilder<?> builder) {
        super(builder);
        this.validatorPairs = builder.validatorPairs;
        this.defaultValue = builder.defaultValue;
        this.processor = builder.processor;
    }

    /**
     * Validates user input in the {@link #value} using {@link #validatorPairs}.
     * The first encountered error ResponseNode in the pair will be returned.
     * If no validators are available or all validations are passed, it returns an empty optional object.
     *
     * @return <code>true</code> if user input passes validation or there's no available validators
     */
    public Response validate() {
        if (validatorPairs != null) {
            for (ValidatorPair validatorPair : validatorPairs) {
                if (!validatorPair.validate(value)) {
                    return validatorPair.getFailResponse();
                }
            }
        }
        return new Response(true);
    }

    /**
     * Preprocesses user input using {@link #processor}. The processed result will become the {@link #value}
     *
     * @param input user input
     */
    void inputPreProcessing(String input) {
        this.value = input;
        if (input == null || input.length() == 0) {
            this.value = defaultValue;
        }
        if (processor != null) {
            this.value = processor.process(value);
        }
    }

    @Override
    public Response fetchResponse() {
        Response response = validate();
        if (response.success()) {
            return new Response.Builder(true).translatable(getTranslatable()).build();
        } else {
            return response;
        }
    }

    /**
     * @param input user input
     * @return the node to navigate to after parsing user input
     */
    public Node parseInput(String input) {
        inputPreProcessing(input);
        if (!validate().success()) {
            return getChild();
        }
        return this;
    }

    /**
     * The abstract Builder for an input Node.
     *
     * @param <T> The type of a sub Node Builder that extends this current Node Builder
     * @author Dan Lyu
     */
    abstract static class AbstractInputNodeBuilder<T extends AbstractInputNodeBuilder<T>> extends RequestableNodeBuilder<T> {

        /**
         * A list of paris that contain validators and ResponseNode.
         * Used to validate user input.
         */
        private final List<ValidatorPair> validatorPairs;

        /**
         * The default value of the InputNode (if no user input)
         */
        private String defaultValue;

        /**
         * The processor used to process raw user input before validation.
         * The precessed text will be saved as value.
         */
        private InputPreProcessor processor;

        /**
         * @param translatable the translatable identifier
         * @param key          the key to map user input
         */
        public AbstractInputNodeBuilder(String translatable, String key) {
            super(translatable, key);
            validatorPairs = new ArrayList<>();
        }

        /**
         * @param processor the processor used to process raw user input before validation.
         * @return the builder itself
         */
        public T inputProcessor(InputPreProcessor processor) {
            this.processor = processor;
            return getThis();
        }

        /**
         * @param defaultValue The default value of the InputNode (if no user input)
         * @return the builder itself
         */
        public T defaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return getThis();
        }

        /**
         * Puts the validator and translatable into the Builder.
         * The validateFailNextNode will be the InputNode itself using this method.<p>
         * Which means user will repeat the same input until the validation passes.
         *
         * @param validator          the validator used to validate user input
         * @param translatableFailed the failed translatable identifier, a ResponseNode will be constructed after this
         * @return the builder itself
         */
        public T validator(Validator validator, String translatableFailed) {
            return validator(validator, new Response(false, translatableFailed));
        }

        /**
         * Puts the validator, failed ResponseNode and the node after the failedResponseNode into the Builder.
         *
         * @param validator    the validator used to validate user input
         * @param failResponse the failed Response to use when validation fails
         * @return the builder itself
         */
        public T validator(Validator validator, Response failResponse) {
            this.validatorPairs.add(new ValidatorPair(validator, failResponse));
            return getThis();
        }

        /**
         * Works the same as setting the node as the child.
         *
         * @param node the node to navigate to when validation passes
         * @return the builder itself
         */
        public T validateSuccessNext(Node node) { // the same as child node
            child(node);
            return getThis();
        }

    }

    /**
     * The Builder for an input Node.
     *
     * @author Dan Lyu
     */
    public static class Builder extends AbstractInputNodeBuilder<Builder> {

        /**
         * @param translatable The translatable identifier
         * @param key          the key to map user input
         */
        public Builder(String translatable, String key) {
            super(translatable, key);
        }

        /**
         * @return the builder itself
         */
        @Override
        Builder getThis() {
            return this;
        }

        /**
         * Builds an InputNode
         *
         * @return the built InputNode
         */
        @Override
        public InputNode build() {
            return new InputNode(this);
        }

    }

}
