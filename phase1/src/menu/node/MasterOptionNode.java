package menu.node;

import menu.node.base.Inputable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MasterOptionNode extends Node implements Inputable {

    private final List<OptionNode> children;

    public MasterOptionNode(Builder builder) {
        super(builder);
        children = builder.children;
        sort();
    }

    public void sort() {
        children.sort(new OptionNodeComparator());
    }

    public Optional<OptionNode> getChild(int id) {
        id -= 1;
        if (id >= children.size()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(children.get(id));
        }
    }

    public Optional<OptionNode> getChild(String input) {
        Optional<Integer> id = isOptionValid(input);
        if (id.isPresent()) {
            return getChild(id.get());
        } else {
            return Optional.empty();
        }
    }

    private Optional<Integer> isOptionValid(String input) {
        int id;
        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException | NullPointerException e) {
            return Optional.empty();
        }
        if (input.length() == 0 || !getChild(id).isPresent()) {
            return Optional.empty();
        }
        return Optional.of(id);
    }

    @Override
    public Node parseInput(String input) {
        Optional<OptionNode> node = getChild(input);
        if (!node.isPresent()) {
            return new ResponseNode.Builder("invalid.option").build();
        }
        return node.get();
    }

    @Override
    public void display() {
        for (OptionNode child : children) {
            child.displaySafe();
        }
    }

    public static class Builder extends NodeBuilder<Builder> {

        private final List<OptionNode> children = new ArrayList<>();

        public Builder(String translatable) {
            super(translatable);
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public MasterOptionNode build() {
            return new MasterOptionNode(this);
        }


        public Builder addChild(OptionNode... node) {
            children.addAll(Arrays.asList(node));
            return this;
        }

        public Builder addChild(int id, String translatable) {
            this.addChild(new OptionNode.Builder(translatable).id(id).build());
            return this;
        }
    }
}
