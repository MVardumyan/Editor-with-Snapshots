package snapshot;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by evarmic on 02-Oct-18.
 */
public class CareTaker {
    private static ArrayList<Memento> snapshots;
    private static File log;
    private static int count_of_added_snapshots=0;
    private static int count_of_removed_snapshots=0;

    public CareTaker() {
        snapshots = new ArrayList<Memento>();
        try {
            log = new File("log.ser");

            if (log.exists()) {
                FileInputStream fis = new FileInputStream(log);
                ObjectInputStream input = new ObjectInputStream(fis);

                Object result;
                try {
                    while (true) {
                        result = input.readObject();
                        snapshots.add((Memento) result);
                    }
                } catch (EOFException e) {
                    //do nothing
                }

                fis.close();
                input.close();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveSnapshot(Memento snap) {
        snapshots.add(snap);
        count_of_added_snapshots++;
    }

    public void removeSnapshot(Memento snap) {
        snapshots.remove(snap);
        count_of_removed_snapshots++;
    }

    public void removeSnapshotByDate(Date date) {
        for(Memento snap: snapshots) {
            if(date.equals(snap.getDate_of_change())) {
                removeSnapshot(snap);
                break;
            }
        }
    }

    public Memento getSnapshot(int id) {
        return snapshots.get(id);
    }

    public ArrayList<Memento> getSnapshots() {
        return snapshots;
    }

    public static void logSnapshots() {
        FileOutputStream fos;
        ObjectOutputStream output;

        try {
            if (count_of_added_snapshots > 0) {

                fos = new FileOutputStream(log, true);
                output = new ObjectOutputStream(fos);

                for (int i = snapshots.size() - count_of_added_snapshots; i < snapshots.size(); i++)
                    output.writeObject(snapshots.get(i));

                output.close();
                fos.close();

            }
            if (count_of_removed_snapshots > 0) {
                //FIND SOME BETTER SOLUTION!
                log.delete();
                log.createNewFile();
                fos = new FileOutputStream(log);
                output = new ObjectOutputStream(fos);

                for(Memento snap: snapshots)
                    output.writeObject(snap);

                output.reset();

                output.close();
                fos.close();
            }
        } catch (FileNotFoundException e) {
                e.printStackTrace();
        } catch (IOException e) {
        e.printStackTrace();
        }
    }
}
