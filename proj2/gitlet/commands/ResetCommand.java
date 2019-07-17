package gitlet.commands;

import gitlet.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResetCommand implements Command {
    private String id;
    private FileOriginWriter fileWriter;
    private String stdOutCommitNotExists;
    private String stdOutUntrackedOw;

    public ResetCommand(String id) {
        this.id = id;
        this.fileWriter = FileWriterFactory.getWriter();
        this.stdOutCommitNotExists = "No commit with that id exists.";
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
            try {
                Commit commit = fileWriter.recoverCommit(id);
                HashMap<String,String> fp = commit.getFilePointers();
                // recover current commit
                String currentCommitId = fileWriter.getCurrentHeadPointer();
                Commit currentHead = fileWriter.recoverCommit(currentCommitId);
                List<String> fileList = new ArrayList<>();
                FileSystemWriter.getFiles(fileWriter.getWorkingDirectory(), "", fileList);
                Staging staging = fileWriter.recoverStaging();
                List<String> untrackedFiles = new ArrayList<>();
                while (fileList.size() != 0) {
                    String fileName = fileList.remove(0);
                    // modify untrackedFiles
                    if (currentHead.getFilePointers() != null) {
                        if (!currentHead.getFilePointers().containsKey(fileName)) {
                            if (staging.getFilesToRm().size() > 0) {
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
                if (fp != null && fp.size() > 0) {
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
                    if (fp != null && fp.size() > 0) {
                        for (String filePath : fp.keySet()) {
                            String fileCommitId = fp.get(filePath);
                            new CheckoutFileCommand(fileCommitId, filePath).execute();
                        }
                    }
                    // make current branch point to commit
                    String branchRef = fileWriter.getCurrentBranchRef();
                    fileWriter.createFile(branchRef, id);
                    // reset .gitlet/staging directory
                    fileWriter.deleteDirectory(".gitlet/staging/filesToAddFolder", false);
                    fileWriter.deleteDirectory(".gitlet/staging/filesToRmFolder", false);
                    // reset and save staging area
                    fileWriter.saveStaging(new Staging());
                    return true;
                }
            } catch (IllegalArgumentException e) {
                // happen when commit id dir doesn't exist
                System.out.println(this.stdOutCommitNotExists);
                return false;
            }
        }
    }
}
