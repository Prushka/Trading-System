package group.system;

import group.menu.Menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The main loop of the presenter part.
 *
 * @author lecture code, Logging project
 * @author Dan Lyu
 */
public class ConsoleSystem {

    public void run(Menu menu) {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        menu.displayInitial();
        try {
            String input = "";
            while (!input.equalsIgnoreCase("exit")) {
                input = br.readLine();
                if (!input.equalsIgnoreCase("exit")) {
                    menu.parseInput(input);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
            System.err.println("There's no node next!");
        }

    }


}
