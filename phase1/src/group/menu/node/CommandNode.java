package group.menu.node;

public class CommandNode extends SubmitNode {
    /**
     * Constructs a SubmitNode from a {@link Builder}
     *
     * @param builder the {@link Builder}
     */
    CommandNode(Builder builder) {
        super(builder);
    }

    @Override
    public Node parseInput(String input) {
        return this.getChild();
    }
}
