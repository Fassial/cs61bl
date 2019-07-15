package gitlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileSystemWriter implements FileOriginWriter {
    private static final Logger fLogger = Logger.getLogger(Commit.class.getPackage().getName());
    
    @Override
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
    
    @Override
    public void createDirectory(String dirName) {
        // if don't exist, just create one.
        File f = new File(dirName);
        if (!f.exists()) {
            f.mkdirs();
        }
    }
    
    /*********************************
     * Replaces all text in the existing 
     * file with the given text.
     *********************************/
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
    
    @Override
    public boolean exists(String name) {
        return new File(name).exists();
    }
    
    @Override
    public boolean canWrite(String name) {
        return new File(name).canWrite();
    }
    
    @Override
    public boolean isDirectory(String name) {
        return new File(name).isDirectory();
    }
    
    /*********************************
     * Get the reletive path of all files 
     * under the current path.
     *********************************/
    public static void getFiles(String absolutePath, String reletivePath, List<String> fileList) {
        File rootFile = new File(absolutePath);
        File[] files = rootFile.listFiles();
        String separator = System.getProperty("file.separator");
        for (File file : files) {
            if (!file.exists()) {
                throw new NullPointerException("Cannot find " + file);
            } else if (file.isFile()) {
                fileList.add(reletivePath + file.getName());
            } else {
                if (file.isDirectory()) {
                    if (!file.getName().equals(".gitlet")) {
                        String nextReletivePath = reletivePath + file.getName() + separator;
                        getFiles(file.getAbsolutePath(), nextReletivePath, fileList);
                    } else {
                        continue;
                    }
                }
            }
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Commit recoverCommit(String id) {
        String objDir = ".gitlet/objects/" + id;
        File d = new File(objDir);
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
                        parent = recoverCommit(parentId);
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
    
    @Override
    public void saveCommit(Commit commit) {
        String directory = ".gitlet/objects/" + commit.getId();
        String filename = directory + "/" + commit.getId();
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
    
    @Override
    public String getCurrentBranchRef() {
        String ref = getText(".gitlet/HEAD").replace("ref: ", "");
        return ref;
    }

    @Override
    public String getCurrentHeadPointer() {
        String head = getText(getCurrentBranchRef());
        return head;
    }

    @Override
    public String getText(String fileName) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(fileName));
            return new String(encoded, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public String getWorkingDirectory() {
        return System.getProperty("user.dir");
    }

    @Override
    public void saveStaging(Staging staging) {
        String filename = ".gitlet/objects/staging";

        try (OutputStream file = new FileOutputStream(filename);
                OutputStream buffer = new BufferedOutputStream(file);
                ObjectOutput output = new ObjectOutputStream(buffer);) {
            output.writeObject(staging.getFilesToAdd());
            output.writeObject(staging.getFilesToRm());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Staging recoverStaging() {
        String filename = ".gitlet/objects/staging";
        Staging recovered = null;
        File f = new File(filename);
        if (f.exists()) {
            try (InputStream file = new FileInputStream(filename);
                 InputStream buffer = new BufferedInputStream(file);
                 ObjectInput input = new ObjectInputStream(buffer);) {
                List<String> filesToAdd = (List<String>) input.readObject();
                List<String> filesToRm = (List<String>) input.readObject();
                recovered = new Staging();
                recovered.setFilesToAdd(filesToAdd);
                recovered.setFilesToRm(filesToRm);
            } catch (ClassNotFoundException ex) {
                fLogger.log(Level.SEVERE, "Cannot perform input. Class not found.", ex);
            } catch (IOException ex) {
                fLogger.log(Level.SEVERE, "Cannot perform input.", ex);
            }
            return recovered;
        } else {
            System.out.println("Staging object not found!");
            return null;
        }
    }
    
    @Override
    public String[] getAllBranches() {
        return new File(".gitlet/refs/heads").list();
    }

    @Override
    public long lastModified(String name) {
        return new File(name).lastModified();
    }

    @Override
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
    
    @Override
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
    
    /**
     * delete dir and including files
     *
     * @param dir
     *        deleteCurrDir
     * @return true / false
     */
    @Override
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
    
    @Override
    public String getBranchHead(String branch) {
        String path = ".gitlet/refs/heads/" + branch;
        String head = getText(path);
        return head;
    }

    @Override
    public void makeBranchHead(String branch) {
        String path = ".gitlet/refs/heads/" + branch;
        if (exists(path)) {
            createFile(".gitlet/HEAD", "ref: " + path);
        }
    }

    @Override
    public String getCurrentBranch() {
        return getCurrentBranchRef().replace(".gitlet/refs/heads/", "");
    }

    @Override
    public boolean filesEqual(String a, String b) {
        File file1 = new File(a);
        File file2 = new File(b);

        if (file1.length() != file2.length()) {
            return false;
        }
        try (InputStream in1 = new BufferedInputStream(new FileInputStream(file1));
             InputStream in2 = new BufferedInputStream(new FileInputStream(file2));) {
            int value1, value2;
            do {
                // since we're buffered read() isn't expensive
                value1 = in1.read();
                value2 = in2.read();
                if (value1 != value2) {
                    return false;
                }
            } while (value1 >= 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // since we already checked that the file sizes are equal
        // if we're here we reached the end of both files without a mismatch
        return true;
    }
    
    @Override
    public String[] getAllCommitIds() {
        File objects = new File(".gitlet/objects");
        
        //get everything gut the "staging" file
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if ("staging".equals(name)) {
                    return false;
                }
                return true;
            }
        };
        return objects.list(filter);
    }

    @Override
    public void deleteBranch(String branch) {
        File f = new File(".gitlet/refs/heads/" + branch);
        f.delete();
    }
}
