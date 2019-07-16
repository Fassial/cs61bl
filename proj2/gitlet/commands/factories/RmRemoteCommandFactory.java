package gitlet.commands.factories;

import gitlet.commands.Command;
import gitlet.commands.RmRemoteCommand;

public class RmRemoteCommandFactory implements CommandFactory {
    
    @Override
    public String getCommandName() {
        return "rm-remote";
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
            return new RmRemoteCommand(args[1]);
        }
    }
}
