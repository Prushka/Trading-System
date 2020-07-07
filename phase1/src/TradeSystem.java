//package system;


//import src.PersonalUserManager;
//import src.AdministrativeManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TradeSystem {

    private PersonalUserManager pum = new PersonalUserManager();
    private AdministrativeManager aum = new AdministrativeManager(); // where do we create first/head admin


    public void makeUserAccount() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        UserPropertyIterator prompts = new UserPropertyIterator();
        ArrayList<String> userDetails = new ArrayList<>();
        int curr = 0;


        System.out.println("To create your account enter ok, else enter exit: ");
        try {
            String userInput = br.readLine();
            while (!userInput.equals("exit")) {
                if (prompts.hasNext()) {
                    System.out.println(prompts.next());
                }
                userInput = br.readLine();
                if (!userInput.equals("exit")) {
                    userDetails.add(userInput);
                    curr++;
                }
            }
        }
        catch(IOException e) {
            System.out.println("something went wrong, please try again");
        }

        try {
            pum.createPersonalUser(userDetails.get(0), userDetails.get(1), userDetails.get(2), userDetails.get(3));
        }
        catch(IndexOutOfBoundsException e) {
            System.out.println("something went wrong, please try again.");
        }
    }


    public void verifyLogin() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String userInput = br.readLine();
            ArrayList<String> userDetails = new ArrayList<>();

            while (!userInput.equals("exit")) {
                System.out.println("Type 1 to login to Personal Account, 2 for Administrator Account or exit to cancel: ");
                if (userInput.equals("1")) {
                    userInput = br.readLine();
                    System.out.println("Enter your username: ");
                    userDetails.add(userInput);
                    userInput = br.readLine();
                    System.out.print("Enter your password: ");
                    userDetails.add(userInput);
                    if (pum.verify(userDetails.get(0), userDetails.get(1))) {
                        userInput = br.readLine();
                        System.out.println("Would you like to add or remove from your booklist? (enter add/remove): ");
                        ArrayList<String> userInv = pum.findUser(userDetails.get(0)).getInventory;  //implement findUser in personalusermanager
                        if (userInput.equals("add")) {
                            userInput = br.readLine();
                            userInv.add(userInput);
                        }
                        if (userInput.equals("remove")) {
                            System.out.println(userInv);
                            userInput = br.readLine();
                            System.out.println("Enter the index of which item you would like to remove: ");
                            userInv.remove(userInput);
                            System.out.println(userInv); // prints updated inventory
                        }
                    }
                }

                if (userInput.equals("2")) {
                    userInput = br.readLine();
                    System.out.println("Enter your username: ");
                    userDetails.add(userInput);
                    userInput = br.readLine();
                    System.out.print("Enter your password: ");
                    userDetails.add(userInput);
                    if (aum.verifyLogin(userDetails.get(0), userDetails.get(1))) {
                        userInput = br.readLine();
                        System.out.println("Would you like to add or remove from a user booklist? (enter a username): ");
                        ArrayList<String> userInv = pum.findUser(userInput).getInventory;
                        userInput = br.readLine();
                        System.out.println("Would you like to add or remove?(enter add/remove): ");
                        if (userInput.equals("add")) {
                            userInput = br.readLine();
                            userInv.add(userInput);
                        }
                        if (userInput.equals("remove")) {
                            System.out.println(userInv);
                            userInput = br.readLine();
                            System.out.println("Enter the index of which item you would like to remove: ");
                            userInv.remove(userInput);
                            System.out.println(userInv); // prints updated inventory
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("something went wrong");
        }
    }


}




