package snapshot;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by evarmic on 02-Oct-18.
 */
public class CareTaker extends Thread {
    private ArrayList<Memento> snapshots;
    private FileOutputStream fos;
    private FileInputStream fis;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    File log;

    public CareTaker() {
        snapshots = new ArrayList<Memento>();
        try {
            log = new File("log.ser");

            if (log.exists() && log.length() != 0) {
                fis = new FileInputStream(log);
                input = new ObjectInputStream(fis);

                Object result;
                while (!(result=input.readObject()).equals(-1)) {
                    snapshots.add((Memento)result);
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

    public void run() {
        if(snapshots.size()!=0) {
            try {
                fos = new FileOutputStream(log, true);
                output = new ObjectOutputStream(fos);

                for (Memento element : snapshots)
                    output.writeObject(element);

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
