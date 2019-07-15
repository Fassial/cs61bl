package gitlet.commands;

import java.util.List;
import gitlet.commands.factories.CommandFactory;

public class CommandParser {
    private final List<CommandFactory> availableCommands;
    
    public CommandParser(List<CommandFactory> availableCommands) {
        this.availableCommands = availableCommands;
    }
    
    private CommandFactory findRequestedCommand(String commandName) {
        for (CommandFactory availableCommand : availableCommands) {
            if (commandName.equals(availableCommand.getCommandName())) {
                return availableCommand;
            }
        }
        return null;
    }
    
    public Command parseCommand(String[] args) {
        String requestedCommandName = args[0];
        CommandFactory requestedCommand = this.findRequestedCommand(requestedCommandName);
        if (requestedCommand == null) {
            return null;
        } else {
            Command command = requestedCommand.makeCommand(args);
            if (command == null) {
                return new NopCommand();
            } else {
                return command;
            }
        }
    }
}
