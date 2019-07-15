package gitlet.commands;

import gitlet.FileWriterFactory;
import gitlet.FileSystemWriter;
import gitlet.FileOriginWriter;
import gitlet.Staging;
import gitlet.Commit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class StatusCommand implements Command {
    private FileOriginWriter fileWriter;
    
    public StatusCommand(){
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
            // get current branch ref
            String currentBranch = fileWriter.getCurrentBranchRef().replace(".gitlet/refs/heads/", "");
            // get all branches
            List<String> allBranches = Arrays.asList(fileWriter.getAllBranches());
            Collections.sort(allBranches);
            
            // print all branches, sorted alphabetically, with 
            // an asterisk next to current branch
            System.out.println("=== Branches ===");
            for (String branch : allBranches){
                if (branch.equals(currentBranch)){
                    branch = "*" + branch;
                }
                System.out.println(branch);
            }
            System.out.println();
            
            // get staging
            Staging staging = fileWriter.recoverStaging();
            // print files in filesToAdd.
            System.out.println("=== Staged Files ===");
            if (staging.getFilesToAdd().size() > 0){
                List<String> filesToAdd = staging.getFilesToAdd();
                Collections.sort(filesToAdd);
                for (String file : filesToAdd){
                    System.out.println(file);
                }
            }
            System.out.println();
            
            //print files in filesToRm
            System.out.println("=== Removed Files ===");
            if (staging.getFilesToRm().size() > 0){
                List<String> filesToRm = staging.getFilesToRm();
                Collections.sort(filesToRm);
                for (String file : staging.getFilesToRm()){
                    System.out.println(file);
                }
            }
            
            // recover current commit
            String currentCommitId = fileWriter.getCurrentHeadPointer();
            Commit currentHead = fileWriter.recoverCommit(currentCommitId);
            String currentCommitObjectsFolder = ".gitlet/objects/" + currentCommitId;
            List<String> fileList = new ArrayList<>();
            FileSystemWriter.getFiles(fileWriter.getWorkingDirectory(), "", fileList);
            List<String> untrackedFiles = new ArrayList<>();
            List<String> trackedAddNRmFiles = new ArrayList<>();
            while (fileList.size() != 0) {
                String fileName = fileList.remove(0);
                // modify untrackedFiles
                if (!currentHead.getFilePointers().containsKey(fileName)) {
                    if (staging.getFilesToRm().size() > 0){
                        if (!staging.getFilesToRm().contains(fileName)) {
                            untrackedFiles.add(fileName);
                        }
                    } else {
                        untrackedFiles.add(fileName);
                    }
                }
            }
            for (String fileName : currentHead.getFilePointers().keySet()) {
                trackedAddNRmFiles.add(fileName);
            }
            if (staging.getFilesToAdd().size() > 0){
                for (String fileName : staging.getFilesToAdd()) {
                    trackedAddNRmFiles.add(fileName);
                }
            }
            Collections.sort(trackedAddNRmFiles);
            
            // print files Modifications Not Staged For Commit
            System.out.println("=== Modifications Not Staged For Commit ===");
            for (String fileName : trackedAddNRmFiles) {
                if (!fileList.contains(fileName)) {
                    System.out.println(fileName + " (deleted)");
                } else if (!fileWriter.filesEqual(fileName, currentCommitObjectsFolder + "/" + fileName)) {
                    System.out.println(fileName + " (modified)");
                }
            }
            
            // print files untracked
            System.out.println("=== Untracked Files ===");
            for (String fileName : untrackedFiles) {
                System.out.println(fileName);
            }
            return true;
        }
    }
}
