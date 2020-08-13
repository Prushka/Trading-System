package phase2.trade.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import phase2.trade.command.Command;
import phase2.trade.command.GetCommands;
import phase2.trade.command.GetCommandsByType;
import phase2.trade.presenter.GeneralTableViewController;
import phase2.trade.user.User;
import phase2.trade.user.command.CreateUser;

import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

@ControllerProperty(viewFile = "general_table_view.fxml")
public class UserOperationController extends GeneralTableViewController<Command> implements Initializable {


    public UserOperationController(ControllerResources controllerResources) {
        super(controllerResources, false, false);
    }

    private String convertTime(long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        return format.format(date);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        Command<List<Command>> getCommands = getCommandFactory().getCommand(GetCommands::new);

        getCommands.execute((result, resultStatus) -> {
            resultStatus.setSucceeded(() -> afterFetch(result));
            resultStatus.handle(getPopupFactory());
        });

    }

    public void afterFetch(List<Command> result) {
        System.out.println(result.size());
        setDisplayData(FXCollections.observableArrayList(result));
        tableViewGenerator
                .addColumn("Type", param -> {
                    if (param.getValue() != null) {
                        return new SimpleStringProperty(param.getValue().getDType());
                    } else {
                        return new SimpleStringProperty("null");
                    }
                })
                .addColumn("Time", param -> {
                    if (param.getValue() != null) {
                        return new SimpleStringProperty(convertTime(param.getValue().getTimestamp()));
                    } else {
                        return new SimpleStringProperty("null");
                    }
                })
                .addColumn("Operator", param -> {
                    if (param.getValue() != null) {
                        User operator = param.getValue().getOperator();
                        if (operator == null) {
                            return new SimpleStringProperty("SYSTEM");
                        }
                        return new SimpleStringProperty(param.getValue().getOperator().getName());
                    } else {
                        return new SimpleStringProperty("null");
                    }
                });
        tableViewGenerator.build();

        ComboBox<Class<?>> comboBox = new ComboBox<>();
        comboBox.setItems(FXCollections.observableArrayList(CreateUser.class));
        comboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getCommand(comboBox.getSelectionModel().getSelectedItem());
            }
        });
        getPane("topBar").getChildren().addAll();
    }

    public <C> List<Command<C>> getCommand(Class<C> clazz){
        GetCommandsByType<C> getCommandsByType = getCommandFactory().getCommand(GetCommandsByType::new, c -> {
            c.setCommandClass(clazz);
        });
        // getCommandsByType.execute(((result, status) -> {
        //     TableViewGenerator<>
        // }));
        return null;
    }

}
