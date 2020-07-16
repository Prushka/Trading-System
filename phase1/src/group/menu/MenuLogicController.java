package group.menu;

import group.config.LoggerFactory;
import group.menu.data.Response;
import group.menu.node.InputNode;
import group.menu.node.MasterOptionNode;
import group.menu.node.Node;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The entry of nodes.<p>
 * The intended exposed methods to be used to operate menu nodes.<p>
 * Contains operations to navigate menu levels, display and parse input
 *
 * @author Dan Lyu
 */
public class MenuLogicController {

    /**
     * The logger to log information from the Menu,
     * instantiated using a single instance {@link java.util.logging.FileHandler} with Level.ALL
     * and a {@link ConsoleHandler} with Level.INFO
     */
    static final Logger LOGGER = new LoggerFactory(MenuLogicController.class).getConfiguredLogger();

    /**
     * The current menu node where the user is at
     */
    private Node currentNode;

    /**
     * Constructs a menu.
     *
     * @param node the entry {@link MasterOptionNode}
     */
    public MenuLogicController(MasterOptionNode node) {
        this.currentNode = node;
    }

    /**
     * Goes up one menu level.
     * Checked if the node is backable.
     */
    private void goBack() {
        setCurrentNode(currentNode.traceBack());
    }

    /**
     * Goes down one menu level.
     * Checked if the user can skip the current node.
     */
    private void goToNext() {
        // These restrictions only apply if the user goes back to a node that he/she already inputs something and wants to get back to the next node without actually inputting anything in that previous node
        // Implement this in a polymorphism way may need to add fields and methods to all node classes. I don't know!
        if (currentNode.getChild() != null && currentNode instanceof InputNode && currentNode.getChild() instanceof InputNode) {
            if (currentNode.getValue() != null && currentNode.getValue().length() > 0 && ((InputNode) currentNode).validate().success()) {
                setCurrentNode(currentNode.getChild());
            }
        }
    }

    /**
     * Parses the user input.
     * The node has the responsibility to parse the input further.
     *
     * @param input user input
     */
    public void parseInput(String input) {
        if (input.equalsIgnoreCase("back")) {
            goBack();
        } else if (input.equalsIgnoreCase("next")) {
            goToNext();
        } else {
            setCurrentNode(currentNode.parseInput(input).traceForward());
        }
        if (currentNode.isSkippable()) {
            parseInput("");
        } else {
            currentNode.fetchResponse().display();
        }
    }

    public Response fetchInitialResponse() {
        return new Response.Builder(true).translatable("init.tip").response(currentNode.fetchResponse()).build();
    }

    /**
     * Sets the current node to the next node.
     *
     * @param nextNode the next node to be set
     */
    private void setCurrentNode(Node nextNode) {
        LOGGER.log(Level.FINE, "debug.switch.node", new Object[]{currentNode, nextNode});
        this.currentNode = nextNode;
    }
}
