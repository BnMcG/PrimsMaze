package graphics;

import graph.Graph;
import graph.Node;
import graph.NodeType;

import javax.swing.*;
import java.awt.*;

import java.util.List;

/**
 * Created by ben on 7/14/16.
 */
public class MazeCanvas extends JPanel {

    private Graph maze;

    public final int NODE_WIDTH;
    public final int NODE_HEIGHT;

    public MazeCanvas(Graph maze, int canvasWidth, int canvasHeight) {
        super();
        this.setPreferredSize(new Dimension(canvasWidth, canvasHeight));
        this.maze = maze;

        NODE_WIDTH = canvasWidth/maze.GRID_WIDTH;
        NODE_HEIGHT = canvasHeight/maze.GRID_HEIGHT;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.renderNodes(g);
        this.renderKeyNodes(g);
        this.renderPath(g);

        repaint();
        revalidate();

    }

    public void renderPath(Graphics brush) {
        List<Node> path = this.maze.getPath();
        System.out.println(path);

        for(Node p : path) {
            brush.setColor(Color.ORANGE);
            brush.fillRect(p.GRID_X * NODE_WIDTH, p.GRID_Y * NODE_HEIGHT, NODE_WIDTH, NODE_HEIGHT);
        }
    }

    private void renderKeyNodes(Graphics brush) {
        Node startNode = maze.getStartNode();
        Node endNode = maze.getEndNode();

        if(startNode != null) {
            brush.setColor(Color.GREEN);
            brush.fillRect(startNode.GRID_X * NODE_WIDTH, startNode.GRID_Y * NODE_HEIGHT, NODE_WIDTH, NODE_HEIGHT);
        }

        if(endNode != null) {
            brush.setColor(Color.PINK);
            brush.fillRect(endNode.GRID_X * NODE_WIDTH, endNode.GRID_Y * NODE_HEIGHT, NODE_WIDTH, NODE_HEIGHT);
        }
    }

    private void renderNodes(Graphics brush) {
        Node[][] grid = this.maze.getGrid();

        for(int gridX = 0; gridX < this.maze.GRID_WIDTH; gridX++) {
            for(int gridY = 0; gridY < this.maze.GRID_HEIGHT; gridY++) {
                int pixelX = gridX * NODE_WIDTH;
                int pixelY = gridY * NODE_HEIGHT;

                brush.setColor(grid[gridX][gridY].getType() == NodeType.PASSAGE ? Color.GRAY : Color.DARK_GRAY);
                brush.fillRect(pixelX, pixelY, NODE_WIDTH, NODE_HEIGHT);
            }
        }
    }
}
