package group.menu;

import group.config.ConsoleLanguageFormatter;
import group.config.property.LanguageProperties;
import group.menu.node.InputNode;
import group.menu.node.MasterOptionNode;
import group.menu.node.Node;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The entry of nodes.<p>
 * The intended exposed methods to be used to operate nodes.<p>
 * Contains operations to navigate menu levels, display and parse input
 *
 * @author Dan Lyu
 */
public class Menu {

    /**
     * The
     */
    static Logger LOGGER; // consider using instance variable

    static {
        LOGGER = Logger.getLogger(Menu.class.getName());
        LOGGER.setUseParentHandlers(false);
        LOGGER.setLevel(Level.INFO);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.INFO);
        handler.setFormatter(new ConsoleLanguageFormatter(new LanguageProperties()));
        LOGGER.addHandler(handler);
    }

    private Node currentNode;

    public Menu(MasterOptionNode node) {
        this.currentNode = node;
        display();
    }

    private void goBack() {
        setCurrentNode(currentNode.traceBack());
    }

    private void goToNext() {
        // These restrictions only apply if the user goes back to a node that he/she already inputs something and wants to get back to the next node without actually inputting anything in that previous node
        // Implement this in a polymorphism way may need to add fields and methods to all node classes. I don't know!
        if (currentNode.getChild() != null && currentNode instanceof InputNode && currentNode.getChild() instanceof InputNode) {
            if (currentNode.getValue() != null && currentNode.getValue().length() > 0 && !((InputNode) currentNode).validate().isPresent()) {
                LOGGER.log(Level.INFO,"current.value.tip");
                setCurrentNode(currentNode.getChild());
            }
        }
    }

    public void parseInput(String input) {
        if (input.equalsIgnoreCase("back")) {
            goBack();
        } else if (input.equalsIgnoreCase("next")) {
            goToNext();
        } else {
            setCurrentNode(currentNode.parseInput(input).traceForward());
        }
        display();
    }

    private void display() {
        currentNode.display();
    }

    private void setCurrentNode(Node nextNode) {
        LOGGER.log(Level.FINE, "debug.switch.node", new Object[]{currentNode, nextNode});
        this.currentNode = nextNode;
    }

    public void displayInitial() {
        LOGGER.log(Level.INFO, "init.tip");
    }
}
