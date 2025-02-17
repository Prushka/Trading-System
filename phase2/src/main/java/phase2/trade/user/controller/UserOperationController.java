package phase2.trade.user.controller;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import phase2.trade.alert.TableViewAlert;
import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.command.Command;
import phase2.trade.command.GetCommands;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.EditableTableController;
import phase2.trade.editor.CommandEditor;
import phase2.trade.item.Item;
import phase2.trade.refresh.ReType;
import phase2.trade.user.User;
import phase2.trade.user.command.CreateUser;
import phase2.trade.view.TableViewGenerator;

import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The User operation controller.
 *
 * @author Dan Lyu
 */
@ControllerProperty(viewFile = "general_table_view.fxml")
public class UserOperationController extends EditableTableController<Command, CommandEditor> implements Initializable {

    private final Command<List<Command>> getCommands = getCommandFactory().getCommand(GetCommands::new);

    /**
     * Constructs a new User operation controller.
     *
     * @param controllerResources the controller resources
     */
    public UserOperationController(ControllerResources controllerResources) {
        super(controllerResources, false, false, CommandEditor::new);
    }

    private String convertTime(long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    @Override
    public void reload() {
        getCommands.execute((result, resultStatus) -> {
            resultStatus.setSucceeded(() -> reloadNewDisplayData(result));
            resultStatus.handle(getNotificationFactory());
        });
        super.reload();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        getCommands.execute((result, resultStatus) -> {
            resultStatus.setSucceeded(() -> afterFetch(result));
            resultStatus.handle(getNotificationFactory());
        });

    }

    private TableView<Command> addToTableViewGenerator(List<Command> result, TableViewGenerator<Command> tableViewGenerator) {
        tableViewGenerator
                .addColumn("Type", "dType")
                .addColumn("Time", param -> {
                    if (param.getValue() != null) {
                        return new SimpleStringProperty(convertTime(param.getValue().getTimestamp()));
                    }
                    return new SimpleStringProperty("null");
                })
                .addColumn("Operator", param -> {
                    if (param.getValue() != null) {
                        User operator = param.getValue().getOperator();
                        if (operator == null) {
                            return new SimpleStringProperty("SYSTEM");
                        }
                        return new SimpleStringProperty(param.getValue().getOperator().getName());
                    }
                    return new SimpleStringProperty("null");
                })
                .addColumn("Items", param -> {
                    if (param.getValue() != null) {
                        Collection<Long> effected = param.getValue().getEffectedEntities(Item.class);
                        if (effected != null) {
                            return new SimpleStringProperty(effected.toString());
                        }
                    }
                    return new SimpleStringProperty("null");
                })
                .addColumn("Users", param -> {
                    if (param.getValue() != null) {
                        Collection<Long> effected = param.getValue().getEffectedEntities(User.class);
                        if (effected != null) {
                            return new SimpleStringProperty(effected.toString());
                        }
                    }
                    return new SimpleStringProperty("null");
                })
                .addColumn("undone", param -> {
                    if (param.getValue() != null) {
                        return new SimpleStringProperty(String.valueOf(param.getValue().isUndone()));
                    }
                    return new SimpleStringProperty("null");
                }).
                addColumn("UndoneTime", param -> {
                    if (param.getValue() != null && param.getValue().getUndoTimestamp() != null) {
                        return new SimpleStringProperty(convertTime(param.getValue().getUndoTimestamp()));
                    }
                    return new SimpleStringProperty("null");
                });
        return tableViewGenerator.build();
    }

    /**
     * After fetch.
     *
     * @param result the result
     */
    public void afterFetch(List<Command> result) {
        setDisplayData(FXCollections.observableArrayList(result));
        addToTableViewGenerator(result, tableViewGenerator);

        ComboBox<Class<?>> comboBox = new ComboBox<>();
        comboBox.setItems(FXCollections.observableArrayList(CreateUser.class));
        comboBox.setOnAction(event -> {
            // getCommands(comboBox.getSelectionModel().getSelectedItem());
        });
        // getPane(DashboardPane.TOP).getChildren().addAll(comboBox);

        JFXButton undo = new JFXButton("Undo Selected Operations");
        undo.setOnAction(event -> tableView.getSelectionModel().getSelectedItem().undoIfUndoable((ResultStatusCallback<List<Command>>) (result1, status) -> {
            status.setSucceeded(() -> {
                getNotificationFactory().toast("TADA~ Your command was successfully undone!");
                publish(ReType.RELOAD);
            });
            status.setFailed(() -> {
                if (result1 == null) {
                    getNotificationFactory().toast("The command you selected is not an undoable command! (It's configured to be undoable)");
                } else {
                    getNotificationFactory().toast("The command you selected is effected by other commands!");
                    TableViewAlert<Command> tableViewAlert = getNotificationFactory().tableViewAlert(Command.class, "Blocking Commands", "");
                    tableViewAlert.addTableView(addToTableViewGenerator(result1, new TableViewGenerator<>(FXCollections.observableArrayList(result1))));
                    tableViewAlert.display();
                }
            });
            status.handle(getNotificationFactory());
        }, getGatewayBundle()));
        buttonPane.getChildren().addAll(undo);
    }

    // To display data of sub commands using custom logic
    // 1. The easiest solution is to include a presenting method in every command. This however violates the single responsibility.
    // 2. The second choice is to make a DAO for EVERY Command Class since the generic type cannot be determined at runtime.
    // 3. The above option may be solved by reflection (this one is to be confirmed) to retrieve the ParameterizedType or even implement an interface at RUNTIME. I have no idea how to implement these.
    // 4. The final option here is to use a if statements with (command instance of ?), cast it and then display it here. This smells.

    // Maybe a method to retrieve "Representative" data in its original form can exist in all Command classes. But I do not have the time to implement them separately.
    // Since the "Representative" data should also have generics in it. Otherwise it will become a String the same as a representing method. Or maybe we can have a CommandData. Implement its subclasses in all Commands.
    // So let's just display the effected ids for now

    /**
     * Gets commands.
     *
     * @return the commands
     */
    public List<Command> getCommands() {
        // GetCommandsByType getCommandsByType = getCommandFactory().getCommand(GetCommandsByType::new, c -> {
        //     c.setCommandClass(clazz);
        // });
        // getCommandsByType.execute(((result, status) -> {
        //     TableViewGenerator<>
        // }));
        return null;
    }

    @Override
    protected void updateEntity(List<Command> entities) {

    }
}
