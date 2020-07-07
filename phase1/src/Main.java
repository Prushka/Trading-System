import system.TestController;

public class Main {

    public static void main(String[] args) {
        TestController testController = new TestController();
        Runtime.getRuntime().addShutdownHook(new Thread(testController::shutdown));
    }

}