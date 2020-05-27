package hr.fer.zemris.optjava.dz12.nodes;

import hr.fer.zemris.optjava.dz12.nodes.subroutine.IfFoodAhead;
import hr.fer.zemris.optjava.dz12.nodes.subroutine.Prog2;
import hr.fer.zemris.optjava.dz12.nodes.subroutine.Prog3;
import hr.fer.zemris.optjava.dz12.nodes.actions.ActionNode;
import hr.fer.zemris.optjava.dz12.nodes.actions.Left;
import hr.fer.zemris.optjava.dz12.nodes.actions.Move;
import hr.fer.zemris.optjava.dz12.nodes.actions.Right;
import hr.fer.zemris.optjava.dz12.nodes.subroutine.SubroutineNode;

import java.util.Random;

public class NodeManagement {

    private static Random rand = new Random();

    public static Node generateTree(int maxDepth, int availableNodes, boolean full){
        Node root = getRandomSubroutineNode();

        for (int i = 0; i < root.requiredChildren(); ++i){
            Node child = generateSubtree(maxDepth - 1, availableNodes - 1, full);
            root.addChild(child);
        }

        return root;
    }

    public static Node generateSubtree(int maxDepth, int availableNodes, boolean full) {
        Node root;
        if (maxDepth == 1 || availableNodes < 1) {
            root = getRandomActionNode();
        } else if (full) {
            root = getRandomSubroutineNode();
        } else root = getRandomNode();

        availableNodes--;
        for (int i = 0; i < root.requiredChildren(); i++) {
            Node child = generateSubtree(maxDepth - 1, availableNodes, full);
            availableNodes -= child.getSubnodes();
            root.addChild(child);
        }
        return root;
    }

    public static Node getRandomNode(){
        if (rand.nextDouble() > 0.5) {
            return getRandomActionNode();
        } else {
            return getRandomSubroutineNode();
        }
    }

    public static SubroutineNode getRandomSubroutineNode(){
        switch (rand.nextInt(3)){
            case 0:
                return new IfFoodAhead();
            case 1:
                return new Prog2();
            default:
                return new Prog3();
        }
    }

    public static ActionNode getRandomActionNode(){
        switch (rand.nextInt(3)){
            case 0:
                return new Left();
            case 1:
                return new Right();
            default:
                return new Move();
        }
    }

    public static Node getRandomNode(Node root){
        int rootSubnodes = root.getSubnodes();
        return getRandomNode(root, rand.nextInt(rootSubnodes));
    }

    public static Node getRandomNode(Node root, int randNodeNumber){
        if (randNodeNumber == 0) return root;

        randNodeNumber--;

        for (int i = 0; i < root.requiredChildren(); ++i){
            Node child = root.getChild(i);
            if (child.getSubnodes() > randNodeNumber) return getRandomNode(child, randNodeNumber);
            else randNodeNumber -= child.getSubnodes();
        }
        return root;
    }
}
