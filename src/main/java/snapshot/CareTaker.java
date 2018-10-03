package snapshot;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by evarmic on 02-Oct-18.
 */
public class CareTaker {
    private static ArrayList<Memento> snapshots;
    private FileOutputStream fos;
    private FileInputStream fis;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private static File log;
    private static int count_of_added_snapshots=0;

    public CareTaker() {
        snapshots = new ArrayList<Memento>();
        try {
            log = new File("log.ser");

            if (log.exists()) {
                fis = new FileInputStream(log);
                input = new ObjectInputStream(fis);

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
        int index = snapshots.indexOf(snap);
        snapshots.remove(index);
    }

    public Memento getSnapshot(int id) {
        return snapshots.get(id);
    }

    public ArrayList<Memento> getSnapshots() {
        return snapshots;
    }

    public static void logSnapshots() {

        if(count_of_added_snapshots>0) {
            try {
                FileOutputStream fos = new FileOutputStream(log, true);
                ObjectOutputStream output = new ObjectOutputStream(fos);

                for (int i=snapshots.size()-count_of_added_snapshots; i<snapshots.size(); i++)
                    output.writeObject(snapshots.get(i));

                fos.close();
                output.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
