package phase2.trade.view.widget;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.ResultStatus;
import phase2.trade.controller.ControllerResources;
import phase2.trade.editor.UserEditor;
import phase2.trade.user.AccountState;
import phase2.trade.user.User;
import phase2.trade.user.command.ChangeAccountState;

import java.net.URL;
import java.util.ResourceBundle;

public class AccountStateWidget extends WidgetControllerBase {

    private final Label title = new Label();

    public AccountStateWidget(ControllerResources controllerResources) {
        super(controllerResources);
    }

    @Override
    public void refresh() {
        title.setText("Account State");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGradient("gradient-l");
        ToggleGroup group = new ToggleGroup();
        putLanguageValue(AccountState.NORMAL.name(), "state.normal");
        putLanguageValue(AccountState.ON_VOCATION.name(), "state.on.vocation");
        RadioButton normalRadio = getNodeFactory().getDefaultRadioButton(getLanguageByValue(AccountState.NORMAL.name()), group);
        RadioButton onVocationRadio = getNodeFactory().getDefaultRadioButton(getLanguageByValue(AccountState.ON_VOCATION.name()), group);

        switch (getAccountManager().getLoggedInUser().getAccountState()) {
            case NORMAL:
                normalRadio.setSelected(true);
                break;
            case ON_VOCATION:
                onVocationRadio.setSelected(true);
                break;
        }
        EventHandler<ActionEvent> accountStateHandler = event -> {
            ChangeAccountState changeAccountState = getCommandFactory().getCommand(ChangeAccountState::new);
            changeAccountState.execute((result, status) -> status.setSucceeded(() -> {
                getPopupFactory().toast(2, "Updated");
                status.handle(getPopupFactory());
            }), getValueByLanguage(((RadioButton) group.getSelectedToggle()).getText()));
        };

        normalRadio.setFocusTraversable(false);
        onVocationRadio.setFocusTraversable(false);
        normalRadio.setOnAction(accountStateHandler);
        onVocationRadio.setOnAction(accountStateHandler);
        addTitle(title);
        addContent(normalRadio, onVocationRadio);
        refresh();
    }
}
