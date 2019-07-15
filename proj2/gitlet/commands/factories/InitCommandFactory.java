package gitlet.commands.factories;

import gitlet.commands.Command;
import gitlet.commands.InitCommand;

public class InitCommandFactory implements CommandFactory {
    
    @Override
    public String getCommandName() {
        return "init";
    }
    
    @Override
    public String getCommandDescription() {
        return null;
    }
    
    @Override
    public Command makeCommand(String[] args) {
        if (args.length > 1) {
            System.out.println("Incorrect operands.");
            return null;
        } else {
            return new InitCommand();
        }
    }
}
