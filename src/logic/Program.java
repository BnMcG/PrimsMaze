package logic;

import graph.Graph;
import graphics.MazeGUI;

import javax.swing.*;

/**
 * Created by ben on 7/14/16.
 */
public class Program {

    public static void main(String[] args) {
        final Graph maze = new Graph(75, 75);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MazeGUI(675, 675, maze);
            }
        });

        new CalculateThread(maze).start();
    }

}
