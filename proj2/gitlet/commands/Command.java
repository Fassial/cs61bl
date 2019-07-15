package gitlet.commands;

public interface Command {
    public boolean isDangerous();
    public boolean execute();
    public boolean needInitDir();
    
    /** find Initial Gitlet Directory
    public boolean findInitDir() {
        File rootFile = new File(System.getProperty("user.dir"));
        String[] fileNames = rootFile.list();
        for (String fileName : fileNames) {
            if (fileName.equals(".gitlet")) {
                return true;
            }
        }
        return false;
    }
    **/
}
