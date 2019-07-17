package gitlet;

import gitlet.commands.CommandParser;
import gitlet.commands.Command;
import gitlet.commands.factories.*;
import java.util.ArrayList;
import java.util.List;

/* Driver class for Gitlet, the tiny stupid version-control system.
   @author
*/
public class Main {

    /* Usage: java gitlet.Main ARGS, where ARGS contains
       <COMMAND> <OPERAND> .... */
    private static List<CommandFactory> getAvailableCommands() {
        List<CommandFactory> commands = new ArrayList<>();
        // add available commands into the List
        commands.add(new InitCommandFactory());
        commands.add(new AddCommandFactory());
        commands.add(new CommitCommandFactory());
        commands.add(new RmCommandFactory());
        commands.add(new LogCommandFactory());
        commands.add(new GlobalLogCommandFactory());
        commands.add(new FindCommandFactory());
        commands.add(new StatusCommandFactory());
        commands.add(new BranchCommandFactory());
        commands.add(new CheckoutCommandFactory());
        commands.add(new RmBranchCommandFactory());
        commands.add(new ResetCommandFactory());
        commands.add(new MergeCommandFactory());
        commands.add(new AddRemoteCommandFactory());
        commands.add(new RmRemoteCommandFactory());
        commands.add(new PushCommandFactory());
        commands.add(new FetchCommandFactory());
        commands.add(new PullCommandFactory());
        /*commands.add(new RebaseCommandFactory());
        commands.add(new InteractiveRebaseCommandFactory());*/
        
        return commands;
    }
    
    public static void main(String... args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        } else {
            List<CommandFactory> availableCommands = getAvailableCommands();
            CommandParser commandParser = new CommandParser(availableCommands);
            
            Command command = commandParser.parseCommand(args);
            if (command == null) {
                System.out.println("No command with that name exists.");
                return;
            } else {
                boolean isExecutable = true;
                /** Dangerous command will need the user to comfirm it again! 
                if (command.isDangerous()) {
                    System.out.println("Warning: The command you entered may alter the files "
                                     + "in your working directory. Uncommitted changes may be lost. "
                                     + "Are you sure you want to continue? (yes/no)");
                    Scanner scanner = new Scanner(System.in);
                    String answer = scanner.nextLine();
                    if (!"yes".equals(answer)) {
                        isExecutable = false;
                    }
                    scanner.close();
                }
                **/
                if (isExecutable) {
                    command.execute();
                }
                return;
            }
        }
    }

}
