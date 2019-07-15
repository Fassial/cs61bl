package gitlet.commands;

import gitlet.Commit;
import gitlet.FileWriterFactory;
import gitlet.FileOriginWriter;

public class FindCommand implements Command {
    String message;
    FileOriginWriter fileWriter;
    
    public FindCommand(String message) {
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
            boolean found = false;
            for (String id : fileWriter.getAllCommitIds()){
                Commit head = fileWriter.recoverCommit(id);
                if (message.equals(head.getMessage())){
                    found = true;
                }
            }
            if (!found) {
                System.out.println("Found no commit with that message.");
            }
            return true;
        }
    }
    
    public FileOriginWriter getFileWriter() {
        return fileWriter;
    }

    public void setFileWriter(FileOriginWriter fileWriter) {
        this.fileWriter = fileWriter;
    }
}
