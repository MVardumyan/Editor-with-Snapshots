package snapshot;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by evarmic on 02-Oct-18.
 */
public class Memento implements Serializable {
    private String content;
    private Date date_of_change;

    public Memento(String content) {
        this.content=content;
        date_of_change = Calendar.getInstance().getTime();
    }

    public String getContent() {
        return content;
    }

    public Date getDate_of_change() {
        return date_of_change;
    }
}
