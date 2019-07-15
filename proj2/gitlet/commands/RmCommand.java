package gitlet.commands;

import gitlet.Commit;
import gitlet.FileWriterFactory;
import gitlet.FileOriginWriter;
import gitlet.Staging;

public class RmCommand implements Command {
    private String fileToRm;
    private FileOriginWriter fileWriter;
    
    public RmCommand(String filename) {
        this.fileToRm = filename;
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
            // get the current staging area
            Staging staging = fileWriter.recoverStaging();
            if (staging.getFilesToRm().contains(fileToRm)) {
                // if fileToRM is alread in filesToRm, just
                // return true
                return true;
            } else if (staging.getFilesToAdd().contains(fileToRm)) {
                // if fileToRm is in filesToAdd, just remove it,
                // resave staging area, and return true
                staging.getFilesToAdd().remove(fileToRm);
                fileWriter.saveStaging(staging);
                return true;
            } else {
                // get the head commit. check if fileToRm is in the
                // filePointers collection. If not, no reason to remove
                String headId = fileWriter.getCurrentHeadPointer();
                Commit headCommit = fileWriter.recoverCommit(headId);
                if (headCommit.getFilePointers() != null && headCommit.getFilePointers().containsKey(fileToRm)){
                    staging.getFilesToRm().add(fileToRm);
                    fileWriter.saveStaging(staging);
                    return true;
                } else {
                    System.out.println("No reason to remove the file.");
                    return false;
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
