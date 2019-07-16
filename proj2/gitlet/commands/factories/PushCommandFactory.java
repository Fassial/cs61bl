package gitlet.commands.factories;

import gitlet.commands.Command;
import gitlet.commands.PushCommand;

public class PushCommandFactory implements CommandFactory {
    
    @Override
    public String getCommandName() {
        return "push";
    }
    
    @Override
    public String getCommandDescription() {
        return null;
    }
    
    @Override
    public Command makeCommand(String[] args) {
        if (args.length != 3) {
            System.out.println("Incorrect operands.");
            return null;
        } else {
            return new PushCommand(args[1], args[2]);
        }
    }
}
