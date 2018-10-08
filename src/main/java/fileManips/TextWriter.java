package fileManips;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by evarmic on 03-Oct-18.
 */
public class TextWriter {
    public static void writeTo(File textFile, String content) throws IOException {
        FileOutputStream fos = new FileOutputStream(textFile);

        if(textFile.getName().contains(".docx")) {
            XWPFDocument document = new XWPFDocument();

            String[] tokens = content.split("\\n");

            for(String token: tokens) {
                XWPFParagraph paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();
                run.setText(token);
            }
            
            document.write(fos);

        } else {
            PrintWriter pw = new PrintWriter(fos);
            pw.print(content);
            pw.close();
        }

        fos.close();
    }
}
