package graph;

import java.util.UUID;

/**
 * Created by ben on 7/14/16.
 */
public class Node {
    private NodeType type;

    private Node parent;

    public final int GRID_X;
    public final int GRID_Y;

    private UUID id;

    public Node(NodeType type, int gridX, int gridY, Node parent) {

        this.type = type;
        this.GRID_X = gridX;
        this.GRID_Y = gridY;
        this.parent = parent;
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public Node(NodeType type, int gridX, int gridY) {
        this.parent = null;
        this.GRID_X = gridX;
        this.GRID_Y = gridY;
        this.type = type;
        this.id = UUID.randomUUID();
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Node[" + this.GRID_X + "," + this.GRID_Y + "] = " + this.getId();
    }
}
