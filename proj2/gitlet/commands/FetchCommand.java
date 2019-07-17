package gitlet.commands;

import gitlet.RemoteRepos;
import gitlet.FileWriterFactory;
import gitlet.FileOriginWriter;

import java.io.File;
import java.util.Arrays;

public class FetchCommand implements Command {
    private String remoteName;
    private String branch;
    private FileOriginWriter fileWriter;
    private String stdOutRemoteNotHaveBranch;
    private String stdOutRemoteDirNotExists;
    
    public FetchCommand(String remoteName, String branch) {
        this.remoteName = remoteName;
        this.branch = branch;
        this.fileWriter = FileWriterFactory.getWriter();
        this.stdOutRemoteNotHaveBranch = "That remote does not have that branch.";
        this.stdOutRemoteDirNotExists = "Remote directory not found.";
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
            RemoteRepos currentRemoteRepos = RemoteRepos.getCurrentRemoteRepos();
            String remoteRepoName = currentRemoteRepos.getRemoteRepos().get(remoteName);
            if (!(new File(remoteRepoName).exists())) {
                System.out.println(this.stdOutRemoteDirNotExists);
                return false;
            } else {
                String[] remoteRepoBranches = currentRemoteRepos.getAllRemoteRepoBranches(this.remoteName);
                if (remoteRepoBranches == null) {
                    // should not come here
                    System.out.println(this.stdOutRemoteNotHaveBranch);
                    return false;
                }
                if (Arrays.binarySearch(remoteRepoBranches, branch) < 0) {
                    // the remote gitlet does not have the given branch name,
                    System.out.println(this.stdOutRemoteNotHaveBranch);
                    return false;
                } else {
                    return currentRemoteRepos.fetchRemoteRepoBranch(remoteName, branch);
                }
            }
        }
    }
}
