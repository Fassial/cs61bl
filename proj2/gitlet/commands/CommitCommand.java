package gitlet.commands;

import gitlet.Commit;
import gitlet.FileWriterFactory;
import gitlet.FileOriginWriter;
import gitlet.Staging;
import java.util.HashMap;

public class CommitCommand implements Command {
    private String message;
    private FileOriginWriter fileWriter;
    
    public CommitCommand(String message) {
        this.message = message;
        this.fileWriter = FileWriterFactory.getWriter();
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
            if ("".equals(message)){
                // if commit message is empty, print error message and return false
                System.out.println("Please enter a commit message.");
                return false;
            } else {
                // recover current commit
                String currentCommitId = fileWriter.getCurrentHeadPointer();
                Commit currentHead = fileWriter.recoverCommit(currentCommitId);
                // get Staging 
                Staging staging = fileWriter.recoverStaging();
                //    if stagins is empty, print error message and return false
                if (staging.getFilesToRm().size() == 0 && staging.getFilesToAdd().size() == 0 ){
                    System.out.println("No changes added to the commit.");
                    return false;
                } else {
                    // get current filePointers
                    HashMap<String, String> filePointers = currentHead.getFilePointers() == null ? 
                                    new HashMap<String, String>() : currentHead.getFilePointers();
                    // create new commit with parent filePointers and 
                    // current systime for timestamp
                    Commit newCommit = new Commit(currentHead, System.currentTimeMillis(), message, filePointers);
                    String id = newCommit.getId();
                    // create commit folder
                    String objectsFolder = ".gitlet/objects/" + id;
                    fileWriter.createDirectory(objectsFolder);
                    // add or update the filePointers from 
                    // staging.filesToAdd
                    // and make a copy of files to commit folder
                    if (staging.getFilesToAdd().size() > 0) {
                        for (String fileToAdd : staging.getFilesToAdd()){
                            newCommit.getFilePointers().put(fileToAdd, id);
                            // write commit copy
                            fileWriter.copyFile(fileToAdd, objectsFolder + "/" + fileToAdd);
                        }
                    }
                    // remove files from filePointers from
                    // staging.fileToRm
                    if (staging.getFilesToRm().size() > 0) {
                        for (String fileToRm : staging.getFilesToRm()){
                            newCommit.getFilePointers().remove(fileToRm);
                        }
                    }
                    // reset .gitlet/staging directory
                    fileWriter.deleteDirectory(".gitlet/staging/filesToAddFolder", false);
                    fileWriter.deleteDirectory(".gitlet/staging/filesToRmFolder", false);
                    // save new Commit object
                    fileWriter.saveCommit(newCommit);
                    // update reference in current branch to 
                    // new commit id
                    fileWriter.createFile(fileWriter.getCurrentBranchRef(), id);
                    // reset and save staging area
                    fileWriter.saveStaging(new Staging());
                    return true;
                }
            }
        }
    }
    
    public FileOriginWriter getFileWriter() {
        return fileWriter;
    }

    public void setFileWriter(FileOriginWriter fileWriter) {
        this.fileWriter = fileWriter;
    }
}
