package gitlet.commands.factories;

import gitlet.commands.CommitCommand;
import gitlet.commands.Command;

public class CommitCommandFactory implements CommandFactory {
    
    @Override
    public String getCommandName() {
        return "commit";
    }
    
    @Override
    public String getCommandDescription() {
        return null;
    }
    
    @Override
    public Command makeCommand(String[] args) {
        if (args.length > 2) {
            System.out.println("Incorrect operands.");
            return null;
        } else if (args.length < 2) {
            System.out.println("Please enter a commit message.");
            return null;
        } else {
            return new CommitCommand(args[1]);
        }
    }
}
