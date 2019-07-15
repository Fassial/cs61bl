package gitlet.commands.factories;

import gitlet.commands.Command;
import gitlet.commands.ResetCommand;

public class ResetCommandFactory implements CommandFactory {
    
    @Override
    public String getCommandName() {
        return "reset";
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
            return new ResetCommand(args[1]);
        }
    }
}
