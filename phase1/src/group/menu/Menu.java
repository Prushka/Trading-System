package group.menu;

import group.config.ConsoleLanguageFormatter;
import group.config.property.LanguageProperties;
import group.menu.node.MasterOptionNode;
import group.menu.node.Node;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Menu {

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

    public void parseInput(String input) {
        if (input.equalsIgnoreCase("back")) {
            setCurrentNode(currentNode.traceBack());
        }else {
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
        LOGGER.log(Level.INFO,"init.tip");
    }
}
