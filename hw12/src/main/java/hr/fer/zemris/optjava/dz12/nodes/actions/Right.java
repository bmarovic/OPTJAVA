package hr.fer.zemris.optjava.dz12.nodes.actions;

import hr.fer.zemris.optjava.dz12.World;
import hr.fer.zemris.optjava.dz12.nodes.Node;

public class Right extends ActionNode {

    @Override
    public void executeOrder(World world) {
        if (world.getSteps() > World.MAX_STEPS) return;
        world.right();
    }

    @Override
    public Node copy() {
        return new Right();
    }

    @Override
    public String toString() {
        return "Right";
    }
}
