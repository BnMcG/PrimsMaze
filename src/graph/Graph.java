package graph;

import logic.CalculateThread;

import java.util.*;

/**
 * Created by ben on 7/14/16.
 */
public class Graph {

    public final int GRID_WIDTH;
    public final int GRID_HEIGHT;

    private boolean generationComplete;

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

        this.generationComplete = false;
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
        List<Node> tree = new ArrayList<Node>();

        if(this.startNode == null) {
            Random rng = new Random();
            int x = rng.nextInt(GRID_WIDTH-1);
            int y = rng.nextInt(GRID_HEIGHT-1);

            this.startNode = grid[x][y];
            this.startNode.setType(NodeType.PASSAGE);
        }

        tree.add(startNode);

        while(!generationComplete) {

            ListIterator<Node> treeIterator = tree.listIterator();


            List<Node> frontiers = this.findFrontiersInTree(treeIterator);
            Node chosenFrontier = this.chooseFrontier(frontiers);

            // This means that all frontiers have been processed and maze generation is complete
            if(chosenFrontier == null) {
                this.setEndNode();

                this.generationComplete = true;
                break;
            }

            // Convert chosen frontier into passage
            this.convertNodeToPassage(chosenFrontier);

            // Add the converted frontier to the tree of passages, and it will then be scanned for frontiers on next loop
            treeIterator.add(chosenFrontier);

            // Find the node which connects the current node on the tree to the frontier we just converted and
            // added to the tree
            Node connectingNode = this.findConnectingNodeOfFrontier(chosenFrontier);

            // Set the parent of the connecting node to be the tree item the frontier comes from
            connectingNode.setParent(chosenFrontier.getParent());

            // Set the parent of the chosen frontier to be the connecting node
            chosenFrontier.setParent(connectingNode);

            // Convert the connecting node into a passage, too
            this.convertNodeToPassage(connectingNode);

            // Artifically slow down
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Node convertNodeToPassage(Node n) {
        n.setType(NodeType.PASSAGE);
        return n;
    }

    private Node findConnectingNodeOfFrontier(Node frontier) {
        // Work out the node that will act as a passage between the current node and the frontier node we just converted
        int pathNodeXCoord = (frontier.GRID_X + frontier.getParent().GRID_X) /2;
        int pathNodeYCoord = (frontier.GRID_Y + frontier.getParent().GRID_Y) / 2;

        return grid[pathNodeXCoord][pathNodeYCoord];
    }

    public void setEndNode() {
        // Pick a random position in the maze as the end point
        this.endNode = this.chooseRandomPassageNode();
    }

    private Node chooseRandomPassageNode() {
        // Pick a random position in the maze as the end point
        Random rng = new Random();
        Node n;

        do {
            n = grid[rng.nextInt(GRID_WIDTH - 1)][rng.nextInt(GRID_HEIGHT - 1)];
        }
        while(n.getType() == NodeType.WALL);

        return n;
    }

    private List<Node> findPath(Node end) {
        // The starting node won't have a parent since it's where we generated the maze from
        if(end.getParent() == null) {
            List<Node> list = new ArrayList<Node>();
            list.add(end);
            return list;
        }

        // Keep recursing back until we find the starting node.
        List<Node> list = this.findPath(end.getParent());
        list.add(end);

        // Return a list of nodes from the given node to the starting node
        return list;
    }

    private Node chooseFrontier(List<Node> frontiers) {
        // If frontiers size is 0, this means we've run out of frontiers, so maze generation
        // is complete.
        if(frontiers.size() == 0) {
            return null;
        } else {
            if (frontiers.size() == 1) {
                // Pick the only possible frontier to advance to
                return frontiers.get(0);
            } else {
                // Pick a random frontier to advance to
                Random rng = new Random();
                return frontiers.get(rng.nextInt(frontiers.size() - 1));
            }
        }
    }

    private List<Node> findFrontiersInTree(ListIterator<Node> tree) {

        List<Node> frontiers = new ArrayList<Node>();

        while(tree.hasNext()) {
            Node n = tree.next();

            // X direction
            // Increment by 4 since the gaps in between aren't considered.
            // This will process the far left and far right nodes in the X direction, eg:
            // OGGGO
            for (int x = n.GRID_X - 2; x <= n.GRID_X + 2; x += 4) {
                // Check the node type isn't a passage, since passages can't be frontiers (empty spaces make bad walls!)
                // If we didn't do this, then the program would loop infinitely (I think)
                if (x >= 0 && x < GRID_WIDTH && grid[x][n.GRID_Y].getType() != NodeType.PASSAGE) {
                    // set the parent of this particular node to whichever node we're currently analysing,
                    // change its type to passage, add it to list of frontiers
                    // Since it's now a passage, it won't be considered as a frontier again.

                    grid[x][n.GRID_Y].setParent(n);
                    frontiers.add(grid[x][n.GRID_Y]);
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
                // Same as x direction, but flipped for Y
                if (y >= 0 && y < GRID_HEIGHT && grid[n.GRID_X][y].getType() != NodeType.PASSAGE) {
                    grid[n.GRID_X][y].setParent(n);
                    frontiers.add(grid[n.GRID_X][y]);
                }
            }
        }

        return frontiers;
    }

    public List<Node> getPathToEndNode() {
        return findPath(endNode);
    }

    public boolean isGenerationComplete() {
        return generationComplete;
    }
}
