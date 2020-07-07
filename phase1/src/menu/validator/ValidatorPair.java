package menu.validator;

import menu.node.InputNode;
import menu.node.Node;
import menu.node.ResponseNode;

public class ValidatorPair {

    private final Validator validator;

    private final ResponseNode failResponseNode;

    private Node failResponseNextNode;

    public ValidatorPair(Validator validator, ResponseNode successNode, Node failResponseNextNode) {
        this.validator = validator;
        this.failResponseNode = successNode;
        this.failResponseNextNode = failResponseNextNode;
    }

    public void setFailResponseNextNodeIfNull(InputNode inputNode){
        if(this.failResponseNextNode == null){
            this.failResponseNextNode = inputNode;
        }
    }

    public boolean validate(String value){
        return validator.validate(value);
    }

    public Node getFailResponseNextNode() {
        return failResponseNextNode;
    }

    public ResponseNode getFailResponseNode() {
        return failResponseNode;
    }
}
