import menu.ConsoleSystem;
import menu.Menu;
import menu.data.NodeRequest;
import menu.data.Response;
import menu.node.*;
import menu.node.base.RequestHandler;

public class Main {

    public static class SampleUseCase {
        public Response parseRequest(NodeRequest request) {
            return new Response(true, "register.success", request.get("username"), request.get("password"));
        }

        public Response parseRequestFail(NodeRequest request) {
            return new Response(false, "register.fail", request.get("username"), request.get("password"));
        }
    }

    public static void main(String[] args) { // TODO: Need a better way to build menu nodes

        SampleUseCase useCase = new SampleUseCase();

        OptionNode registerNode = new OptionNode(1, "option.register");
        OptionNode loginAccountNode = new OptionNode(2, "option.login");
        OptionNode requestInfo = new OptionNode(3, "get.something");


        ResponseNode responseNode = new ResponseNode("");
        requestInfo.setChild(responseNode);

        MasterOptionNode accountMasterNode = new MasterOptionNode("account.master.node").addChild(loginAccountNode).addChild(registerNode).sort();


        InputNode usernameInputNode = new InputNode("username", "username");
        ResponseNode invalidInput = new ResponseNode("invalid.username");
        invalidInput.setChild(usernameInputNode);


        usernameInputNode.validateNode(invalidInput, input -> input.length() > 5);


        // TODO: Will this violate?
        SubmitNode passwordNode = new SubmitNode("password", "password", useCase::parseRequestFail, registerNode);
        passwordNode.setChild(accountMasterNode);

        registerNode.setChild(usernameInputNode).setChild(passwordNode);
        loginAccountNode.setChild(usernameInputNode).setChild(passwordNode);

        Menu menu = new Menu(accountMasterNode);
        ConsoleSystem console = new ConsoleSystem();
        console.run(menu);
    }
}
