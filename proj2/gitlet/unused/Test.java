import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Test {
    static String getWorkingDirectory() {
        return System.getProperty("user.dir");
    }
    
    private static void getFiles(String absolutePath, String reletivePath, List<String> fileList) {
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
                    if (!file.getAbsolutePath().equals("." + separator + ".gitlet")) {
                        String nextReletivePath = reletivePath + file.getName() + separator;
                        getFiles(file.getAbsolutePath(), nextReletivePath, fileList);
                    } else {
                        continue;
                    }
                }
            }
        }
    }
    
    public static void main(String[] args) {
        List<String> fileList = new ArrayList<>();
        // getFiles(".", fileList);
        getFiles(getWorkingDirectory(), "", fileList);
        for (String f : fileList) {
            System.out.println(f);
        }
    }
}