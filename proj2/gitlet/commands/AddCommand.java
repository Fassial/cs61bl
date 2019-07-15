package gitlet.commands;

import gitlet.FileWriterFactory;
import gitlet.FileOriginWriter;
import gitlet.Staging;
import java.io.File;

public class AddCommand implements Command {
    private FileOriginWriter fileWriter;
    private String fileToAdd;
    
    public AddCommand(String filename) {
        this.fileToAdd = filename;
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
            // if the file doesn't exists, print error message and 
            // return false
            if (!fileWriter.exists(fileToAdd)){
                System.out.println("File does not exist.");
                // System.err.println("File does not exist: " + fileToAdd);
                return false;
            } else {
                // get the current staging area
                Staging staging = fileWriter.recoverStaging();
                if (staging.getFilesToAdd().contains(fileToAdd)) {
                    // if fileToAdd is alread in filesToAdd, just return true
                    return true;
                } else if (staging.getFilesToRm().contains(fileToAdd)){
                    // if fileToAdd is in filesToRm, just remove it, resave staging area, and return true
                    staging.getFilesToRm().remove(fileToAdd);
                    fileWriter.saveStaging(staging);
                    return true;
                } else {
                    // get the current HEAD, if file is not in that commit, it is definitely a new/changed file
                    String headId = fileWriter.getCurrentHeadPointer();
                    String commitFile = ".gitlet/objects/" + headId + "/" + fileToAdd;
                    if (!fileWriter.exists(commitFile)){
                        staging.getFilesToAdd().add(fileToAdd);
                        fileWriter.saveStaging(staging);
                        return true;
                    } else {
                        // if file is in commit, compare modified date of file in working directory with 
                        // modified date of file in commit directory. If match, file is unchanged.
                        long commitLM = fileWriter.lastModified(commitFile);
                        long toAddLM = fileWriter.lastModified(fileToAdd);        // get lastModified timeStamp
                        if (commitLM == toAddLM && fileWriter.filesEqual(commitFile, fileToAdd)){
                            return false;
                        } else {
                            staging.getFilesToAdd().add(fileToAdd);
                            fileWriter.saveStaging(staging);
                            return true;
                        }
                    }
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
