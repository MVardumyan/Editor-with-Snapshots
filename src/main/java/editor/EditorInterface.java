package editor;

import snapshot.CareTaker;
import snapshot.Memento;
import snapshot.TextReader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.soap.Text;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * Created by evarmic on 02-Oct-18.
 */
public class EditorInterface extends JFrame {
    private File currentFile;
    private JTextArea textArea;
    private JList snapsList;
    private CareTaker careTaker;
    private JFileChooser fileChooser;

    public EditorInterface() {
        super("Editor with snapshots");

        JPanel snapsPanel = new JPanel();
        JPanel textPanel = new JPanel();
        JPanel buttonPanel1 = new JPanel();
        JPanel buttonPanel2 = new JPanel();
        JButton rollbackButton = new JButton("Rollback");
        JButton removeButton = new JButton("Remove snapshot");
        textArea  = new JTextArea();
        JButton saveButton = new JButton("Save");
        JButton openFileButton = new JButton("Open file");

        configureSnapshotList();
        configureFileChooser();

        //layouts
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.DARK_GRAY));
        snapsPanel.setLayout(new BorderLayout());
        textPanel.setLayout(new BorderLayout());

        //snapshots panel config
        snapsPanel.add(new JScrollPane(snapsList), BorderLayout.CENTER);
        buttonPanel1.setLayout(new FlowLayout());
        buttonPanel1.add(rollbackButton);
        buttonPanel1.add(removeButton);
        snapsPanel.add(buttonPanel1, BorderLayout.SOUTH);

        //text panel config

        //buttons config
        buttonPanel2.setLayout(new FlowLayout());
        buttonPanel2.add(saveButton);
        buttonPanel2.add(openFileButton);

        //file opening listener
        openFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showOpenDialog((Component) e.getSource());

                if(result==JFileChooser.APPROVE_OPTION) {
                    FileReader fr;
                    BufferedReader br;

                        try {
                            currentFile = fileChooser.getSelectedFile();

                            if(currentFile.exists() && currentFile.canRead()) {
                                String text = TextReader.getText(currentFile);
                                textArea.setFont(new Font("Unicode", Font.PLAIN, 15));
                                textArea.setText(text);
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

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });

        textPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        textPanel.add(buttonPanel2, BorderLayout.SOUTH);

        //adding panels
        add(snapsPanel, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);

    }

    //snapshots list config
    private void configureSnapshotList() {
        snapsList = new JList();
        snapsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        careTaker = new CareTaker();
        String[] snapshotTitles = new String[careTaker.getSnapshots().size()];
        for(int i=0; i<snapshotTitles.length; i++) {
            snapshotTitles[i] = i+". " + careTaker.getSnapshot(i).getDate_of_change();
        }
        snapsList.setListData(snapshotTitles);
    }

    //file chooser config
    private void configureFileChooser() {
        fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("txt", ".txt"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("java", ".java"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("docx", ".docx"));
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")+"\\Desktop"));
    }
}