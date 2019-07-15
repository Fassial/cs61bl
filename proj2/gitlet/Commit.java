package gitlet;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class Commit implements Serializable {
    private String id;
    private String shortId;
    private Commit parent;
    private Long timeStamp;
    private String message;
    private HashMap<String, String> filePointers;
    
    public Commit() {
        this(null, 0L, "", null);
    }
    
    public Commit(Commit parent, Long timeStamp, String message, HashMap<String, String> filePointers) {
        this.parent = parent;
        this.timeStamp = timeStamp;
        this.message = message;
        this.filePointers = filePointers;
        
        String text = "";
        String parentText = "";
        String filePointersText = "";
        // get parent info and save it into text
        if (parent != null) {
            parentText = Integer.toString(parent.hashCode());
            filePointersText = Integer.toString(parent.filePointersHashCode());
        }
        text = filePointersText + message + timeStamp.toString() + parentText;
        this.id = Hasher.getSha1(text);
        this.shortId = id.substring(1, 10);
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getShortId() {
        return this.shortId;
    }
    
    public Commit getParent() {
        return this.parent;
    }
    
    public Long getTimeStamp() {
        return this.timeStamp;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public HashMap<String, String> getFilePointers() {
        return this.filePointers;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Commit) {
            Commit other = (Commit) o;
            return this.hashCode() == other.hashCode();
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        int pfpHash = (this.parent == null) ? 0 : this.parent.filePointersHashCode();
        int idHash = (this.id == null) ? 0 : this.id.hashCode();
        int msgHash = (this.message == null) ? 0 : this.message.hashCode();
        int tsHash = (this.timeStamp == null) ? 0 : this.timeStamp.hashCode();
        
        return pfpHash ^ idHash ^ msgHash ^ tsHash;
    }
    
    public int filePointersHashCode() {
        if (this.filePointers == null) {
            return 0;
        } else {
            int hash = 0;
            for (String key : this.filePointers.keySet()) {
                hash ^= (key.hashCode() << 1);
                hash ^= this.filePointers.get(key).hashCode();
            }
            return hash;
        }
    }
    
    /*********************************
     * find two Commits' common parent
     *********************************/
    public String findSplitPoint(Commit other) {
        if (other == null) {
            return null;
        } else if (this.equals(other)) {
            return this.id;
        } else if (this.timeStamp >= other.timeStamp) {
            // this is new, go on to this.parent
            return other.findSplitPoint(this.parent);
        } else {
            // other is new, go on to other.parent
            return this.findSplitPoint(other.parent);
        }
    }
}
