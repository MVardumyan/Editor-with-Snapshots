package editor;

import snapshot.CareTaker;

import javax.swing.*;

public class App 
{
    public static void main( String[] args )
    {
        EditorInterface editor = new EditorInterface();
        editor.setVisible(true);
        editor.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //editor.setUndecorated(true);
        editor.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Runtime.getRuntime().addShutdownHook(new CareTaker());
    }
}
