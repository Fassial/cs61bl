package gitlet.commands.factories;

import gitlet.FileWriterFactory;
import gitlet.FileOriginWriter;
import gitlet.commands.CheckoutBranchCommand;
import gitlet.commands.CheckoutFileCommand;
import gitlet.commands.Command;
import java.util.Arrays;
import java.util.List;

public class CheckoutCommandFactory implements ICommandFactory {
    private FileOriginWriter fileWriter;
    
    public CheckoutCommandFactory(){
        this.fileWriter = FileWriterFactory.getWriter();
    }
    
    @Override
    public String getCommandName() {
        return "checkout";
    }
    
    @Override
    public String getCommandDescription() {
        return null;
    }
    
    @Override
    public Command makeCommand(String[] args) {
        List<String> branches = Arrays.asList(fileWriter.getAllBranches());
        if (args.length < 2) {
            System.out.println("Incorrect operands.");
            return null;
        } else if (args.length == 2) {
            if (branches.contains(args[1])) {
                return new CheckoutBranchCommand(args[1]);
            } else {
                return new CheckoutFileCommand(args[1]);
            }
        } else if (args.length == 3) {
            return new CheckoutFileCommand(args[1], args[2]);
        } else {
            System.out.println("Incorrect operands.");
            return null;
        }
    }
}
