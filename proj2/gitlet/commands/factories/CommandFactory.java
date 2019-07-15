package gitlet.commands.factories;

import gitlet.commands.Command;

public interface CommandFactory {
    public String getCommandName();
    public String getCommandDescription();
    public Command makeCommand(String[] args);
}
