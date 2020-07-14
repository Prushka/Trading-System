package group.menu;

import group.menu.data.PersistentRequest;
import group.menu.handler.RequestHandler;
import group.menu.node.*;
import group.menu.processor.InputPreProcessor;
import group.menu.validator.Validator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * A class to build different menu nodes in order. This class contains inner classes for different steps needed to build a menu using nodes.<p>
 * <p>
 * This builder builds menu in a certain order in a single operation:<p>
 * Build option node, then add a bunch of input nodes that follow.<p>
 * The submit node can be at the end of all input nodes or directly follow an option node.<p>
 * Master option nodes are represented using identifiers as placeholder.<p>
 * The master option nodes are added to the submit node's master option node pool at the end of the operation.<p>
 * <p>
 * A construct method call has to be made at the end of all options in order to start a new master option branch.<p>
 * <p>
 * Finally a {@link #constructFinal()} has to be called to replace all placeholders to real nodes.<p><p>
 * <b>A simplified flow: option -> (input... -> input) -> submit -> (master...) | construct (repeat above) | constructFinal()</b><p>
 * <p>
 * The language identifier will be the combination of {OperationType, Input Key, Validating Type, Class} using this builder.<p>
 * A language properties file can be generated by {@link #generateLanguage} for all nodes that are translatable (Response / Input / Submit / Option) with "undefined" placeholders.
 * <p>
 * It is possible to build nodes without this builder and generate menu from those built nodes.<p>
 * The purpose of this builder is to simplify the node's builders' calls and the generation of language identifiers.<p>
 * This builder prevents unrelated nodes to be chained by using different inner classes as steps.<p>
 * The identifiers may not be unified without the restriction of certain operation types. This builder also simplifies the operation to build a menu as a whole by chaining method calls.
 *
 * @author Dan Lyu
 */
public class MenuBuilder {

    /**
     * The option node builder step to build an option node and its children.
     */
    public class OptionNodeBuilder {

        /**
         * The class this option node operates on, only to be used as an identifier
         */
        private final Class<?> clazz;

        /**
         * the type of the operation
         */
        private final OperationType type;

        /**
         * The root option node of this builder
         */
        private final OptionNode optionNode;

        /**
         * The current node used to chain next nodes
         */
        private Node currentNode;

        /**
         * The final SubmitNodeBuilder that ends this Builder
         */
        private SubmitNodeBuilder submitNodeBuilder;

        /**
         * @param clazz The class this option node operates on, only to be used as an identifier
         * @param type  the type of the operation
         * @param id    the id of this option
         * @param addon the addon String to be used in the language identifier
         */
        public OptionNodeBuilder(Class<?> clazz, OperationType type, int id, String addon) {
            this.clazz = clazz;
            this.type = type;
            optionNode = new OptionNode(getTranslatable("option", addon), id);
            currentNode = optionNode;
        }

        /**
         * Returns the builder itself. Adds an input node that follows the previous node.
         *
         * @param key            the key of the input value
         * @param processor      the processor used to pre-process user input
         * @param validator      the validator used to validate user input
         * @param validatingType the validating type to use if the validation fails
         * @return the builder itself
         */
        public OptionNodeBuilder input(String key, InputPreProcessor processor, Validator validator, ValidatingType validatingType) {
            String translatable = getTranslatable("input", key);
            InputNode inputNode = new InputNode.Builder(translatable, key)
                    .inputProcessor(processor).validator(validator, getTranslatable(validatingType.toString(), key)).build();
            currentNode.setChild(inputNode);
            currentNode = inputNode;
            return this;
        }

        /**
         * Overloads {@link #input(String, InputPreProcessor, Validator, ValidatingType)} with no input processor.
         *
         * @param key            the key of the input value
         * @param validator      the validator used to validate user input
         * @param validatingType the validating type to use if the validation fails
         * @return the builder itself
         */
        public OptionNodeBuilder input(String key, Validator validator, ValidatingType validatingType) {
            return this.input(key, null, validator, validatingType);
        }

        /**
         * Overloads {@link #input(String, Validator, ValidatingType)} method with an invalid validating type as default.
         *
         * @param key       the key of the input value
         * @param validator the validator used to validate user input
         * @return the builder itself
         */
        public OptionNodeBuilder input(String key, Validator validator) {
            return this.input(key, validator, ValidatingType.invalid);
        }

        /**
         * Overloads {@link #input(String, Validator)} with no validator.
         *
         * @param key the key of the input value
         * @return the builder itself
         */
        public OptionNodeBuilder input(String key) {
            return this.input(key, null);
        }

        /**
         * Returns a SubmitNodeBuilder. Will add a submit node that follows the previous node.
         *
         * @param key            the key of the input value
         * @param processor      the processor used to pre-process user input
         * @param validator      the validator used to validate user input
         * @param validatingType the validating type to use if the validation fails
         * @param requestHandler the handler to parse request and expect response
         * @return a new SubmitNodeBuilder
         */
        public SubmitNodeBuilder submit(String key, InputPreProcessor processor, Validator validator, ValidatingType validatingType, RequestHandler requestHandler) {
            submitNodeBuilder = new SubmitNodeBuilder(key, processor, validator, validatingType, requestHandler);
            return submitNodeBuilder;
        }

        /**
         * Overloads {@link #submit(String, InputPreProcessor, Validator, ValidatingType, RequestHandler)} with no input processor.
         *
         * @param key            the key of the input value
         * @param validator      the validator used to validate user input
         * @param validatingType the validating type to use if the validation fails
         * @param requestHandler the handler to parse request and expect response
         * @return a new SubmitNodeBuilder
         */
        public SubmitNodeBuilder submit(String key, Validator validator, ValidatingType validatingType, RequestHandler requestHandler) {
            return this.submit(key, null, validator, validatingType, requestHandler);
        }

        /**
         * Overloads {@link #submit(String, Validator, ValidatingType, RequestHandler)} with invalid validating type as default.
         *
         * @param key            the key of the input value
         * @param validator      the validator used to validate user input
         * @param requestHandler the handler to parse request and expect response
         * @return a new SubmitNodeBuilder
         */
        public SubmitNodeBuilder submit(String key, Validator validator, RequestHandler requestHandler) {
            return this.submit(key, validator, ValidatingType.invalid, requestHandler);
        }

        /**
         * Overloads {@link #submit(String, Validator, RequestHandler)} with no validator.
         *
         * @param key            the key of the input value
         * @param requestHandler the handler to parse request and expect response
         * @return a new SubmitNodeBuilder
         */
        public SubmitNodeBuilder submit(String key, RequestHandler requestHandler) {
            return this.submit(key, null, requestHandler);
        }

        /**
         * Returns a String identifier for language use.<p>
         * The identifier will be nodeType.operationType.operatingClassSimpleName(with dot splitting all upper case letters)(.addon).
         *
         * @param nodeType The node's type (input / submit / invalid / option)
         * @param addon    the addon String to be added, it is the key of input or the invalid type
         * @return the String identifier for language
         */
        private String getTranslatable(String nodeType, String addon) {
            String clazzSimple = clazz.getSimpleName().replaceAll("([A-Z])", ".$1").toLowerCase();
            if (addon.length() > 0) {
                return String.format("%s.%s%s.%s", nodeType, type, clazzSimple, addon);
            }
            return String.format("%s.%s%s", nodeType, type, clazzSimple);
        }

        /**
         * The submit node builder step to build a submit node and to fill its master node pool using identifiers.
         */
        public class SubmitNodeBuilder {

            private final MasterOptionNodePool masterOptionNodePool;

            private final SubmitNode submitNode;

            public SubmitNodeBuilder(String key, InputPreProcessor processor, Validator validator, ValidatingType validatingType, RequestHandler requestHandler) {
                String translatable = getTranslatable("submit", key);
                SubmitNode submitNode = new SubmitNode.Builder(translatable, key, requestHandler, persistentRequest)
                        .inputProcessor(processor).validator(validator, getTranslatable(validatingType.toString(), key)).build();
                currentNode.setChild(submitNode);
                this.submitNode = submitNode;
                masterOptionNodePool = new MasterOptionNodePool();
            }

            public SubmitNodeBuilder master(String... masterIdentifiers) {
                masterOptionNodePool.addPlaceholder(masterIdentifiers);
                return this;
            }

            public SubmitNodeBuilder succeeded(String masterIdentifier) {
                masterOptionNodePool.setSucceededPlaceHolder(masterIdentifier);
                return this;
            }

            public SubmitNodeBuilder failed(String masterIdentifier) {
                masterOptionNodePool.setFailedPlaceHolder(masterIdentifier);
                return this;
            }
        }
    }

    /**
     * The place where a global persistent request object is instantiated.<p>
     * This is the only place where a persistent request object should be instantiated.
     */
    final PersistentRequest persistentRequest = new PersistentRequest();

    public enum OperationType {
        edit, add, query, remove, verification
    }

    public enum OperationPermission {
        normal, administrator
    }

    public enum ValidatingType {
        invalid, exists, notexist
    }

    private final Map<String, OptionNodeBuilder> optionNodePoolCache = new HashMap<>();

    private final Map<String, OptionNodeBuilder> optionNodePool = new HashMap<>();

    private final Map<String, MasterOptionNode> masters = new HashMap<>();

    private MasterOptionNode entryNode;

    public OptionNodeBuilder option(Class<?> clazz, OperationType type, int id, String addon) {
        OptionNodeBuilder optionNodeBuilder = new OptionNodeBuilder(clazz, type, id, addon);
        optionNodePoolCache.put(optionNodeBuilder.optionNode.getTranslatable(), optionNodeBuilder);
        return optionNodeBuilder;
    }

    public OptionNodeBuilder option(Class<?> clazz, OperationType type, int id) {
        return this.option(clazz, type, id, "");
    }

    public MasterOptionNode construct(String masterNodeIdentifier) {
        return this.construct(masterNodeIdentifier, false);
    }


    public MasterOptionNode construct(String masterNodeIdentifier, boolean isEntryNode) {
        MasterOptionNode.Builder masterBuilder = new MasterOptionNode.Builder(masterNodeIdentifier);
        for (OptionNodeBuilder factory : optionNodePoolCache.values()) {
            masterBuilder.addChild(factory.optionNode);
        }
        optionNodePool.putAll(optionNodePoolCache);
        optionNodePoolCache.clear();
        MasterOptionNode master = masterBuilder.build();
        if (isEntryNode) entryNode = master;
        masters.put(masterNodeIdentifier, master);
        return master;
    }

    public MasterOptionNode constructFinal() {
        for (OptionNodeBuilder optionNodeBuilder : optionNodePool.values()) {
            optionNodeBuilder.submitNodeBuilder.masterOptionNodePool.feedMe(masters);
            optionNodeBuilder.submitNodeBuilder.submitNode.setMasterPool(optionNodeBuilder.submitNodeBuilder.masterOptionNodePool);
        }
        return entryNode;
    }

    public void generateLanguage(String language) { // properties is not in order thus a file writer is used, maybe we can extend Properties class

        PrintWriter writer;
        try {
            writer = new PrintWriter(new File("resources/" + language + ".properties"));
            for (OptionNodeBuilder factory : optionNodePool.values()) {
                OptionNodeIterator iterator = new OptionNodeIterator(factory.optionNode);
                while (iterator.hasNext()) {
                    Node node = iterator.next();
                    writer.println(node.getTranslatable() + "=undefined");
                }
            }
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
