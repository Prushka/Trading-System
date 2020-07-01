package menu.node;

import menu.node.base.Validator;
import menu.node.base.Node;
import menu.node.base.RequestableNode;
import menu.node.base.Valitable;

import java.util.Optional;

public class InputNode extends RequestableNode implements Valitable {

    private ResponseNode validateNode;
    private Validator validator;

    public InputNode(String translatable, String key, ResponseNode validateNode, Validator validator) {
        super(translatable, key);
        this.validateNode = validateNode;
        this.validator = validator;
    }

    public InputNode(String translatable, String key) { // error node and validator must be passed in together
        this(translatable, key, null, null);
    }

    public InputNode validateNode(ResponseNode responseNode, Validator validator){
        this.validateNode = responseNode;
        this.validator = validator;
        return this;
    }

    public Optional<ResponseNode> validate() {
        if (validator == null || validator.validate(getValue())) {
            return Optional.empty();
        } else {
            return Optional.of(validateNode);
        }
    }

    public Node parseInput(String input) {
        this.value = input;
        Optional<ResponseNode> error = validate();
        if(error.isPresent()){
            return error.get();
        }
        return getChild();
    }

}
