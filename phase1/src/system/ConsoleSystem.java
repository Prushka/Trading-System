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
            menu.display();
            String input = "";
            while (!input.equals("exit")) {
                input = br.readLine();
                if (!input.equals("exit")) {
                    menu.parseInput(input);
                    menu.display();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            System.err.println("There's no node next!");
        }

    }


}