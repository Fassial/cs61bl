package gitlet.commands;

import gitlet.RemoteRepos;
import gitlet.Commit;
import gitlet.FileWriterFactory;
import gitlet.FileOriginWriter;

public class RmRemoteCommand implements Command {
    private String remoteName;
    private FileOriginWriter fileWriter;
    private String stdOutRemoteNotExists;
    
    public RmRemoteCommand(String remoteName) {
        this.remoteName = remoteName;
        this.fileWriter = FileWriterFactory.getWriter();
        this.stdOutRemoteNotExists = "A remote with that name does not exist.";
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
            RemoteRepos currentRemoteRepos = getCurrentRemoteRepos();
            if (!currentRemoteRepos.inRemoteRepos(this.remoteName)) {
                System.out.println(this.stdOutRemoteNotExists);
                return false;
            } else {
                return currentRemoteRepos.remoteRemoteRepo(remoteName);
            }
        }
    }
}
