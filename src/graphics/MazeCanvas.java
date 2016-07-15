package graphics;

import graph.Graph;
import graph.Node;
import graph.NodeType;

import javax.swing.*;
import java.awt.*;

import java.util.List;
import java.util.Random;

/**
 * Created by ben on 7/14/16.
 */
public class MazeCanvas extends JPanel {

    private Graph maze;

    public final int NODE_WIDTH;
    public final int NODE_HEIGHT;

    private boolean doRenderKeyNodes;
    private boolean doRenderPath;
    private boolean doRenderRandomNodes;
    private boolean doRenderGraph;

    public MazeCanvas(Graph maze, int canvasWidth, int canvasHeight) {
        super();
        this.setPreferredSize(new Dimension(canvasWidth, canvasHeight));
        this.maze = maze;

        NODE_WIDTH = canvasWidth/maze.GRID_WIDTH;
        NODE_HEIGHT = canvasHeight/maze.GRID_HEIGHT;

        this.doRenderKeyNodes = true;
        this.doRenderRandomNodes = false;
        this.doRenderPath = false;
        this.doRenderGraph = true;
    }

    public boolean isDoRenderKeyNodes() {
        return doRenderKeyNodes;
    }

    public void setDoRenderKeyNodes(boolean doRenderKeyNodes) {
        this.doRenderKeyNodes = doRenderKeyNodes;
    }

    public boolean isDoRenderPath() {
        return doRenderPath;
    }

    public void setDoRenderPath(boolean doRenderPath) {
        this.doRenderPath = doRenderPath;
    }

    public boolean isDoRenderRandomNodes() {
        return doRenderRandomNodes;
    }

    public void setDoRenderRandomNodes(boolean doRenderRandomNodes) {
        this.doRenderRandomNodes = doRenderRandomNodes;
    }

    public boolean isDoRenderGraph() {
        return doRenderGraph;
    }

    public void setDoRenderGraph(boolean doRenderGraph) {
        this.doRenderGraph = doRenderGraph;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(this.doRenderGraph) {
            this.renderNodes(g);
        }

        if(this.doRenderRandomNodes) {
            this.renderRandomNode(g);
        }

        if(maze.getEndNode() != null && this.doRenderPath) {
            this.renderPath(g);
        }

        if(this.doRenderKeyNodes) {
            this.renderKeyNodes(g);
        }

        // Once the maze has finished generating, only repaint every 0.5 seconds to reduce resource load
        if(maze.isGenerationComplete()) {
            try {
                revalidate();
                repaint();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            revalidate();
            repaint();
        }

    }

    private void renderRandomNode(Graphics brush) {
        if(maze.isGenerationComplete()) {
            Node[][] path = this.maze.getGrid();
            Random rng = new Random();

            int x = rng.nextInt(this.maze.GRID_WIDTH - 1);
            int y = rng.nextInt(this.maze.GRID_HEIGHT - 1);

            Node one = path[x][y];
            while(one.getType() == NodeType.WALL) {
                x = rng.nextInt(this.maze.GRID_WIDTH - 1);
                y = rng.nextInt(this.maze.GRID_HEIGHT - 1);

                one = path[x][y];
            }

            brush.setColor(Color.BLUE);
            brush.fillRect(one.GRID_X * NODE_WIDTH, one.GRID_Y * NODE_HEIGHT, NODE_WIDTH, NODE_HEIGHT);

            Node two = one.getParent();

            if(two != null) {
                brush.setColor(Color.CYAN);
                brush.fillRect(two.GRID_X * NODE_WIDTH, two.GRID_Y * NODE_HEIGHT, NODE_WIDTH, NODE_HEIGHT);
            }
        }
    }

    private void renderKeyNodes(Graphics brush) {
        Node startNode = maze.getStartNode();
        Node endNode = maze.getEndNode();

        if(startNode != null) {
            brush.setColor(new Color(153,255,58));
            brush.fillRect(startNode.GRID_X * NODE_WIDTH, startNode.GRID_Y * NODE_HEIGHT, NODE_WIDTH, NODE_HEIGHT);
        }

        if(endNode != null) {
            brush.setColor(new Color(255, 58, 124));
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

    private void renderPath(Graphics brush) {
        List<Node> path = this.maze.getPathToEndNode();

        for(Node p: path) {
            brush.setColor(new Color(255, 108, 58));
            brush.fillRect(p.GRID_X * NODE_WIDTH, p.GRID_Y * NODE_HEIGHT, NODE_WIDTH, NODE_HEIGHT);
        }
    }
}
