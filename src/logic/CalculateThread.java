package logic;

import graph.Graph;

/**
 * Created by ben on 7/14/16.
 */
public class CalculateThread extends Thread {

    private Graph maze;

    public CalculateThread(Graph maze) {
        this.maze = maze;
    }

    @Override
    public void run() {
        this.maze.calculate();
    }

}
