package hr.fer.zemris.optjava.dz12;

import hr.fer.zemris.optjava.dz12.nodes.actions.ActionNode;
import hr.fer.zemris.optjava.dz12.nodes.actions.Left;
import hr.fer.zemris.optjava.dz12.nodes.actions.Move;
import hr.fer.zemris.optjava.dz12.nodes.actions.Right;

import java.util.ArrayList;
import java.util.List;

public class World {

    public static final int MAX_STEPS = 600;
    private boolean[][] worldMap;
    private int ycoord;
    private int xcoord;
    private int foodEaten;
    private Direction direction;
    private int steps;
    private List<ActionNode> stepsList = null;
    private boolean visualization = false;
    private int width;
    private int height;

    public World(boolean[][] worldMap) {
        this.worldMap = worldMap;
        this.ycoord = 0;
        this.xcoord = 0;
        this.foodEaten = 0;
        this.direction = Direction.RIGHT;
        this.height = worldMap.length;
        this.width = worldMap[0].length;
        this.steps = 0;
    }

    public World(boolean[][] worldMap, int ycoord, int xcoord, int foodEaten, Direction direction, int steps) {
        this.worldMap = worldMap;
        this.ycoord = ycoord;
        this.xcoord = xcoord;
        this.foodEaten = foodEaten;
        this.direction = direction;
        this.height = worldMap.length;
        this.width = worldMap[0].length;
        this.steps = steps;
    }

    public int getFoodEaten() {
        return foodEaten;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return worldMap[0].length;
    }

    public boolean getAtCoordinate(int y, int x) {
        return worldMap[y][x];
    }

    public void setAtCoordinate(int y, int x, boolean value) {
        worldMap[y][x] = value;
    }

    public int getYcoord() {
        return ycoord;
    }

    public int getXcoord() {
        return xcoord;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getSteps() {
        return steps;
    }

    public void setVisualization(boolean visualization) {
        this.visualization = visualization;
        this.stepsList = new ArrayList<>();
    }

    public void incrementSteps() {
        this.steps++;
    }

    public List<ActionNode> getStepsList() {
        return stepsList;
    }

    public void move(){
        switch (direction){
            case DOWN:
                ycoord++;
                if (ycoord >= height) ycoord = 0;
                break;
            case RIGHT:
                xcoord++;
                if (xcoord >= width) xcoord = 0;
                break;
            case UP:
                ycoord--;
                if (ycoord < 0) ycoord = height - 1;
                break;
            case LEFT:
                xcoord--;
                if (xcoord < 0) xcoord = width - 1;
                break;
        }

        if (getAtCoordinate(ycoord, xcoord)) {
            foodEaten++;
            setAtCoordinate(ycoord, xcoord, false);
        }
        incrementSteps();
        if (visualization) stepsList.add(new Move());
    }

    public void left(){
        switch (getDirection()) {
            case RIGHT:
                setDirection(Direction.UP);
                break;
            case DOWN:
                setDirection(Direction.RIGHT);
                break;
            case LEFT:
                setDirection(Direction.DOWN);
                break;
            case UP:
                setDirection(Direction.LEFT);
                break;
        }
        incrementSteps();
        if (visualization) stepsList.add(new Left());
    }

    public void right(){
        switch (getDirection()) {
            case RIGHT:
                setDirection(Direction.DOWN);
                break;
            case DOWN:
                setDirection(Direction.LEFT);
                break;
            case LEFT:
                setDirection(Direction.UP);
                break;
            case UP:
                setDirection(Direction.RIGHT);
                break;
        }
        incrementSteps();
        if (visualization) stepsList.add(new Right());
    }

    public World duplicate(){
        int ycoord = this.ycoord;
        int xcoord = this.xcoord;
        int foodEaten = this.foodEaten;
        Direction direction = this.direction;
        int steps = this.steps;
        boolean[][] mapCopy = new boolean[getHeight()][getWidth()];
        for (int i = 0; i < worldMap.length; i++) {
            System.arraycopy(worldMap[i], 0, mapCopy[i], 0, getWidth());
        }
        return new World(mapCopy, ycoord, xcoord, foodEaten, direction, steps);
    }
}
