package hr.fer.zemris.optjava.dz12.nodes.subroutine;

import hr.fer.zemris.optjava.dz12.World;
import hr.fer.zemris.optjava.dz12.nodes.Node;

import java.util.List;

public class Prog3 extends SubroutineNode {

    @Override
    public int requiredChildren() {
        return 3;
    }

    @Override
    public void executeOrder(World world) {
        List<Node> children = getChildren();
        for (Node child: children) {
            child.executeOrder(world);
        }
    }

    @Override
    public Node copy() {
        SubroutineNode newNode = new Prog3();
        for (int i = 0; i < 3; i++) {
            newNode.addChild(this.getChild(i).copy());
        }
        return newNode;
    }

    @Override
    public String toString() {
        return "Prog3";
    }
}
