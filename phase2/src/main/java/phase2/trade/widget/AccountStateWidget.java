package phase2.trade.widget;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import phase2.trade.controller.ControllerResources;
import phase2.trade.user.AccountState;
import phase2.trade.user.command.ChangeAccountState;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The Account state widget.
 *
 * @author Dan Lyu
 */
public class AccountStateWidget extends WidgetControllerBase {

    private final Label title = new Label();

    /**
     * Constructs a new Account state widget.
     *
     * @param controllerResources the controller resources
     */
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
        RadioButton normalRadio = getNodeFactory().getDefaultRadioButton(AccountState.NORMAL.language, group);
        RadioButton onvacationRadio = getNodeFactory().getDefaultRadioButton(AccountState.ON_VACATION.language, group);

        switch (getAccountManager().getLoggedInUser().getAccountState()) {
            case NORMAL:
                normalRadio.setSelected(true);
                break;
            case ON_VACATION:
                onvacationRadio.setSelected(true);
                break;
        }
        EventHandler<ActionEvent> accountStateHandler = event -> {
            ChangeAccountState changeAccountState = getCommandFactory().getCommand(ChangeAccountState::new);
            changeAccountState.execute((result, status) -> {
                status.setSucceeded(() -> {
                    String message = result.equals(AccountState.ON_VACATION) ? "Your items won't be on shelf!" : "Updated!";
                    getNotificationFactory().toast(2, message);
                });
                status.handle(getNotificationFactory());
            }, AccountState.getByLanguage(((RadioButton) group.getSelectedToggle()).getText()).name());
        };

        normalRadio.setFocusTraversable(false);
        onvacationRadio.setFocusTraversable(false);
        normalRadio.setOnAction(accountStateHandler);
        onvacationRadio.setOnAction(accountStateHandler);
        addTitle(title);
        addContent(normalRadio, onvacationRadio);
        refresh();
    }
}
