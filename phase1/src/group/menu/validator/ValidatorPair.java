package group.menu.validator;

import group.menu.node.InputNode;
import group.menu.node.Node;
import group.menu.node.ResponseNode;

/**
 * The pair of validator and ResponseNode.<br>
 * If user input passes the {@link #validator}, the {@link #failResponseNode} won't be used.<br>
 * If it didn't, the {@link #failResponseNode} will be the place where failed result is stored.<br>
 *
 * @author Dan Lyu
 * @see ResponseNode
 */
public class ValidatorPair {

    /**
     * The validator
     */
    private final Validator validator;

    /**
     * The response node to be used if the validation didn't pass
     */
    private final ResponseNode failResponseNode;

    /**
     * @param validator            {@link #validator}
     * @param failResponseNode     {@link #failResponseNode}
     * @param failResponseNextNode the node after the failResponseNode to ask the user to repeat his/her input
     */
    public ValidatorPair(Validator validator, ResponseNode failResponseNode, Node failResponseNextNode) {
        this.validator = validator;
        this.failResponseNode = failResponseNode;
        this.failResponseNode.setChild(failResponseNextNode);
    }

    /**
     * If the failResponseNextNode is not set when constructing the {@link #failResponseNode}.<br>
     * The input node itself will be set to be the next node
     *
     * @param inputNode the input node
     */
    public void setFailResponseNextNodeIfNull(InputNode inputNode) {
        if (failResponseNode.getChild() == null) {
            failResponseNode.setChild(inputNode);
        }
    }

    /**
     * @param value user input
     * @return if user input passes the validation
     */
    public boolean validate(String value) {
        if(validator == null) return true;
        return validator.validate(value);
    }

    /**
     * @return {@link #failResponseNode}
     */
    public ResponseNode getFailResponseNode() {
        return failResponseNode;
    }
}
