package testReader;
import java.io.*;
/*
 * This class is used to read the file and return the content of the file.
 * 
 * @param fileName
 * @return fileContent
 * @throws IOException
 * @throws FileNotFoundException
 */

public class TestReader extends BufferedReader {
    private String filePath;
    private char[] fileContent;

    public TestReader(String filePath) throws IOException, FileNotFoundException {
        super(new FileReader(filePath));
        this.filePath = filePath;
        char[] array = new char[100];
        this.read(array);
        this.fileContent = array;   
    }

    public String getFilePath() {
        return this.filePath;
    }

    public char[] getFileContent() {
        return this.fileContent;
    }
    
    /**
     * 
     * @param <T>
     * @return
     */
    public <T> String getType() throws IOException {
        LineNumberReader lnr = new LineNumberReader(this);
        String line = "";
        while(line != null){
            line = lnr.readLine();
            if(line==null){break;}
            /* do stuff */
            return line;
        }
        return line;
    }

    public String readAllLines() throws IOException {
        StringBuilder content = new StringBuilder();
        String line;

        while ((line = this.readLine()) != null) {
            content.append(line);
            content.append(System.lineSeparator());
        }
    
        return content.toString();
    }

}
