package gitlet.commands.factories;

import gitlet.commands.Command;
import gitlet.commands.MergeCommand;

public class MergeCommandFactory implements CommandFactory {
    
    @Override
    public String getCommandName() {
        return "merge";
    }
    
    @Override
    public String getCommandDescription() {
        return null;
    }
    
    @Override
    public Command makeCommand(String[] args) {
        if (args.length != 2) {
            System.out.println("Incorrect operands.");
            return null;
        } else {
            return new MergeCommand(args[1]);
        }
    }
}
