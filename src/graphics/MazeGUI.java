package graphics;

import graph.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by ben on 7/14/16.
 */
public class MazeGUI {

    public final int DISPLAY_WIDTH;
    public final int DISPLAY_HEIGHT;

    private JFrame frame;
    private MazeCanvas canvas;
    private Graph maze;

    private Timer nodePositionTimer;


    public MazeGUI(int displayWidth, int displayHeight, Graph m) {
        this.DISPLAY_HEIGHT = displayHeight;
        this.DISPLAY_WIDTH = displayWidth;
        this.maze = m;

        this.init();
    }

    private void init() {
        this.frame = new JFrame("Prim's Maze");
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.frame.setResizable(false);

        this.canvas = new MazeCanvas(this.maze, this.DISPLAY_WIDTH, this.DISPLAY_HEIGHT);

        canvas.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.frame.add(canvas);

        this.setupNodePositionTimer();
        this.setupKeyboardListener(this.frame);

        JLabel label = new JLabel("P: toggle path, T: toggle automatic node changes, G: toggle graph, K: toggle key nodes, E: choose new end node, R: display random nodes");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Sans Serif", Font.PLAIN, 8));

        this.canvas.add(label);

        this.frame.pack();
        this.canvas.setVisible(true);
        this.frame.setVisible(true);
    }

    private void setupNodePositionTimer() {
        nodePositionTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maze.setEndNode();
            }
        });
    }

    private void setupKeyboardListener(JFrame frame) {
        // Listen to the keyboard and if we detect certain keys do certain things
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                switch(e.getKeyCode()) {
                    // if P pressed, toggle rendering of path
                    case KeyEvent.VK_P:
                        canvas.setDoRenderPath(!canvas.isDoRenderPath());
                        break;
                    // if K pressed, toggle rendering of key nodes
                    case KeyEvent.VK_K:
                        canvas.setDoRenderKeyNodes(!canvas.isDoRenderKeyNodes());
                        break;
                    // if R pressed, toggle rendering of random nodes (+ their parent)
                    case KeyEvent.VK_R:
                        canvas.setDoRenderRandomNodes(!canvas.isDoRenderRandomNodes());
                        break;
                    // If G pressed, toggle rendering of graph
                    case KeyEvent.VK_G:
                        canvas.setDoRenderGraph(!canvas.isDoRenderGraph());
                        break;
                    // If E pressed, choose a new end point
                    case KeyEvent.VK_E:
                        maze.setEndNode();
                        break;
                    // If T pressed, toggle end node timer
                    case KeyEvent.VK_T: {
                        if (nodePositionTimer.isRunning()) {
                            nodePositionTimer.stop();
                        } else {
                            nodePositionTimer.start();
                        }
                        break;
                    }
                }

            }
        });
    }

    public Graphics getCanvasBrush() {
        return this.canvas.getGraphics();
    }

}
