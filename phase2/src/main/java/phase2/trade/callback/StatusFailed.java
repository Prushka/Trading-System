package phase2.trade.callback;

import phase2.trade.presenter.PopupFactory;

public class StatusFailed extends ResultStatus {

    @Override
    public void handle(PopupFactory popupFactory, Runnable succeed, Runnable failed) {

    }

    @Override
    public boolean ifPass() {
        return false;
    }

}
