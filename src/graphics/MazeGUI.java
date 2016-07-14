package graphics;

import graph.Graph;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ben on 7/14/16.
 */
public class MazeGUI {

    public final int DISPLAY_WIDTH;
    public final int DISPLAY_HEIGHT;

    private JFrame frame;
    private MazeCanvas canvas;
    private Graph maze;


    public MazeGUI(int displayWidth, int displayHeight, Graph m) {
        this.DISPLAY_HEIGHT = displayHeight;
        this.DISPLAY_WIDTH = displayWidth;
        this.maze = m;

        this.init();
    }

    private void init() {
        this.frame = new JFrame("Prim's Maze");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(false);

        this.canvas = new MazeCanvas(this.maze, this.DISPLAY_WIDTH, this.DISPLAY_HEIGHT);
        this.canvas.setVisible(true);

        this.frame.add(canvas);

        this.frame.pack();
        this.frame.setVisible(true);
    }

    public Graphics getCanvasBrush() {
        return this.canvas.getGraphics();
    }

}
