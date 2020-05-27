package hr.fer.zemris.optjava.dz12.nodes.actions;

import hr.fer.zemris.optjava.dz12.World;
import hr.fer.zemris.optjava.dz12.nodes.Node;

public class Move extends ActionNode {

    @Override
    public void executeOrder(World world) {
        if (world.getSteps() > World.MAX_STEPS) return;
        world.move();
    }

    @Override
    public Node copy() {
        return new Move();
    }

    @Override
    public String toString() {
        return "Move";
    }
}
