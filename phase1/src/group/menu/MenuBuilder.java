package group.menu;

import group.menu.data.PersistentRequest;
import group.menu.handler.RequestHandler;
import group.menu.node.*;
import group.menu.processor.InputPreProcessor;
import group.menu.validator.Validator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuBuilder {

    public class OptionNodeBuilder {
        private final Class<?> clazz;
        private final OperationType type;
        private final OptionNode optionNode;
        private Node currentNode;
        private SubmitNodeBuilder submitNodeBuilder;

        public OptionNodeBuilder(Class<?> clazz, OperationType type, int id, String addon) {
            this.clazz = clazz;
            this.type = type;
            optionNode = new OptionNode(getTranslatable("option", addon), id);
            currentNode = optionNode;
        }

        public OptionNodeBuilder input(String key, InputPreProcessor processor, Validator validator, ValidatingType validatingType) {
            String translatable = getTranslatable("input", key);
            InputNode inputNode = new InputNode.Builder(translatable, key)
                    .inputProcessor(processor).validator(validator, getTranslatable(validatingType.toString(), key)).build();
            currentNode.setChild(inputNode);
            currentNode = inputNode;
            return this;
        }

        public OptionNodeBuilder input(String key, Validator validator, ValidatingType validatingType) {
            return this.input(key, null, validator, validatingType);
        }

        public OptionNodeBuilder input(String key, Validator validator) {
            return this.input(key, null, validator, ValidatingType.invalid);
        }

        public OptionNodeBuilder input(String key) {
            return this.input(key, null, null, ValidatingType.invalid);
        }

        public SubmitNodeBuilder submit(String key, InputPreProcessor processor, Validator validator, ValidatingType validatingType, RequestHandler requestHandler) {
            submitNodeBuilder = new SubmitNodeBuilder(key, processor, validator, validatingType, requestHandler);
            return submitNodeBuilder;
        }

        public SubmitNodeBuilder submit(String key, Validator validator, ValidatingType validatingType, RequestHandler requestHandler) {
            return this.submit(key, null, validator, validatingType, requestHandler);
        }

        public SubmitNodeBuilder submit(String key, Validator validator, RequestHandler requestHandler) {
            return this.submit(key, validator, ValidatingType.invalid, requestHandler);
        }

        public SubmitNodeBuilder submit(String key, RequestHandler requestHandler) {
            return this.submit(key, null, requestHandler);
        }

        private String getTranslatable(String nodeType, String addon) {
            String clazzSimple = clazz.getSimpleName().replaceAll("([A-Z])", ".$1").toLowerCase();
            if (addon.length() > 0) {
                return String.format("%s.%s%s.%s", nodeType, type, clazzSimple, addon);
            }
            return String.format("%s.%s%s", nodeType, type, clazzSimple);
        }

        public class SubmitNodeBuilder {

            private final List<String> flexibleMasterPlaceHolder = new ArrayList<>();

            private final SubmitNode submitNode;

            private String masterPlaceHolder;

            public SubmitNodeBuilder(String key, InputPreProcessor processor, Validator validator, ValidatingType validatingType, RequestHandler requestHandler) {
                String translatable = getTranslatable("submit", key);
                SubmitNode submitNode = new SubmitNode.Builder(translatable, key, requestHandler, persistentRequest)
                        .inputProcessor(processor).validator(validator, getTranslatable(validatingType.toString(), key)).build();
                currentNode.setChild(submitNode);
                this.submitNode = submitNode;
            }

            public SubmitNodeBuilder master(String masterIdentifier) {
                masterPlaceHolder = masterIdentifier;
                return this;
            }

            public SubmitNodeBuilder flexibleMaster(String masterIdentifier) {
                flexibleMasterPlaceHolder.add(masterIdentifier);
                return this;
            }
        }
    }

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
            SubmitNode submitNode = optionNodeBuilder.submitNodeBuilder.submitNode;
            submitNode.setChild(masters.get(optionNodeBuilder.submitNodeBuilder.masterPlaceHolder));
            for (String flexibleMaster : optionNodeBuilder.submitNodeBuilder.flexibleMasterPlaceHolder) {
                submitNode.fillMasterPool(masters.get(flexibleMaster));
            }
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
