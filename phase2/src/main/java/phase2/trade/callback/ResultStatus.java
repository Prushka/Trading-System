package phase2.trade.callback;

import phase2.trade.presenter.PopupFactory;

public abstract class ResultStatus {

    public abstract void handle(PopupFactory popupFactory, Runnable succeed, Runnable failed);

    public abstract boolean ifPass();
}
