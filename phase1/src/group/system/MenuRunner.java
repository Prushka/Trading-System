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

    private static final Logger LOGGER = new LoggerFactory(MenuRunner.class).getConfiguredLogger();

    /**
     * The logic controller of Menu, contains all menu nodes and exposed parseInput()
     */
    private final MenuLogicController menuLogicController;

    /**
     * The shutdownables to be shutdown when the loop ends
     */
    private final List<Shutdownable> shutdowns;

    /**
     * @param menuLogicController the logic controller of Menu, contains all menu nodes and exposed parseInput()
     */
    public MenuRunner(MenuLogicController menuLogicController) {
        this.menuLogicController = menuLogicController;
        shutdowns = new ArrayList<>();
    }

    /**
     * The main loop which accepts user input and pass it to the {@link MenuLogicController}
     * This part is based on Lecture Code, Logging project
     */
    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        menuLogicController.fetchInitialResponse().display();

        try {
            String input = "";
            while (!input.equalsIgnoreCase("exit")) {
                input = br.readLine();
                if (!input.equalsIgnoreCase("exit")) {
                    menuLogicController.parseInput(input);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to read from Buffered reader.", e);
        }
        shutdown();
    }

    /**
     * @param shutdownable add a shutdownable to be shutdown by this class
     */
    public void shutdownHook(Shutdownable shutdownable) {
        this.shutdowns.add(shutdownable);
    }

    /**
     * operations to perform (shutdown the shutdownables) before the program destroys
     */
    private void shutdown() {
        shutdowns.forEach(Shutdownable::shutdown);
    }
}
