package snapshot;

import java.io.*;

public class TextReader {

    public static String getText(File textFile) throws IOException {
        FileInputStream fis = new FileInputStream(textFile);
        InputStreamReader isr  = new InputStreamReader(fis, "Unicode");
        BufferedReader br  = new BufferedReader(isr);
        StringBuilder text = new StringBuilder();

        String c;

        while ((c = br.readLine()) != null)
            text.append(c + "\n");

        fis.close();
        isr.close();
        br.close();

        return String.valueOf(text);
    }

}