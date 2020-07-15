package group.system;

import group.config.LoggerFactory;
import group.menu.MenuLogicController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuRunner {

    private static final Logger LOGGER = new LoggerFactory(MenuController.class).getConfiguredLogger();

    private final MenuLogicController menuLogicController;

    private final List<Shutdownable> shutdowns;

    public MenuRunner(MenuLogicController menuLogicController) {
        this.menuLogicController = menuLogicController;
        shutdowns = new ArrayList<>();
    }

    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        menuLogicController.fetchInitialResponse().display();

        try {
            String input = "";
            while (!input.equalsIgnoreCase("exit")) {
                input = br.readLine();
                if (!input.equalsIgnoreCase("exit")) {
                    menuLogicController.parseInput(input).display();
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to read from Buffered reader.", e);
        } catch (NullPointerException e) {
            LOGGER.log(Level.SEVERE, "There's no node next.", e);
            e.printStackTrace();
        }
        shutdown();
    }

    public void shutdownHook(Shutdownable shutdownable) {
        this.shutdowns.add(shutdownable);
    }

    private void shutdown() {
        shutdowns.forEach(Shutdownable::shutdown);
    }
}
