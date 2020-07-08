package group.menu.validator;

import group.menu.node.InputNode;
import group.menu.node.Node;
import group.menu.node.ResponseNode;

public class ValidatorPair {

    private final Validator validator;

    private final ResponseNode failResponseNode;

    public ValidatorPair(Validator validator, ResponseNode successNode, Node failResponseNextNode) {
        this.validator = validator;
        this.failResponseNode = successNode;
        this.failResponseNode.setChild(failResponseNextNode);
    }

    public void setFailResponseNextNodeIfNull(InputNode inputNode){
        if(failResponseNode.getChild() == null){
            failResponseNode.setChild(inputNode);
        }
    }

    public boolean validate(String value){
        return validator.validate(value);
    }

    public ResponseNode getFailResponseNode() {
        return failResponseNode;
    }
}
