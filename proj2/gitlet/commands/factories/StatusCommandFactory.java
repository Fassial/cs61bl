package gitlet.commands.factories;

import gitlet.commands.Command;
import gitlet.commands.StatusCommand;

public class StatusCommandFactory implements CommandFactory {
    
    @Override
    public String getCommandName() {
        return "status";
    }
    
    @Override
    public String getCommandDescription() {
        return null;
    }
    
    @Override
    public Command makeCommand(String[] args) {
        if (args.length != 1) {
            System.out.println("Incorrect operands.");
            return null;
        } else {
            return new StatusCommand();
        }
    }
}
