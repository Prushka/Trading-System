package group.system;

public class ControllerDispatcher {


    public ControllerDispatcher() {
        MenuConstructor menuConstructor = new MenuConstructor();
        SupportTicketController supportTicketController = new SupportTicketController(menuConstructor);

    }
}
