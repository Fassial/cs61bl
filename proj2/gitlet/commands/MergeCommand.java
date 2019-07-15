package gitlet.commands;

import gitlet.Commit;
import gitlet.FileWriterFactory;
import gitlet.FileOriginWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MergeCommand implements Command {
    private String branch;
    private FileOriginWriter fileWriter;
    private String stdOutUncommitedChanges;
    private String stdOutBranchNotExists;
    private String stdOutIsCurrentBranch;
    private String stdOutUntrackedOw;
    
    public MergeCommand(String branch) {
        this.branch = branch;
        this.fileWriter = FileWriterFactory.getWriter();
        this.stdOutUncommitedChanges = "You have uncommitted changes.";
        this.stdOutBranchNotExists = "A branch with that name does not exist.";
        this.stdOutIsCurrentBranch = "Cannot merge a branch with itself.";
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
            // get Staging 
            Staging staging = fileWriter.recoverStaging();
            //    if stagins is not empty, print error message and return false
            if (staging.getFilesToRm().size() != 0 || staging.getFilesToAdd().size() != 0 ){
                System.out.println(this.stdOutUncommitedChanges);
                return false;
            } else {
                if (!fileWriter.exists(".gitlet/refs/heads/" + branch)) {
                    // if branch doesn't exist, print message and fail
                    System.out.println(this.stdOutBranchNotExists);
                    return false;
                } else {
                    String currentBranch = fileWriter.getCurrentBranch();
                    if (branch.equals(currentBranch)) {
                        // if branch is the current branch, print message and fail;
                        System.out.println(this.stdOutIsCurrentBranch);
                        return false;
                    } else {
                        // get the current commit, the other branch commit, and the splitpoint
                        Commit current = fileWriter.recoverCommit(fileWriter.getCurrentHeadPointer());
                        Commit other = fileWriter.recoverCommit(fileWriter.getBranchHead(branch));
                        Commit split = fileWriter.recoverCommit(current.findSplitPoint(other));
                        if (split.equals(other)) {
                            // If the split point is the same commit as the given branch, then we do nothing
                            System.out.println("Given branch is an ancestor of the current branch.");
                            return true;
                        } else if (split.equals(current)) {
                            // If the split point is the current branch, then the current branch is set to the same commit as the given branch
                            if (new CheckoutBranchCommand(branch).execute()) {
                                // successfully execute
                                System.out.println("Current branch fast-forwarded.");
                                return true;
                            } else {
                                // inner error.
                                return false;
                            }
                        } else {
                            List<String> currentModOnly = new ArrayList<String>();
                            List<String> otherModOnly = new ArrayList<String>();
                            List<String> currentAdd = new ArrayList<String>();
                            List<String> otherAdd = new ArrayList<String>();
                            List<String> currentAddOnly = new ArrayList<String>();
                            List<String> otherAddOnly = new ArrayList<String>();
                            List<String> currentSub = new ArrayList<String>();
                            List<String> otherSub = new ArrayList<String>();
                            List<String> conflictFiles = new ArrayList<String>();
                            HashMap<String, String> currentFP = current.getFilePointers();
                            HashMap<String, String> otherFP = other.getFilePointers();
                            HashMap<String, String> splitFP = split.getFilePointers();
                            
                            // iterate through filePointers, save files that have changed in either commit
                            // since split
                            if ((splitFP != null) && (splitFP.keySet().size() > 0)) {
                                for (String file : splitFP.keySet()) {
                                    if (currentFP.containsKey(file) && otherFP.containsKey(file)) {
                                        String currentCommit = currentFP.get(file);
                                        String splitCommit = splitFP.get(file);
                                        String otherCommit = otherFP.get(file);
                                        if ((!currentCommit.equals(splitCommit)) && otherCommit.equals(splitCommit)) {
                                            // Any files that have been modified in the current branch 
                                            // but not in the given branch since the split point 
                                            currentModOnly.add(file);
                                        } else if ((!otherCommit.equals(splitCommit)) && currentCommit.equals(splitCommit)) {
                                            // Any files that have been modified in the given branch since the split point, 
                                            // but not modified in the current branch since the split point
                                            otherModOnly.add(file);
                                        } else if ((!otherCommit.equals(splitCommit)) && (!currentCommit.equals(splitCommit))) {
                                            // Any files modified in different ways in the current and given branches are in conflict. 
                                            // the contents of both are changed and different from other
                                            conflictFiles.add(file);
                                        }
                                    } else {
                                        if (currentFP.containsKey(file)) {
                                            String currentCommit = currentFP.get(file);
                                            String splitCommit = splitFP.get(file);
                                            if (currentCommit.equals(splitCommit)) {
                                                // Any files present at the split point, unmodified in the current branch, 
                                                // and absent in the given branch should be removed (and untracked).
                                                otherSub.add(file);
                                            } else {
                                                // Any files modified in different ways in the current and given branches are in conflict. 
                                                // the contents of one are changed and the other is deleted
                                                conflictFiles.add(file);        // not in otherFP
                                            }
                                        } else if (otherFP.containsKey(file)) {
                                            String splitCommit = splitFP.get(file);
                                            String otherCommit = otherFP.get(file);
                                            if (otherCommit.equals(splitCommit)) {
                                                // Any files present at the split point, unmodified in the given branch, 
                                                // and absent in the current branch should remain absent.
                                                currentSub.add(file);
                                            } else {
                                                // Any files modified in different ways in the current and given branches are in conflict. 
                                                // the contents of one are changed and the other is deleted
                                                conflictFiles.add(file);        // not in currentFP
                                            }
                                        } else {
                                            // both delete, just ignore
                                        }
                                    }
                                }
                            }
                            if ((currentFP != null) && (currentFP.keySet().size() > 0)) {
                                for (String file : currentFP.keySet()) {
                                    if (!splitFP.containsKey(file)) {
                                        currentAdd.add(file);
                                    }
                                }
                            }
                            if ((otherFP != null) && (otherFP.keySet().size() > 0)) {
                                for (String file : otherFP.keySet()) {
                                    if (!splitFP.containsKey(file)) {
                                        otherAdd.add(file);
                                    }
                                }
                            }
                            if ((currentAdd != null) && (currentAdd.keySet().size() > 0)) {
                                for (String file : currentAdd) {
                                    if (otherAdd.contains(file) {
                                        // Any files modified in different ways in the current and given branches are in conflict. 
                                        // the file was absent at the split point and have different contents in the given and current branches
                                        conflictFiles.add(file);
                                    } else {
                                        // Any files that were not present at the split point and are present
                                        // only in the current branch should remain as they are.
                                        currentAddOnly.add(file);
                                    }
                                }
                            }
                            if ((otherAdd != null) && (otherAdd.keySet().size() > 0)) {
                                for (String file : otherAdd) {
                                    if (!currentAdd.contains(file)) {
                                        // Any files that were not present at the split point and are present
                                        // only in the current branch should remain as they are.
                                        otherAddOnly.add(file);
                                    }
                                }
                            }
                            
                            List<String> fileList = new ArrayList<>();
                            FileSystemWriter.getFiles(fileWriter.getWorkingDirectory(), "", fileList);
                            boolean untrackedFilesOw = false;
                            if ((otherAddOnly != null) && (otherAddOnly.keySet().size() > 0)) {
                                for (String file : otherAddOnly) {
                                    if (fileList.contains(file)) {
                                        untrackedFilesOw = true;
                                        break;
                                    }
                                }
                            }
                            if ((conflictFiles != null) && (conflictFiles.keySet().size() > 0)) {
                                for (String file : conflictFiles) {
                                    if (fileList.contains(file)) {
                                        untrackedFilesOw = true;
                                        break;
                                    }
                                }
                            }
                            if (untrackedFilesOw) {
                                System.out.println(this.stdOutUntrackedOw);
                                return false;
                            } else {
                                /**  otherModOnly
                                 * Any files that have been modified in the given branch 
                                 * since the split point, but not modified in the current 
                                 * branch since the split point should be changed to their 
                                 * versions in the given branch (checked out from the commit 
                                 * at the front of the given branch). These files should 
                                 * then all be automatically staged.
                                 **/
                                if ((otherModOnly != null) && (otherModOnly.keySet().size() > 0)) {
                                    for (String file : otherModOnly) {
                                        String commitId = otherFP.get(file);
                                        // appear in add (modified), need commit later
                                        // new AddCommand(file).execute();
                                        new CheckoutFileCommand(commitId, file).execute();
                                        staging.getFilesToAdd().add(file);
                                        fileWriter.copyFile(file, ".gitlet/staging/filesToAddFolder/" + file);
                                    }
                                }
                                
                                /**  otherAddOnly
                                 * Any files that were not present at the split point and 
                                 * are present only in the given branch should be checked 
                                 * out and staged.
                                 **/
                                if ((otherAddOnly != null) && (otherAddOnly.keySet().size() > 0)) {
                                    for (String file : otherAddOnly) {
                                        String commitId = otherFP.get(file);
                                        // appear in add, need commit later
                                        new CheckoutFileCommand(commitId, file).execute();
                                        // new AddCommand(file).execute();
                                        staging.getFilesToAdd().add(file);
                                        fileWriter.copyFile(file, ".gitlet/staging/filesToAddFolder/" + file);
                                    }
                                }
                                
                                /**  otherSub
                                 * Any files present at the split point, unmodified in the 
                                 * current branch, and absent in the given branch should 
                                 * be removed (and untracked).
                                 **/
                                if ((otherSub != null) && (otherSub.keySet().size() > 0)) {
                                    for (String file : otherSub) {
                                        // appear in 
                                        // new RmCommand(file).execute();
                                        staging.getFilesToRm().add(file);
                                        fileWriter.copyFile(file ".gitlet/staging/filesToRmFolder/" + file);
                                    }
                                }
                                
                                /**  conflictFiles
                                 * Any files modified in different ways in the current and 
                                 * given branches are in conflict. “Modified in different ways” 
                                 * can mean that the contents of both are changed and different 
                                 * from other, or the contents of one are changed and the other 
                                 * is deleted, or the file was absent at the split point and 
                                 * have different contents in the given and current branches. 
                                 * In this case, replace the contents of the conflicted file 
                                 * with the following, but do not stage the result.
                                 **/
                                boolean resultInConflict = false;
                                if ((conflictFiles != null) && (conflictFiles.keySet().size() > 0)) {
                                    resultInConflict = true;
                                    for (String file : conflictFiles) {
                                        if (currentFP.containsKey(file) && otherFP.containsKey(file)) {
                                            // the contents of both are changed and different from other 
                                            // & the file was absent at the split point and have different 
                                            // contents in the given and current branches
                                            String currentCommitId = currentFP.get(file);
                                            String currentFilePath = ".gitlet/objects/" + currentCommitId + "/" + file;
                                            String otherCommitId = otherFP.get(file);
                                            String otherFilePath = ".gitlet/objects/" + otherCommitId + "/" + file;
                                            String currentFile = fileWriter.getText(currentFilePath);
                                            String otherFile = fileWriter.getText(otherFilePath);
                                            String writeText = "<<<<<<< HEAD\n" + currentFile + "=======\n" + otherFile + ">>>>>>>";
                                            fileWriter.deleteFile(file);
                                            fileWriter.createFile(file, writeText);
                                            /**  do not stage the result.
                                             * staging.getFilesToAdd().add(file);
                                             * fileWriter.copyFile(file, ".gitlet/staging/filesToAddFolder/" + file);
                                             **/
                                        } else {
                                            // the contents of one are changed and the other is deleted
                                            if (currentFP.containsKey(file)) {
                                                String currentCommitId = currentFP.get(file);
                                                String currentFilePath = ".gitlet/objects/" + currentCommitId + "/" + file;
                                                String otherCommitId = otherFP.get(file);
                                                String otherFilePath = ".gitlet/objects/" + otherCommitId + "/" + file;
                                                String currentFile = fileWriter.getText(currentFilePath);
                                                String otherFile = "";
                                                String writeText = "<<<<<<< HEAD\n" + currentFile + "=======\n" + otherFile + ">>>>>>>";
                                                fileWriter.deleteFile(file);
                                                fileWriter.createFile(file, writeText);
                                                /**  do not stage the result.
                                                 * staging.getFilesToAdd().add(file);
                                                 * fileWriter.copyFile(file, ".gitlet/staging/filesToAddFolder/" + file);
                                                 **/
                                            } else if (otherFP.containsKey(file)) {
                                                String currentCommitId = currentFP.get(file);
                                                String currentFilePath = ".gitlet/objects/" + currentCommitId + "/" + file;
                                                String otherCommitId = otherFP.get(file);
                                                String otherFilePath = ".gitlet/objects/" + otherCommitId + "/" + file;
                                                String currentFile = "";
                                                String otherFile = fileWriter.getText(otherFilePath);
                                                String writeText = "<<<<<<< HEAD\n" + currentFile + "=======\n" + otherFile + ">>>>>>>";
                                                fileWriter.deleteFile(file);
                                                fileWriter.createFile(file, writeText);
                                                /**  do not stage the result.
                                                 * staging.getFilesToAdd().add(file);
                                                 * fileWriter.copyFile(file, ".gitlet/staging/filesToAddFolder/" + file);
                                                 **/
                                            }
                                        }
                                    }
                                }
                                
                                // Commit
                                if (resultInConflict) {
                                    new CommitCommand("Encountered a merge conflict.").execute();
                                } else {
                                    new CommitCommand("Merged " + currentBranch + " with " + branch + ".").execute();
                                }
                                for (String file : otherSub) {
                                    fileWriter.deleteFile(file);
                                }
                                return true;
                            }
                        }
                    }
                }
            }
        }
    }
}
