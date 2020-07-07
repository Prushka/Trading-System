package menu;

import config.ConsoleLanguageFormatter;
import config.property.LanguageProperties;
import menu.node.MasterOptionNode;
import menu.node.Node;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Menu {

    protected static Logger LOGGER; // consider using instance variable

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
        setCurrentNode(currentNode.parseInput(input));
    }

    public void display() {
        currentNode.display();
    }

    private void setCurrentNode(Node nextNode) {
        LOGGER.log(Level.FINE, "debug.switch.node", new Object[]{currentNode, nextNode});
        this.currentNode = nextNode;
        display();
    }

}
