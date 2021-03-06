package gitlet.commands;

import gitlet.RemoteRepos;
import gitlet.Commit;
import gitlet.FileWriterFactory;
import gitlet.FileOriginWriter;

import java.io.File;
import java.util.HashMap;

public class PushCommand implements Command {
    private String remoteName;
    private String branch;
    private FileOriginWriter fileWriter;
    private String stdOutHeadNotEqual;
    private String stdOutRemoteDirNotExists;
    
    public PushCommand(String remoteName, String branch) {
        this.remoteName = remoteName;
        this.branch = branch;
        this.fileWriter = FileWriterFactory.getWriter();
        this.stdOutHeadNotEqual = "Please pull down remote changes before pushing.";
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
                String currentHead = this.fileWriter.getCurrentHeadPointer();
                Commit currentHeadCommit = fileWriter.recoverCommit(currentHead);
                String remoteHead = currentRemoteRepos.getRemoteBranchHead(this.remoteName, this.branch);
				if (remoteHead == null) {
					System.out.println("Remote branch: " + this.branch + " does not exist!");
					return false;
				}
                Commit remoteHeadCommit = currentRemoteRepos.recoverCommit(remoteRepoName, remoteHead);
                if (remoteHeadCommit == null) {
                    // remoteHead CommitId don't exist
                    System.out.println(this.stdOutHeadNotEqual);
                    return false;
                }
                boolean currentHeadIsNew = false;
                Commit p = currentHeadCommit;
                while (p.getParent() != null) {
                    p = p.getParent();
                    if (p.equals(remoteHeadCommit)) {
                        currentHeadIsNew = true;
                        break;
                    }
                }
                if (!currentHeadIsNew) {
                    // including head not equals and remote branch not exists
                    System.out.println(this.stdOutHeadNotEqual);
                    return false;
                } else {
                    p = currentHeadCommit;
                    String sourceRootDirectory = ".gitlet" + File.separator + "objects" + File.separator;
                    String destRootDirectory = remoteRepoName + File.separator + "objects" + File.separator;
                    /*
                     * append the future commits to the remote branch.
                     */
                    while (!p.equals(remoteHeadCommit)) {
                        String sourceDirectory =  sourceRootDirectory + p.getId();
                        String destDirectory =  destRootDirectory + p.getId();
                        currentRemoteRepos.copyDirectory(sourceDirectory, destDirectory);
                        p = p.getParent();
                    }
                    // modify the heads/branch 's head commit
                    currentRemoteRepos.setRemoteBranchHead(remoteName, branch, currentHead);
                    Commit remoteFutureHeadCommit = currentRemoteRepos.recoverCommit(remoteRepoName, currentHead);
                    HashMap<String, String> fp = remoteFutureHeadCommit.getFilePointers();
                    if (fp != null && fp.size() > 0) {
                        for(String filePath : fp.keySet()){
                            String fileCommitId = fp.get(filePath);
                            // String realFileRootPath = new String(remoteRepoName);
                            // realFileRootPath = realFileRootPath.replace("/.gitlet", "");
							String realFileRootPath = remoteRepoName.substring(0, remoteRepoName.lastIndexOf("\\"));
                            String commitPath = remoteRepoName + File.separator + "objects" + File.separator + fileCommitId;
                            String fileName =  commitPath + File.separator + filePath;
                            fileWriter.copyFile(fileName, realFileRootPath + filePath);
                        }
                    }
                    return true;
                }
            }
        }
    }
}
