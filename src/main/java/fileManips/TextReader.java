package fileManips;

import java.io.*;
import org.mozilla.universalchardet.ReaderFactory;
import org.mozilla.universalchardet.UniversalDetector;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class TextReader {

    public static String getText(File textFile) throws IOException {
        StringBuilder text = new StringBuilder();

        String c;

        if(textFile.getName().contains(".docx")) {
            XWPFDocument docx = new XWPFDocument(new FileInputStream(textFile));
            XWPFWordExtractor extractor = new XWPFWordExtractor(docx);
            text.append(extractor.getText());
        } else {
            BufferedReader br  = ReaderFactory.createBufferedReader(textFile);
            while ((c = br.readLine()) != null)
                text.append(c + "\n");

            br.close();
        }

        return String.valueOf(text);
    }

    public static String getEncoding(File textFile) throws IOException {
        return UniversalDetector.detectCharset(textFile);
    }

}