package editor;

import fileManips.TextWriter;
import snapshot.CareTaker;
import fileManips.TextReader;
import snapshot.Memento;
import snapshotManips.SnapshotHandler;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Date;

/**
 * Created by evarmic on 02-Oct-18.
 */
public class EditorInterface extends JFrame {
    private File currentFile;
    private CareTaker careTaker;
    private JPanel textPanel;
    private JTextArea textArea;
    private DefaultListModel listModel;
    private JList snapsList;
    private JFileChooser fileChooser;
    private JButton saveButton;

    public EditorInterface() {
        super("Editor with snapshots");

        careTaker = new CareTaker();

        JPanel snapsPanel = new JPanel();
        textPanel = new JPanel();
        JPanel buttonPanel1 = new JPanel();
        JPanel buttonPanel2 = new JPanel();
        JButton SwitchToButton = new JButton("Switch to");
        JButton removeButton = new JButton("Remove snapshot");
        listModel = new DefaultListModel();
        snapsList = new JList(listModel);
        textArea  = new JTextArea();
        saveButton = new JButton("Save snapshot");
        JButton newFileButton = new JButton("New File");
        JButton openFileButton = new JButton("Open file");

        configureFileChooser();

        //layouts
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.DARK_GRAY));
        snapsPanel.setLayout(new BorderLayout());
        textPanel.setLayout(new BorderLayout());

        //SNAPSHOTS PANEL CONFIG
        snapsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        snapsPanel.add(new JScrollPane(snapsList), BorderLayout.CENTER);
        buttonPanel1.setLayout(new FlowLayout());
        buttonPanel1.add(SwitchToButton);
        buttonPanel1.add(removeButton);
        snapsPanel.add(buttonPanel1, BorderLayout.SOUTH);

        //Switch To other snapshot listener
        SwitchToButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to switch to this snapshot?", "Switch to snapshot?", JOptionPane.YES_NO_OPTION);
                if(reply == JOptionPane.YES_OPTION) {
                    Date value = (Date) snapsList.getSelectedValue();

                    if(value==null) {
                        JOptionPane.showMessageDialog(null, "Please select the snapshot first", "No Snapshot selected", JOptionPane.ERROR_MESSAGE);
                    } else {
                        String text = SnapshotHandler.switchTo(value, careTaker, currentFile);
                        textArea.setText(text);
                    }
                }
            }
        });

        //remove snapshot listener
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date value = (Date) snapsList.getSelectedValue();
                if(value == null) {
                    JOptionPane.showMessageDialog(null, "Please select the snapshot first", "No Snapshot selected", JOptionPane.ERROR_MESSAGE);
                } else {
                    listModel.remove(snapsList.getSelectedIndex());
                    careTaker.removeSnapshotByDate(value);
                }
            }
        });

        //TEXT PANEL CONFIG

        //buttons config
        buttonPanel2.setLayout(new FlowLayout());
        buttonPanel2.add(saveButton);
        buttonPanel2.add(newFileButton);
        buttonPanel2.add(openFileButton);

        //file opening listener
        openFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showOpenDialog(textPanel);

                if(result==JFileChooser.APPROVE_OPTION) {

                        try {
                            currentFile = fileChooser.getSelectedFile();

                            if(currentFile.exists() && currentFile.canRead()) {
                                String text = TextReader.getText(currentFile);
                                textArea.setFont(new Font(TextReader.getEncoding(currentFile), Font.PLAIN, 15));
                                //read
                                textArea.setText(text);

                                //bring list of snapshots for current file
                                configureSnapshotList();
                            } else if(!currentFile.exists()) {
                                JOptionPane.showMessageDialog(null, "File " + currentFile.getAbsolutePath() + " does not exist", "File not found", JOptionPane.ERROR_MESSAGE);
                            } else if(!currentFile.canRead()) {
                                JOptionPane.showMessageDialog(null, "File can't be read", "Error opening file", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (IOException e1) {
                            JOptionPane.showMessageDialog(null, e1.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);
                        }
                }

            }
        });

        //New file listener
        newFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentFile=null;
                textArea.setText("");
                //clean snapshot list
                listModel.clear();
            }
        });

        //Save file listener
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentFile == null) {
                    int result = fileChooser.showSaveDialog(textPanel);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        currentFile = fileChooser.getSelectedFile();
                        //write
                        try {
                            TextWriter.writeTo(currentFile, textArea.getText());
                        } catch (IOException e1) {
                            JOptionPane.showMessageDialog(null, e1.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);
                        }
                    } else
                        return; //inner if end
                } else {
                    int reply = JOptionPane.showConfirmDialog(null, "Do You wish to save current version of file?", "Save file?", JOptionPane.YES_NO_OPTION);
                    if(reply == JOptionPane.YES_OPTION) {
                        //write
                        try {
                            TextWriter.writeTo(currentFile, textArea.getText());
                        } catch (IOException e1) {
                            JOptionPane.showMessageDialog(null, e1.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);
                        }
                        Memento snapshot = new Memento(currentFile.getAbsolutePath(), textArea.getText());
                        careTaker.saveSnapshot(snapshot);
                        addSnapshotToList(snapshot);
                    } else
                        return; // inner if end
                } // else end
                //create snapshot
                Memento snapshot = new Memento(currentFile.getAbsolutePath(), textArea.getText());
                careTaker.saveSnapshot(snapshot);
                addSnapshotToList(snapshot);
            }
            });

        textArea.setFont(new Font("UTF8", Font.PLAIN, 15));
        textPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        textPanel.add(buttonPanel2, BorderLayout.SOUTH);

        //adding panels
        add(snapsPanel, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);

        //add CTRL+S shortcut listener
        SaveListener saveListener = new SaveListener();
        textArea.addKeyListener(saveListener);
        snapsList.addKeyListener(saveListener);
    }

    //snapshots list config
    private void configureSnapshotList() {
        //if(currentFile!=null) {
            listModel.clear();
            for (Memento snap: careTaker.getSnapshots()) {
                if(snap.getFile_name().equals(currentFile.getAbsolutePath()))
                    addSnapshotToList(snap);
            }
        //}
    }

    private void addSnapshotToList(Memento snap) {
        listModel.addElement(snap.getDate_of_change());
    }

    //file chooser config
    private void configureFileChooser() {
        fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("txt", "txt"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("java", "java"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("docx", "docx"));
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")+"\\Desktop"));
        fileChooser.setLocation(20, 20);
    }

    //CTRL+S configuration
    private class SaveListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
                saveButton.doClick();
            }
        }
    }

}