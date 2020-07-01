package menu;

import menu.node.*;
import menu.data.NodeRequest;
import menu.node.base.Inputable;
import menu.node.base.Node;

public class Menu {

    private Node currentNode;

    public Node getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(Node nextNode) {
        System.out.println("Current node switched from " + currentNode + " to " + nextNode);
        this.currentNode = nextNode;
    }

    private final MasterOptionNode masterOptionNode;

    public MasterOptionNode getMasterOptionNode() {
        return masterOptionNode;
    }

    private NodeRequest request;

    // ErrorNode invalidOption = new ErrorNode("invalid.option");


    public static void main(String[] args) { // test

        OptionNode registerNode = new OptionNode(1, "register");
        OptionNode loginAccountNode = new OptionNode(2, "login");

        MasterOptionNode accountMasterNode = new MasterOptionNode("account.master.node").addChild(loginAccountNode).addChild(registerNode).sort();

        InputNode loginInputNode = new InputNode("username", "username", new ErrorNode("invalid.username"), input -> input.length() > 5);
        InputNode passwordInputNode = new InputNode("password", "password");

        registerNode.setChild(loginInputNode).setChild(passwordInputNode);
        loginAccountNode.setChild(loginInputNode).setChild(passwordInputNode);

        Menu menu = new Menu(accountMasterNode);
        ConsoleSystem console = new ConsoleSystem();
        console.run(menu);
    }

    public Menu(MasterOptionNode node) {
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
