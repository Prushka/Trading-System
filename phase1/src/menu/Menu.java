package menu;

import config.ConsoleLanguageFormatter;
import config.property.LanguageProperties;
import menu.node.*;
import menu.node.base.Dumb;
import menu.node.base.Inputable;
import menu.node.Node;
import menu.node.base.Skippable;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Menu {

    protected static Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(Menu.class.getName());
        LOGGER.setUseParentHandlers(false);
        LOGGER.setLevel(Level.FINEST);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.FINEST);
        handler.setFormatter(new ConsoleLanguageFormatter(new LanguageProperties()));
        LOGGER.addHandler(handler);
    }

    private Node currentNode;

    public Menu(MasterOptionNode node) {
        this.currentNode = node;
    }

    public void parseInput(String input) {
        if (currentNode instanceof Inputable) {
            setCurrentNode(((Inputable) currentNode).parseInput(input));
        } else {
            LOGGER.log(Level.SEVERE, "node does not accept input, something went really wrong");
        }
    }

    public void display() {
        if (!(currentNode instanceof Dumb)) {
            currentNode.display();
        }
    }

    private void setCurrentNode(Node nextNode) {
        LOGGER.log(Level.FINE, "debug.switch.node", new Object[]{currentNode, nextNode});
        this.currentNode = nextNode;
        if (currentNode instanceof Skippable) {
            display();
            setCurrentNode(currentNode.getChild());
        }
    }
}
