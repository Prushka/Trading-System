package phase2.trade.callback;

import javafx.application.Platform;
import phase2.trade.presenter.PopupFactory;

public class StatusExist extends StatusFailed {

    @Override
    public void handle() {
        run(new Runnable() {
            @Override
            public void run() {

                failed.run();
                after.run();
            }
        });
    }

}
