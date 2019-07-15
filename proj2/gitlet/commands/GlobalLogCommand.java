package gitlet.commands;

import gitlet.Commit;
import gitlet.FileWriterFactory;
import gitlet.FileOriginWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GlobalLogCommand implements Command {
    FileOriginWriter fileWriter;
    
    public GlobalLogCommand(){
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
            for (String id : fileWriter.getAllCommitIds()){
                Commit head = fileWriter.recoverCommit(id);
                System.out.println("===");
                System.out.println("Commit " + head.getId());        
                String date = this.convertTime(head.getTimeStamp());            
                System.out.println(date);
                System.out.println(head.getMessage());    
                System.out.println();
            }
            return true;
        }
    }
    
    private String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
    
    public FileOriginWriter getFileWriter() {
        return fileWriter;
    }

    public void setFileWriter(FileOriginWriter fileWriter) {
        this.fileWriter = fileWriter;
    }
}
