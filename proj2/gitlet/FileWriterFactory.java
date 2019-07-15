package gitlet;

public class FileWriterFactory {
    private static FileOriginWriter _instance;
    
    public static FileOriginWriter getWriter() {
        if (_instance == null) {
            _instance = new FileSystemWriter();
        }
        return _instance;
    }
    
    /*********************************
     * this method can change the 
     * default set in FileSystemWriter.
     *********************************/
    public static void setWriter(FileOriginWriter instance) {
        _instance = instance;
    }
    
    public static void useDefault() {
        _instance = new FileSystemWriter();
    }
}
