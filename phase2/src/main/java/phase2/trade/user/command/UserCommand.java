package phase2.trade.user.command;

import phase2.trade.command.UpdateCommand;

import javax.persistence.Entity;

@Entity
public abstract class UserCommand<T> extends UpdateCommand<T> {

    public UserCommand() {
    }
}
