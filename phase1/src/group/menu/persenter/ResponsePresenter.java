package group.menu.persenter;

import group.config.LoggerFactory;
import group.menu.data.Response;
import group.menu.data.TranslatablePair;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * I don't know if I should separate our presenter here.
 * I could totally display within nodes.
 * But from my understanding, those nodes have to be part of the controller. Otherwise we wouldn't follow the MVC pattern.
 * There's also no logic in this presenter, since controller maps everything into a Response object. I don't know if this is valid.
 * Or if we should let the presenter handle separate record.
 */
public class ResponsePresenter {

    static final Logger LOGGER = new LoggerFactory(ResponsePresenter.class).getConfiguredLogger();

    private final Response response;

    public ResponsePresenter(Response response) {
        this.response = response;
    }

    public void display() {
        for (TranslatablePair pair : response.getTranslatablePairs()) {
            LOGGER.log(Level.INFO, pair.getTranslatable(), pair.getParas());
        }
    }
}
