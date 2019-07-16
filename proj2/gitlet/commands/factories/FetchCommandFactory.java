package gitlet.commands.factories;

import gitlet.commands.Command;
import gitlet.commands.FetchCommand;

public class FetchCommandFactory implements CommandFactory {
    
    @Override
    public String getCommandName() {
        return "fetch";
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
            return new FetchCommand(args[1], args[2]);
        }
    }
}
