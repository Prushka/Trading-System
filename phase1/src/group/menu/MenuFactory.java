package group.menu;

import group.menu.handler.RequestHandler;
import group.menu.node.*;
import group.menu.processor.InputPreProcessor;
import group.menu.validator.Validator;

import java.util.HashMap;
import java.util.Map;

public class MenuFactory {

    public static class OptionNodeFactory {
        private final Class<?> clazz;
        private final OperationType type;
        private final OptionNode optionNode;

        private final Map<Node, String> masterPlaceHolder = new HashMap<>();

        private final Map<String, InputNode> inputNodePool = new HashMap<>();

        private Node currentNode;

        public OptionNodeFactory(Class<?> clazz, OperationType type, int id, String addon) {
            this.clazz = clazz;
            this.type = type;
            optionNode = new OptionNode(getTranslatable("option", addon), id);
            currentNode = optionNode;
        }

        public OptionNodeFactory input(String key, InputPreProcessor processor, Validator validator, ValidatingType validatingType) {
            String translatable = getTranslatable("input", key);
            InputNode inputNode = new InputNode.Builder(translatable, key)
                    .inputProcessor(processor).validator(validator, getTranslatable(validatingType.toString(), key)).build();
            inputNodePool.put(translatable, inputNode);
            currentNode.setChild(inputNode);
            currentNode = inputNode;
            return this;
        }

        public OptionNodeFactory input(String key, Validator validator, ValidatingType validatingType) {
            return this.input(key, null, validator, validatingType);
        }

        public OptionNodeFactory input(String key, Validator validator) {
            return this.input(key, null, validator, ValidatingType.invalid);
        }

        public OptionNodeFactory input(String key) {
            return this.input(key, null, null, ValidatingType.invalid);
        }

        public OptionNodeFactory submit(String key, InputPreProcessor processor, Validator validator, ValidatingType validatingType, RequestHandler requestHandler) {
            String translatable = getTranslatable("input", key);
            SubmitNode submitNode = new SubmitNode.Builder(translatable, key, requestHandler)
                    .inputProcessor(processor).validator(validator, getTranslatable(validatingType.toString(), key)).build();
            inputNodePool.put(translatable, submitNode);
            currentNode.setChild(submitNode);
            currentNode = submitNode;
            return this;
        }

        public OptionNodeFactory submit(String key, Validator validator, ValidatingType validatingType, RequestHandler requestHandler) {
            return this.submit(key, null, validator, validatingType, requestHandler);
        }

        public OptionNodeFactory submit(String key, Validator validator, RequestHandler requestHandler) {
            return this.submit(key, validator, ValidatingType.invalid, requestHandler);
        }

        public OptionNodeFactory submit(String key, RequestHandler requestHandler) {
            return this.submit(key, null, requestHandler);
        }

        public OptionNodeFactory master(String masterIdentifier) {
            masterPlaceHolder.put(currentNode, masterIdentifier);
            return this;
        }

        private String getTranslatable(String nodeType, String addon) {
            String clazzSimple = clazz.getSimpleName().toLowerCase();
            if (addon.length() > 0) {
                return String.format("%s.%s.%s.%s", nodeType, type, clazzSimple, addon);
            }
            return String.format("%s.%s.%s", nodeType, type, clazzSimple);
        }
    }

    public enum OperationType {
        edit, add, query, remove, verification
    }

    public enum OperationPermission {
        normal, administrator
    }

    public enum ValidatingType {
        invalid, exists
    }


    private final Map<String, OptionNodeFactory> optionNodePoolCache = new HashMap<>();

    private final Map<String, OptionNodeFactory> optionNodePool = new HashMap<>();

    private final Map<String, MasterOptionNode> masters = new HashMap<>();


    public OptionNodeFactory option(Class<?> clazz, OperationType type, int id, String addon) {
        OptionNodeFactory factory = new OptionNodeFactory(clazz, type, id, addon);
        optionNodePoolCache.put(factory.optionNode.getTranslatable(), factory);
        return factory;
    }

    public OptionNodeFactory option(Class<?> clazz, OperationType type, int id) {
        return this.option(clazz, type, id, "");
    }

    public MasterOptionNode construct(String masterNodeIdentifier) {
        MasterOptionNode.Builder masterBuilder = new MasterOptionNode.Builder(masterNodeIdentifier);
        for (OptionNodeFactory factory : optionNodePoolCache.values()) {
            masterBuilder.addChild(factory.optionNode);
        }
        optionNodePool.putAll(optionNodePoolCache);
        optionNodePoolCache.clear();
        MasterOptionNode master = masterBuilder.build();
        masters.put(masterNodeIdentifier, master);
        return master;
    }

    public void constructFinal() {
        for (OptionNodeFactory factory : optionNodePool.values()) {
            for (Map.Entry<Node, String> entry : factory.masterPlaceHolder.entrySet()) {
                entry.getKey().setChild(masters.get(entry.getValue()));
            }
        }
    }


}
