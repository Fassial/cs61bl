package gitlet.commands.factories;

import gitlet.commands.FindCommand;
import gitlet.commands.Command;

public class FindCommandFactory implements CommandFactory {
    
    @Override
    public String getCommandName() {
        return "find";
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
            return new FindCommand(args[1]);
        }
    }
}
