package menu.node;

import menu.node.base.Inputable;
import menu.node.base.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MasterOptionNode extends Node implements Inputable {

    private final List<OptionNode> children = new ArrayList<>();

    public MasterOptionNode(String translatable) {
        super(translatable);
    }

    public MasterOptionNode sort() {
        children.sort(new OptionNodeComparator());
        return this;
    }

    public MasterOptionNode addChild(OptionNode node) {
        children.add(node);
        return this;
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
            return new ResponseNode("invalid.option").setParent(this);
        }
        return node.get();
    }

    @Override
    public void display() {
        for (OptionNode child : children) {
            child.displaySafe();
        }
    }

}
