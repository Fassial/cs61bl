package gitlet.commands;

import gitlet.RemoteRepos;
import gitlet.FileWriterFactory;
import gitlet.FileOriginWriter;

public class AddRemoteCommand implements Command {
    private String remoteName;
    private String remoteRepoName;
    private FileOriginWriter fileWriter;
    private String stdOutRemoteExists;
    
    public AddRemoteCommand(String remoteName, String remoteRepoName) {
        this.remoteName = remoteName;
        this.remoteRepoName = remoteRepoName;
        this.fileWriter = FileWriterFactory.getWriter();
        this.stdOutRemoteExists = "A remote with that name already exists.";
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
            if (currentRemoteRepos.inRemoteRepos(this.remoteName)) {
                System.out.println(this.stdOutRemoteExists);
                return false;
            } else {
                return currentRemoteRepos.addRemoteRepo(remoteName, remoteRepoName);
            }
        }
    }
}
