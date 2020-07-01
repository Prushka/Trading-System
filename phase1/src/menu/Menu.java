package menu;

import config.ConsoleLanguageFormatter;
import config.property.LanguageProperties;
import menu.node.*;
import menu.data.NodeRequest;
import menu.node.base.Inputable;
import menu.node.base.Node;
import menu.node.base.Skippable;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Menu {

    protected static Logger LOGGER;

    private static void setLogger(LanguageProperties lang) {
        LOGGER = Logger.getLogger(Menu.class.getName());
        LOGGER.setUseParentHandlers(false);
        LOGGER.setLevel(Level.FINEST);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.FINEST);
        handler.setFormatter(new ConsoleLanguageFormatter(lang));
        LOGGER.addHandler(handler);
    }

    private Node currentNode;

    public Node getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(Node nextNode) {
        LOGGER.log(Level.FINE, "debug.switch.node", new Object[]{currentNode, nextNode});
        this.currentNode = nextNode;
        if(currentNode instanceof Skippable){
            currentNode.display();
            setCurrentNode(currentNode.getChild());
        }
    }

    private final MasterOptionNode masterOptionNode;

    public MasterOptionNode getMasterOptionNode() {
        return masterOptionNode;
    }

    private NodeRequest request;

    // ErrorNode invalidOption = new ErrorNode("invalid.option");

    public Menu(MasterOptionNode node) {
        if (LOGGER == null) {
            setLogger(new LanguageProperties());
        }
        this.masterOptionNode = node;
        this.currentNode = node;
    }

    public void parseInput(String input) {
        if (currentNode instanceof Inputable) {
            Node nextNode = ((Inputable) currentNode).parseInput(input);
            setCurrentNode(nextNode);
        } else {
            System.err.println("node does not accept input");
        }
    }
}
