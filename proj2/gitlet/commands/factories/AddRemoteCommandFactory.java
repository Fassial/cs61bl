package gitlet.commands.factories;

import gitlet.commands.Command;
import gitlet.commands.AddRemoteCommand;

public class AddRemoteCommandFactory implements CommandFactory {
    
    @Override
    public String getCommandName() {
        return "add-remote";
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
            return new AddRemoteCommand(args[1], args[2]);
        }
    }
}
