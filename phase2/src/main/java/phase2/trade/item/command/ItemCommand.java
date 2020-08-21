package phase2.trade.item.command;

import phase2.trade.command.UpdateCommand;
import phase2.trade.item.Item;

import javax.persistence.Entity;

/**
 * The base class for {@link Item} related commands.
 *
 * @param <T> the entity type (can be a collection of {@link Item}s or a single Item)
 * @author Dan Lyu
 */
@Entity
public abstract class ItemCommand<T> extends UpdateCommand<T> {

    /**
     * Constructs a new Item command.
     */
    public ItemCommand() {
    }

}
