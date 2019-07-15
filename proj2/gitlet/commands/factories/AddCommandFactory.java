package gitlet.commands.factories;

import gitlet.commands.AddCommand;
import gitlet.commands.Command;

public class AddCommandFactory implements CommandFactory {
    
    @Override
    public String getCommandName() {
        return "add";
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
        } else {
            return new AddCommand(args[1]);
        }
    }
}
