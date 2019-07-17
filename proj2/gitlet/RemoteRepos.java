package gitlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RemoteRepos {
    private String configFile;
    private HashMap<String,String> remoteRepos;
    private static final Logger fLogger = Logger.getLogger(Commit.class.getPackage().getName());
    
    private RemoteRepos() {
        this.configFile = ".gitlet" + File.separator + "config";
    }
    
    public static RemoteRepos getCurrentRemoteRepos() {
        RemoteRepos currentRemoteRepos = new RemoteRepos();
		if (!new File(currentRemoteRepos.configFile).exists()) {
			currentRemoteRepos.remoteRepos = new HashMap<>();
		} else {
			if ("".equals(currentRemoteRepos.getText(currentRemoteRepos.configFile))) {
				currentRemoteRepos.remoteRepos = new HashMap<>();
			} else {
				try (InputStream file = new FileInputStream(currentRemoteRepos.configFile);
					 InputStream buffer = new BufferedInputStream(file);
					 ObjectInput input = new ObjectInputStream(buffer);) {
					 currentRemoteRepos.remoteRepos = (HashMap<String,String>) input.readObject();
				} catch (ClassNotFoundException e) {
					fLogger.log(Level.SEVERE, "Cannot perform input. Class not found.", e);
				} catch (IOException e) {
					fLogger.log(Level.SEVERE, "Cannot perform input.", e);
				}
			}
		}
        return currentRemoteRepos;
    }
    
    public HashMap<String,String> getRemoteRepos() {
        return this.remoteRepos;
    }
    
    public void createFile(String fileName, String fileText) {
        // if don't exist, just create one.
        File f = new File(fileName);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        this.writeFile(fileName, fileText);
    }
    
    public boolean saveCurrentRemoteRepos() {
        try (OutputStream file = new FileOutputStream(this.configFile);
             OutputStream buffer = new BufferedOutputStream(file);
             ObjectOutput output = new ObjectOutputStream(buffer);) {
            output.writeObject(this.remoteRepos);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean addRemoteRepo(String remoteName, String remoteRepoName) {
        if (!this.remoteRepos.containsKey(remoteName)) {
            this.remoteRepos.put(remoteName, remoteRepoName);
            return this.saveCurrentRemoteRepos();
        } else {
            return false;
        }
    }
    
    public boolean remoteRemoteRepo(String remoteName) {
        if (!this.remoteRepos.containsKey(remoteName)) {
            return false;
        } else {
            this.remoteRepos.remove(remoteName);
            return this.saveCurrentRemoteRepos();
        }
    }
    
    private String getText(String fileName) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(fileName));
            return new String(encoded, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    public String[] getAllRemoteRepoBranches(String remoteName) {
        if (!this.remoteRepos.containsKey(remoteName)) {
            return null;
        } else {
            String remoteRepoName = this.remoteRepos.get(remoteName);
            String path = remoteRepoName + File.separator + "refs" + File.separator + "heads";
            return new File(path).list();
        }
    }
    
    public String getRemoteBranchHead(String remoteName, String branch) {
        if (!this.remoteRepos.containsKey(remoteName)) {
            return null;
        } else {
            String remoteRepoName = this.remoteRepos.get(remoteName);
            String path = remoteRepoName + File.separator + "refs" + File.separator + "heads" + File.separator + branch;
			if (!new File(path).exists()) {
				return null;
			}
            String head = getText(path);
            return head;
        }
    }
	
	public boolean setRemoteBranchHead(String remoteName, String branch, String commitId) {
        if (!this.remoteRepos.containsKey(remoteName)) {
            return false;
        } else {
            String remoteRepoName = this.remoteRepos.get(remoteName);
            String path = remoteRepoName + File.separator + "refs" + File.separator + "heads" + File.separator + branch;
            createFile(path, commitId);
            return true;
        }
    }
    
    public boolean makeBranchHead(String remoteName, String branch) {
        if (!this.remoteRepos.containsKey(remoteName)) {
            return false;
        } else {
            String remoteRepoName = this.remoteRepos.get(remoteName);
            String path = remoteRepoName + File.separator + "refs" + File.separator + "heads" + File.separator + branch;
            String writeText = ".gitlet/refs/heads/" + branch;
            if (new File(path).exists()) {
                createFile(remoteRepoName + File.separator + "HEAD", "ref: " + writeText);
            }
            return true;
        }
    }
    
    public void saveCommit(String remoteRepoName, Commit commit) {
        String directory = remoteRepoName + File.separator + "objects" + File.separator + commit.getId();
        String filename = directory + File.separator + commit.getId();
        File d = new File(directory);
        File f = new File(filename);
        if (!f.exists()) {
            if (!d.exists())
                d.mkdir();
            try (OutputStream file = new FileOutputStream(filename);
                 OutputStream buffer = new BufferedOutputStream(file);
                 ObjectOutput output = new ObjectOutputStream(buffer);) {
                if (commit.getParent() != null) {
                    output.writeObject(commit.getParent().getId());
                } else {
                    output.writeObject("null");
                }
                output.writeObject(commit.getId());
                output.writeObject(commit.getMessage());
                output.writeObject(commit.getTimeStamp());
                output.writeObject(commit.getFilePointers());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Id: " + commit.getId() + " already exists!");
        }
    }
    
    @SuppressWarnings("unchecked")
    public Commit recoverCommit(String remoteRepoName, String id) {
        String objDir = remoteRepoName + File.separator + "objects" + File.separator + id;
        File d = new File(objDir);
		System.out.println(objDir);
        if (!d.exists()) {
            throw new IllegalArgumentException("commit not found!");
            // return null;
        } else {
            String filename = objDir + "/" + id;
            Commit recovered = null;
            Commit parent = null;
            Long timeStamp;
            String message;
            HashMap<String, String> filePointers;
            
            File f = new File(filename);
            if (!f.exists()) {
                return null;
            } else {
                try (InputStream file = new FileInputStream(filename);
                     InputStream buffer = new BufferedInputStream(file);
                     ObjectInput input = new ObjectInputStream(buffer);) {
                    // deserialize the List
                    String parentId = (String) input.readObject();
                    if (!parentId.equals("null")) {
                        parent = recoverCommit(remoteRepoName, parentId);
                    }
                    input.readObject();        // this is the id, we already have it...
                    message = (String) input.readObject();
                    timeStamp = (Long) input.readObject();
                    filePointers = (HashMap<String, String>) input.readObject();
                    recovered = new Commit(parent, timeStamp, message, filePointers);
                } catch (ClassNotFoundException e) {
                    fLogger.log(Level.SEVERE, "Cannot perform input. Class not found.", e);
                } catch (IOException e) {
                    fLogger.log(Level.SEVERE, "Cannot perform input.", e);
                }
                return recovered;
            }
        }
    }
    
    public void copyFile(String filePath, String destPath) {
        File dest = new File(destPath);
        File source = new File(filePath);
        
        //check if destination directory exists and create if it doesn't
        if (destPath.lastIndexOf("/") > -1) {
            File destDir = new File(destPath.substring(0, destPath.lastIndexOf("/")));
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
        }
        
        try {
            Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void copyDirectory(String dirPath, String destPath) {
        File dest = new File(destPath);
        File source = new File(dirPath);
        //check if destination directory exists and create if it doesn't
        if (destPath.lastIndexOf("/") > -1) {
            File destDir = new File(destPath.substring(0, destPath.lastIndexOf("/")));
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
        }
        if (source.isDirectory()) {
            String[] sourceFiles = source.list();
			if (!dest.exists()) {
                dest.mkdirs();
            }
            for (String sourceFile : sourceFiles) {
				// System.out.println(destPath + File.separator + sourceFile);
                copyDirectory(dirPath + File.separator + sourceFile, destPath + File.separator + sourceFile);
            }
        } else if (source.isFile()) {
			/*
            try {
                Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
			*/
			copyFile(dirPath, destPath);
        }
    }
    
    public boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    public boolean deleteDirectory(String dir, boolean deleteCurrDir) {
        // not end with separator, add it
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        // not exist or not a dir -> exit
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            return false;
        }
        boolean flag = true;
        // delete all dirs and files in root dir
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // delete child files
            if (files[i].isFile()) {
                flag = this.deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
            // delete child dirs
            else if (files[i].isDirectory()) {
                flag = this.deleteDirectory(files[i].getAbsolutePath(), true);
                if (!flag) {
                    break;
                }
            }
        }
        if (!flag) {
            return false;
        } else {
            if (deleteCurrDir) {
                // remove current dir
                if (dirFile.delete()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }
    
    public boolean inRemoteRepos(String remoteName) {
        return this.remoteRepos.containsKey(remoteName);
    }
    
    public boolean remoteRepoExist(String remoteName) {
        if (inRemoteRepos(remoteName)) {
            String path = this.remoteRepos.get(remoteName);
            return new File(path).exists();
        } else {
            return false;
        }
    }
    
    private void writeFile(String fileName, String fileText) {
        FileWriter fw = null;
        try {
            File f = new File(fileName);
            fw = new FileWriter(f, false);        // ?
            fw.write(fileText);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
        
    public boolean fetchRemoteRepoBranch(String remoteName, String branch) {
        String remoteBranchHead = getRemoteBranchHead(remoteName, branch);
        if (remoteBranchHead == null) {
            return false;
        } else {
            String remoteRepoName = this.remoteRepos.get(remoteName);
            Commit headCommit = recoverCommit(remoteRepoName, remoteBranchHead);
            Commit currentCommit = headCommit;
            String destBranchRefDirectory = ".gitlet" + File.separator + "refs" + File.separator + "heads" + File.separator + remoteName;
			String destRemoteBranchRefDirectory = ".gitlet" + File.separator + "refs" + File.separator + "remotes" + File.separator + remoteName;
			String destRootDirectory = ".gitlet" + File.separator + "objects";
            /*
			if (new File(destRootDirectory).exists()) {
                deleteDirectory(destRootDirectory, false);
            }
			*/
            while (currentCommit != null) {
                String sourceDirectory =  remoteRepoName + File.separator + "objects" + File.separator + currentCommit.getId();
                String destDirectory =  destRootDirectory + File.separator + currentCommit.getId();
                copyDirectory(sourceDirectory, destDirectory);
                currentCommit = currentCommit.getParent();
            }
			if (!(new File(destRemoteBranchRefDirectory).exists())) {
				new File(destRemoteBranchRefDirectory).mkdirs();
			}
			if (!(new File(destBranchRefDirectory).exists())) {
				new File(destBranchRefDirectory).mkdirs();
			}
            writeFile(destRemoteBranchRefDirectory + File.separator + branch, headCommit.getId());
			writeFile(destBranchRefDirectory + File.separator + branch, headCommit.getId());
			String FETCH_HEAD_Path = ".gitlet" + File.separator + "FETCH_HEAD";
			String FETCH_HEAD_Text = headCommit.getId() + "," + branch + "," + remoteName + "," + remoteRepoName;
			writeFile(FETCH_HEAD_Path, FETCH_HEAD_Text);
            return true;
        }
    }
}
