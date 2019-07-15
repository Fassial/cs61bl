package gitlet.commands;

import gitlet.Commit;
import gitlet.FileWriterFactory;
import gitlet.FileOriginWriter;
import java.util.HashMap;

public class CheckoutBranchCommand implements Command {
    private String branch;
    private FileOriginWriter fileWriter;
    private String stdOutBranchNotFound;
    private String stdOutIsCurrentBranch;
    private String stdOutUntrackedOw;
    
    public CheckoutBranchCommand(String branch) {
        this.branch = branch;
        this.fileWriter = FileWriterFactory.getWriter();
        this.stdOutBranchNotFound = "No such branch exists.";
        this.stdOutIsCurrentBranch = "No need to checkout the current branch.";
        this.stdOutUntrackedOw = "There is an untracked file in the way; delete it or add it first.";
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
            if (!fileWriter.exists(".gitlet/refs/heads/" + branch)) {
                // if branch doesn't exist, print message and fail
                System.out.println(this.stdOutBranchNotFound);
                return false;
            } else if (branch.equals(fileWriter.getCurrentBranch())) {
                // if branch is current branch, print error and return false
                System.out.println(this.stdOutIsCurrentBranch);
                return false;
            } else {
                // get id of head commit of branch
                String commitId = fileWriter.getBranchHead(branch);
                // get commit
                Commit commit = fileWriter.recoverCommit(commitId);
                HashMap<String, String> fp = commit.getFilePointers();
                
                // recover current commit
                String currentCommitId = fileWriter.getCurrentHeadPointer();
                Commit currentHead = fileWriter.recoverCommit(currentCommitId);
                List<String> fileList = new ArrayList<>();
                FileSystemWriter.getFiles(fileWriter.getWorkingDirectory(), "", fileList);
                List<String> untrackedFiles = new ArrayList<>();
                while (fileList.size() != 0) {
                    String fileName = fileList.remove(0);
                    // modify untrackedFiles
                    if (currentHead.getFilePointers() != null) {
                        if (!currentHead.getFilePointers().containsKey(fileName)) {
                            if (staging.getFilesToRm().size() > 0){
                                if (!staging.getFilesToRm().contains(fileName)) {
                                    untrackedFiles.add(fileName);
                                }
                            } else {
                                untrackedFiles.add(fileName);
                            }
                        }
                    } else {
                        untrackedFiles.add(fileName);        // here we think (add not commit changes) are still untracked
                    }
                }
                boolean untrackedFilesOw = false;
                if (fp.size() > 0 && fp != null) {
                    while (untrackedFiles.size() != 0) {
                        String fileName = untrackedFiles.remove(0);
                        if (fp.containsKey(fileName)) {
                            untrackedFilesOw = true;
                            break;
                        }
                    }
                }
                if (untrackedFilesOw) {
                    System.out.println(this.stdOutUntrackedOw);
                    return false;
                } else {
                    if (fp.size() > 0 && fp != null) {
                        for(String filePath : fp.keySet()){
                            String fileCommitId = fp.get(filePath);
                            new CheckoutFileCommand(fileCommitId, filePath).execute();
                        }
                    }
                    // make branch the current head reference
                    fileWriter.makeBranchHead(branch);        // don't flush staging, the same as git.
                    return true;
                }
            }
        }
    }
}
