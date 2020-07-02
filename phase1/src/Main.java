import menu.ConsoleSystem;
import menu.Menu;
import menu.Validator.EmailValidator;
import menu.data.Request;
import menu.data.Response;
import menu.node.*;

public class Main {

    public static class SampleUseCase {
        public Response parseRequest(Request request) {
            return new Response(true, "register.success", request.get("username"), request.get("password"));
        }

        public Response parseRequestFail(Request request) {
            return new Response(false, "register.fail", request.get("username"), request.get("password"));
        }
    }

    public static void main(String[] args) { // TODO: Maybe a builder is not that useful...

        SampleUseCase useCase = new SampleUseCase();

        OptionNode registerOptionNode = new OptionNode.Builder("option.register").id(1).build();
        OptionNode loginOptionNode = new OptionNode.Builder("option.login").id(2).build();

        MasterOptionNode accountMaster = new MasterOptionNode.Builder("account.master")
                .addChild(registerOptionNode, loginOptionNode).build();

        InputNode emailInput = new InputNode.Builder("input.email")
                .validator(new EmailValidator())
                .validateFailResponse(new ResponseNode("invalid.email"))
                .build();

        InputNode userNameInput = new InputNode.Builder("input.username")
                .validateFailResponse(new ResponseNode("invalid.username")).build();


        registerOptionNode.setChild(emailInput).setChild(userNameInput);

        Menu menu = new Menu(accountMaster);
        ConsoleSystem console = new ConsoleSystem();
        console.run(menu);
    }
}
