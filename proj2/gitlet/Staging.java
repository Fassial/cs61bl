package gitlet;

import java.util.ArrayList;
import java.util.List;

public class Staging {
    private List<String> filesToAdd;
    private List<String> filesToRm;
    
    public Staging(){
        this.filesToAdd = new ArrayList<String>();
        this.filesToRm = new ArrayList<String>();
    }
    
    public List<String> getFilesToAdd() {
        return this.filesToAdd;
    }
    public void setFilesToAdd(List<String> filesToAdd) {
        this.filesToAdd = filesToAdd;
    }
    
    public List<String> getFilesToRm() {
        return this.filesToRm;
    }
    public void setFilesToRm(List<String> filesToRm) {
        this.filesToRm = filesToRm;
    }
}
