package graph;

import java.util.*;

/**
 * Created by ben on 7/14/16.
 */
public class Graph {

    public final int GRID_WIDTH;
    public final int GRID_HEIGHT;

    private boolean pathFound;
    private List<Node> path;

    private Node[][] grid;
    private Node startNode;
    private Node endNode;

    public Graph(int GRID_WIDTH, int GRID_HEIGHT) {
        this.GRID_WIDTH = GRID_WIDTH;
        this.GRID_HEIGHT = GRID_HEIGHT;

        this.grid = new Node[GRID_WIDTH][GRID_HEIGHT];

        for(int x = 0; x < GRID_WIDTH; x++) {
            for(int y = 0; y < GRID_HEIGHT; y++) {
                Node newNode = new Node(NodeType.WALL, x, y);
                System.out.println(newNode);
                this.grid[x][y] = newNode;
            }
        }

        this.pathFound = false;
        this.path = new ArrayList<Node>();
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public Node[][] getGrid() {
        return grid;
    }

    public Node getNode(int x, int y) {
        return this.grid[x][y];
    }

    public void calculate() {
        Random rng = new Random();
        List<Node> tree = new ArrayList<Node>();
        List<Node> frontiers = new ArrayList<Node>();

        // Set a random starting point
        int randX = rng.nextInt(GRID_WIDTH);
        int randY = rng.nextInt(GRID_HEIGHT);

        grid[randX][randY].setType(NodeType.PASSAGE);
        this.startNode = grid[randX][randY];
        this.endNode = grid[GRID_WIDTH-1][GRID_HEIGHT-1];

        tree.add(grid[randX][randY]);

        while(!pathFound) {
            ListIterator<Node> iter = tree.listIterator();
            Node currentNode = null;
            Node frontierNode = null;
            Node passageNode = null;

            while(iter.hasNext()) {
                Node n = iter.next();
                currentNode = n;

                // X direction
                // Increment by 4 since the gaps in between aren't considered.
                // This will process the far left and far right nodes in the X direction, eg:
                // OGGGO
                for (int x = n.GRID_X - 2; x <= n.GRID_X + 2; x += 4) {
                    // Don't add passages to frontiers
                    if (x >= 0 && x < GRID_WIDTH && grid[x][n.GRID_Y].getType() != NodeType.PASSAGE) {
                        Node newNode = new Node(NodeType.PASSAGE, x, n.GRID_Y, n);
                        frontiers.add(newNode);
                    }
                }

                // Y Direction
                // Increment by 4 since gaps aren't considered
                // This will process the top and bottom nodes in the y direction ,eg:
                // O
                // G
                // G
                // G
                // O
                for (int y = n.GRID_Y - 2; y <= n.GRID_Y + 2; y += 4) {
                    // Don't add passages again
                    if (y >= 0 && y < GRID_HEIGHT && grid[n.GRID_X][y].getType() != NodeType.PASSAGE) {
                        frontiers.add(new Node(NodeType.PASSAGE, n.GRID_X, y, n));
                    }
                }
            }

            Node nextNode = null;

            if(frontiers.size() == 0) {
                this.pathFound = true;
                break;
            }

            if(frontiers.size() == 1) {
                nextNode = frontiers.get(0);
            } else {
                nextNode = frontiers.get(rng.nextInt(frontiers.size() - 1));
            }

            iter.add(nextNode);
            frontierNode = nextNode;

            // Set as passable
            grid[nextNode.GRID_X][nextNode.GRID_Y].setType(NodeType.PASSAGE);

            int pathNodeXCoord = (nextNode.GRID_X + nextNode.getParent().GRID_X) /2;
            int pathNodeYCoord = (nextNode.GRID_Y + nextNode.getParent().GRID_Y) / 2;

            grid[pathNodeXCoord][pathNodeYCoord].setType(NodeType.PASSAGE);
            passageNode = grid[pathNodeXCoord][pathNodeYCoord];

            // Clear frontiers for next run of loop
            frontiers.clear();

            frontierNode.setParent(passageNode);
            passageNode.setParent(currentNode);

            // Artifically slow down
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Node> findPath(Node start) {
        System.out.println("Node: " + start.getId());
        //System.out.println("Parent: " + start.getParent().getId());
        if(start.getParent() == null) {
            List<Node> list = new ArrayList<Node>();
            list.add(start);
            return list;
        }

        List<Node> list = this.findPath(start.getParent());
        list.add(start);

        return list;
    }

    public List<Node> getPath() {
        return findPath(endNode);
    }

    public boolean isPathFound() {
        return pathFound;
    }
}
