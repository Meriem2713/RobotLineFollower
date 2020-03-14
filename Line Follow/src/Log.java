import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Log {
    private String filename;
    Writer writer;

    public Log(String fn) {
        filename = fn;
    }

    public void initialize() {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
        }catch(Exception e) {

        }
    }

    public void write(String s) {
        try {
            writer.write(s+"\n");
            writer.flush();
        } catch (IOException e) {
        }
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
        }
    }
}