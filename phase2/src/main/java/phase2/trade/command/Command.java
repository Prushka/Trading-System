package phase2.trade.command;

public abstract class Command {

    public abstract void execute();

    public abstract void undo();

    public abstract void redo();

    // hmm
}