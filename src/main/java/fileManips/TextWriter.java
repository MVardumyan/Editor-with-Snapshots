package fileManips;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by evarmic on 03-Oct-18.
 */
public class TextWriter {
    public static void writeTo(File textFile, String content) throws IOException {
        PrintWriter pw = new PrintWriter(textFile);
        pw.print(content);
        pw.close();
    }
}
