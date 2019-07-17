package gitlet.commands;

import gitlet.FileWriterFactory;
import gitlet.FileOriginWriter;

public class BranchCommand implements Command {
    private String branchName;
    private FileOriginWriter fileWriter;
    private String stdOutBranchExists;
    
    public BranchCommand(String branchName) {
        this.branchName = branchName;
        this.fileWriter = FileWriterFactory.getWriter();
        this.stdOutBranchExists = "A branch with that name already exists.";
    }
    
    @Override
    public boolean isDangerous() {
        return false;
    }
    
    @Override
    public boolean needInitDir() {
        return true;
    }
    
    @Override
    public boolean execute() {
        if (this.needInitDir() != fileWriter.exists(".gitlet")) {
            System.out.println("Not in an initialized gitlet directory.");
            return false;
        } else {
            if (fileWriter.exists(".gitlet/refs/heads/" + branchName)) {
                // check if branch already exists, if it does, 
                // output error message and return false
                System.out.println(this.stdOutBranchExists);
                return false;
            } else {
                // get current commit id
                String currentCommitId = fileWriter.getCurrentHeadPointer();
                // create new branch with commitId as contents
                fileWriter.createFile(".gitlet/refs/heads/" + branchName, currentCommitId);
                // change current branch pointer is what checkout has to do, just return
                return true;
            }
        }
    }
}
