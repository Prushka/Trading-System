package group.system;

import group.menu.Menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleSystem {

    public void run(Menu menu) { // lecture code

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

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
            System.err.println("There's no node next!");
        }

    }


}
