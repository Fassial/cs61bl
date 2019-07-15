package gitlet.commands;

import gitlet.FileWriterFactory;
import gitlet.FileOriginWriter;

public class RmBranchCommand implements Command {
    private String branch;
    private FileOriginWriter fileWriter;
    private String stdOutBranchNotExists;
    private String stdOutIsCurrentBranch;
    
    public RmBranchCommand(String branch) {
        this.branch = branch;
        this.fileWriter = FileWriterFactory.getWriter();
        this.stdOutBranchNotExists = "A branch with that name does not exist.";
        this.stdOutIsCurrentBranch = "Cannot remove the current branch.";
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
            String branchPath = ".gitlet/refs/heads/" + branch;
            if (!fileWriter.exists(branchPath)) {
                // check that branch exists, fail if false
                System.out.println(this.stdOutBranchNotExists);
                return false;
            } else {
                if (branch.equals(fileWriter.getCurrentBranch())) {
                    // check that branch is not current branch, fail if true
                    System.out.println(this.stdOutIsCurrentBranch);
                    return false;
                } else {
                    fileWriter.deleteBranch(branch);
                    return true;
                }
            }
        }
    }
}
