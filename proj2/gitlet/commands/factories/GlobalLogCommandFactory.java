package gitlet.commands.factories;

import gitlet.commands.GlobalLogCommand;
import gitlet.commands.Command;

public class GlobalLogCommandFactory implements CommandFactory {
    
    @Override
    public String getCommandName() {
        return "global-log";
    }
    
    @Override
    public String getCommandDescription() {
        return null;
    }
    
    @Override
    public Command makeCommand(String[] args) {
        if (args.length > 1) {
            System.out.println("Incorrect operands.");
            return null;
        } else {
            return new GlobalLogCommand();
        }
    }
}
