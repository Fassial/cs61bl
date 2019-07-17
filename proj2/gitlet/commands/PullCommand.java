package gitlet.commands;

import gitlet.FileWriterFactory;
import gitlet.FileOriginWriter;

import java.io.File;

public class PullCommand implements Command {
    private String remoteName;
    private String branch;
    private FileOriginWriter fileWriter;
    
    public PullCommand(String remoteName, String branch) {
        this.remoteName = remoteName;
        this.branch = branch;
        this.fileWriter = FileWriterFactory.getWriter();
    }
    
    @Override
    public boolean isDangerous() {
        return true;
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
            if (!new FetchCommand(this.remoteName, this.branch).execute()) {
                return false;
            } else {
                // String branchRefDirectory = ".gitlet" + File.separator + "refs" + File.separator + "remotes" 
                //                             + File.separator + this.remoteName + File.separator + this.branch;
                // get the current commit, the other branch commit, and the splitpoint
                return new MergeCommand(this.remoteName + File.separator + this.branch).execute();
            }
        }
    }
}
