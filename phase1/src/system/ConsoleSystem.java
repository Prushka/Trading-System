package system;

import menu.Menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ConsoleSystem {

    public void run(Menu menu) { // lecture code

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            menu.getCurrentNode().display();
            String input = "";
            while (!input.equals("exit")) {
                input = br.readLine();
                if (!input.equals("exit")) {
                    menu.parseInput(input);
                    menu.getCurrentNode().display();
                }
            }
        } catch (IOException e) {
            System.out.println("Something went wrong");
        }

    }
}
