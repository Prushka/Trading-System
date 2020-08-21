package phase2.trade.user.command;

import phase2.trade.command.UpdateCommand;

import javax.persistence.Entity;

/**
 * The User command.
 *
 * @param <T> User or a collection of User or anything this UserCommand handles and calls
 * @author Dan Lyu
 */
@Entity
public abstract class UserCommand<T> extends UpdateCommand<T> {

    /**
     * Constructs a new User command.
     */
    public UserCommand() {
    }
}
