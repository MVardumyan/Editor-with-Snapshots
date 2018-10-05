package snapshotManips;

import fileManips.TextWriter;
import snapshot.CareTaker;
import snapshot.Memento;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by evarmic on 05-Oct-18.
 */
public class SnapshotHandler {
    public static String switchTo(Date value, CareTaker careTaker, File currentFile) {
        ArrayList<Memento> snapshots = careTaker.getSnapshots();

        for(Memento snap: snapshots) {
                if(snap.getDate_of_change().equals(value)) {
                    try {
                        TextWriter.writeTo(currentFile, snap.getContent());
                        return snap.getContent();
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                }
        }

        return null;
    }
}
