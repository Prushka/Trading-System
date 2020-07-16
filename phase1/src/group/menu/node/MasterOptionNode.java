package group.menu.node;

import group.menu.data.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The root node that manages branches of {@link OptionNode}.<p>
 * This node accepts input and displays on its own but using information from its branches of {@link OptionNode} in a sorted order.
 *
 * @author Dan Lyu
 * @see OptionNode
 */
public class MasterOptionNode extends RequestableNode {

    /**
     * The list of option nodes as branches
     */
    private final List<OptionNode> children;

    /**
     * Constructs a MasterOptionNode from a {@link MasterOptionNode.Builder}
     *
     * @param builder the {@link MasterOptionNode.Builder}
     */
    MasterOptionNode(Builder builder) {
        super(builder);
        children = builder.children;
        sort();
        for (OptionNode child : children) {
            child.setParent(this);
        }
    }

    /**
     * @param translatable the identifier of the current MasterOptionNode
     * @param optionNodes  the option nodes as children
     */
    public MasterOptionNode(String translatable, OptionNode... optionNodes) {
        this(new MasterOptionNode.Builder(translatable).addChild(optionNodes));
    }

    /**
     * A helper class to tell if the user input is valid as an option.
     *
     * @param input the user input
     * @return <code>true</code> if the user input is a valid option in this MasterOptionNode
     */
    private Optional<Integer> isOptionValid(String input) {
        int id;
        if (!input.matches("\\d+")) {
            return Optional.empty();
        }
        id = Integer.parseInt(input);
        if (!getChild(id).isPresent()) {
            return Optional.empty();
        }
        return Optional.of(id);
    }

    /**
     * @param input user input
     * @return the (option) node to navigate to after parsing user input
     */
    @Override
    public Node parseInput(String input) {
        Optional<OptionNode> node = getChild(input);
        if (!node.isPresent()) {
            return this;
        }
        value = input;
        Response directResponse = node.get().fetchProcessedResponse();
        if (directResponse != null) {
            directResponse.display();
        }
        return node.get();
    }

    /**
     * Displays the option nodes
     */
    @Override
    public Response fetchResponse() {
        Response.Builder responseBuilder = new Response.Builder(true);
        for (OptionNode child : children) {
            responseBuilder.response(child.fetchResponse());
        }
        return responseBuilder.build();
    }

    /**
     * MasterOptionNode accepts input.
     *
     * @return true
     */
    @Override
    boolean acceptInput() {
        return true;
    }

    /**
     * Sorts the OptionNodes in {@link #children}
     */
    private void sort() {
        children.sort(new OptionNodeComparator());
    }

    /**
     * @param id the option id
     * @return the optional option node (if exists one)
     */
    private Optional<OptionNode> getChild(int id) {
        id -= 1;
        if (id >= children.size() || id <= -1) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(children.get(id));
        }
    }

    /**
     * @param input the user input
     * @return the optional option node (if exists one)
     */
    private Optional<OptionNode> getChild(String input) {
        Optional<Integer> id = isOptionValid(input);
        if (id.isPresent()) {
            return getChild(id.get());
        } else {
            return Optional.empty();
        }
    }

    /**
     * The Builder for {@link MasterOptionNode}
     *
     * @author Dan Lyu
     */
    public static class Builder extends RequestableNodeBuilder<Builder> {

        /**
         * The list of option nodes as branches
         */
        private final List<OptionNode> children = new ArrayList<>();

        /**
         * @param translatable the identifier of the MasterOptionNode
         */
        public Builder(String translatable) {
            super(translatable, translatable);
        }

        /**
         * @return the Builder itself
         */
        @Override
        Builder getThis() {
            return this;
        }

        /**
         * Builds the {@link MasterOptionNode}
         *
         * @return the MasterOptionNode
         */
        @Override
        public MasterOptionNode build() {
            return new MasterOptionNode(this);
        }


        /**
         * Adds OptionNodes to the branches
         *
         * @param node the OptionNodes to be added as branches
         * @return the builder itself
         */
        public Builder addChild(OptionNode... node) {
            children.addAll(Arrays.asList(node));
            return this;
        }

        /**
         * Adds an option as OptionNode to the branches
         *
         * @param id           the option id
         * @param translatable the translatable identifier for the option
         * @return the builder itself
         */
        public Builder addChild(int id, String translatable) {
            this.addChild(new OptionNode.Builder(translatable).id(id).build());
            return this;
        }

    }

}
