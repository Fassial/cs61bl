package gitlet.commands;

import gitlet.Commit;
import gitlet.FileWriterFactory;
import gitlet.FileOriginWriter;
import gitlet.Staging;

public class InitCommand implements Command {
    private String userDir;
    private FileOriginWriter fileWriter;
    
    public InitCommand(){
        //these could be injected if I wanted to...
        this.fileWriter = FileWriterFactory.getWriter();
        this.userDir = System.getProperty("user.dir");
    }
    
    @Override
    public boolean isDangerous() {
        return false;
    }
    
    @Override
    public boolean needInitDir() {
        return false;
    }
    
    @Override
    public boolean execute() {
        if (this.needInitDir() != fileWriter.exists(".gitlet")) {
            // same as git, even if ".gitlet" is a file not a dir, still result in error.
            System.out.println("A gitlet version-control system already exists in the current directory.");
            return false;
        } else {
            /*
            // get a reference to this directory, check if it's writable
            // if it isn't writable, output error messages and return false
            if(!fileWriter.canWrite(userDir)){
                System.err.println("IO ERROR: Failed to create directory: .gitlet");
                return false;
            }
            */
            // if it's writable, create the .gitlet folder and subfolders
            fileWriter.createDirectory(".gitlet/objects");
            fileWriter.createDirectory(".gitlet/refs/heads");
            // create the initial commit
            Commit initialCommit = new Commit(null, System.currentTimeMillis(), "initial commit", null);
            // create the master branch pointing at initial commit
            // save master branch in .gitlet/refs/heads folder
            fileWriter.createFile(".gitlet/refs/heads/master", initialCommit.getId());
			// config for remote
			fileWriter.createFile(".gitlet/config", "");
            // create a new HEAD reference pointing at master branch
            // save HEAD file to .gitlet/HEAD
            fileWriter.createFile(".gitlet/HEAD", "ref: .gitlet/refs/heads/master");
			// save new staging area
            fileWriter.saveStaging(new Staging());
            // create staging dir
            fileWriter.createDirectory(".gitlet/staging/filesToRmFolder");
            fileWriter.createDirectory(".gitlet/staging/filesToAddFolder");
            // save commit to the .gitlet/objects folder
            fileWriter.saveCommit(initialCommit);
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
