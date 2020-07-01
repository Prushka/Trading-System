import menu.ConsoleSystem;
import menu.Menu;
import menu.node.ErrorNode;
import menu.node.InputNode;
import menu.node.MasterOptionNode;
import menu.node.OptionNode;

public class Main {

    public static void main(String[] args) { // test

        OptionNode registerNode = new OptionNode(1, "option.register");
        OptionNode loginAccountNode = new OptionNode(2, "option.login");

        MasterOptionNode accountMasterNode = new MasterOptionNode("account.master.node").addChild(loginAccountNode).addChild(registerNode).sort();

        InputNode loginInputNode = new InputNode("username", "username", new ErrorNode("invalid.username"), input -> input.length() > 5);
        InputNode passwordInputNode = new InputNode("password", "password");

        registerNode.setChild(loginInputNode).setChild(passwordInputNode);
        loginAccountNode.setChild(loginInputNode).setChild(passwordInputNode);

        Menu menu = new Menu(accountMasterNode);
        ConsoleSystem console = new ConsoleSystem();
        console.run(menu);
    }
}
