package gitlet.commands;

import gitlet.FileWriterFactory;
import gitlet.FileOriginWriter;

public class CheckoutFileCommand implements Command {
    private FileOriginWriter fileWriter;
    private String commitId;
    private String fileName;
    private String stdOutFileNotFound;
    private String stdOutCommitNotFound;
    
    public CheckoutFileCommand(String commitId, String fileName) {
        this.commitId = commitId;
        this.fileName = fileName;
        this.fileWriter = FileWriterFactory.getWriter();
        this.stdOutFileNotFound = "File does not exist in that commit.";
        this.stdOutCommitNotFound = "No commit with that id exists.";
    }
    
    public CheckoutFileCommand(String fileName) {
        this(null, fileName);
        this.stdOutFileNotFound = "File does not exist in that commit.";
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
            if (this.commitId == null) {
                this.commitId = fileWriter.getCurrentHeadPointer();
            }
            String commitPath = ".gitlet/objects/" + commitId;
            String filePath =  commitPath + "/" + fileName;
            if (!fileWriter.exists(commitPath)) {
                // if commit not found, print error messages and return false
                System.out.println(this.stdOutCommitNotFound);
                return false;
            } else if (!fileWriter.exists(filePath)) {
                // if file not found, print error messages and return false
                System.out.println(this.stdOutFileNotFound);
                return false;
            } else {
                fileWriter.copyFile(filePath, fileName);
                return true;
            }
        }
    }
}
