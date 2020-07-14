package group.system;

import group.config.LoggerFactory;
import group.menu.Menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The main loop of the presenter part.
 *
 * @author lecture code, Logging project
 * @author Dan Lyu
 */
public class ConsoleSystem {

    static final Logger LOGGER = new LoggerFactory(ConsoleSystem.class).getConfiguredLogger();

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
            LOGGER.log(Level.SEVERE, "Unable to read from Buffered reader.", e);
        } catch (NullPointerException e) {
            LOGGER.log(Level.SEVERE, "There's no node next.", e);
        }

    }


}
