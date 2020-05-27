package hr.fer.zemris.optjava.dz12.nodes.subroutine;

import hr.fer.zemris.optjava.dz12.Direction;
import hr.fer.zemris.optjava.dz12.World;
import hr.fer.zemris.optjava.dz12.nodes.Node;

public class IfFoodAhead extends SubroutineNode {

    @Override
    public int requiredChildren() {
        return 2;
    }

    @Override
    public void executeOrder(World world) {
        if (foodAhead(world)) {
            getChild(0).executeOrder(world);
        } else getChild(1).executeOrder(world);
    }

    public boolean foodAhead(World world) {
        int xcoord = world.getXcoord();
        int ycoord = world.getYcoord();
        Direction direction = world.getDirection();

        switch (direction) {
            case DOWN:
                ycoord++;
                if (ycoord >= world.getHeight()) ycoord = 0;
                break;
            case RIGHT:
                xcoord++;
                if (xcoord >= world.getWidth()) xcoord = 0;
                break;
            case UP:
                ycoord--;
                if (ycoord < 0) ycoord = world.getHeight() - 1;
                break;
            case LEFT:
                xcoord--;
                if (xcoord < 0) xcoord = world.getWidth() - 1;
                break;
        }
        return world.getAtCoordinate(ycoord, xcoord);
    }

    @Override
    public Node copy() {
        SubroutineNode newNode = new IfFoodAhead();
        for (int i = 0; i < 2; i++) {
            newNode.addChild(this.getChild(i).copy());
        }
        return newNode;
    }

    @Override
    public String toString() {
        return "IfFoodAhead";
    }
}
