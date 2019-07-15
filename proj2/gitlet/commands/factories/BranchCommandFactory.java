package gitlet.commands.factories;

import gitlet.commands.BranchCommand;
import gitlet.commands.Command;

public class BranchCommandFactory implements CommandFactory {
    
    @Override
    public String getCommandName() {
        return "branch";
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
            return new BranchCommand(args[1]);
        }
    }
}
