package menu.node;

import menu.node.base.Validator;
import menu.node.base.Node;
import menu.node.base.RequestableNode;
import menu.node.base.Valitable;

import java.util.Optional;

public class InputNode extends RequestableNode implements Valitable {

    private final ErrorNode errorNode;
    private final Validator validator;

    public InputNode(String translatable, String key, ErrorNode errorNode, Validator validator) {
        super(translatable, key);
        this.errorNode = errorNode;
        this.validator = validator;
    }

    public InputNode(String translatable, String key) { // error node and validator must be passed in together
        this(translatable, key, null, null);
    }

    public Optional<ErrorNode> validate() {
        if (validator == null || validator.validate(getValue())) {
            System.out.println("validated");
            return Optional.empty();
        } else {
            System.out.println("invalid");
            return Optional.of(errorNode);
        }
    }

    public Node parseInput(String input) {
        this.value = input;
        Optional<ErrorNode> error = validate();
        if(error.isPresent()){
            return error.get();
        }
        return getChild();
    }

}
