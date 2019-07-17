package gitlet.commands;

public interface Command {
    boolean isDangerous();
    boolean execute();
    boolean needInitDir();
}
