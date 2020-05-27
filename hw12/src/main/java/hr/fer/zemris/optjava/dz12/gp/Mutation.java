package hr.fer.zemris.optjava.dz12.gp;

import hr.fer.zemris.optjava.dz12.AntTrailGA;
import hr.fer.zemris.optjava.dz12.nodes.Node;
import hr.fer.zemris.optjava.dz12.nodes.NodeManagement;

public class Mutation {

    public static Chromosome mutate(Chromosome child) {
        Chromosome childCopy = child.copy();
        Node root = childCopy.getRoot();

        Node randomNode = NodeManagement.getRandomNode(root);
        randomNode.setChosen(true);

        if (randomNode.getParent() == null){
            childCopy.setRoot(NodeManagement.generateTree(AntTrailGA.MAX_DEPTH, AntTrailGA.MAX_NODES, false));
            return childCopy;
        }

        Node parent = randomNode.getParent();
        for (int i = 0; i < parent.requiredChildren(); ++i){
            if (parent.getChild(i).isChosen()){
                parent.setChildAtIndex(NodeManagement.generateSubtree(
                        AntTrailGA.MAX_DEPTH - randomNode.getCurrentDepth(),
                        AntTrailGA.MAX_NODES - root.getSubnodes(),
                        false), i);
                break;
            }
        }

        childCopy.setRoot(root);
        if (childCopy.getDepth() > AntTrailGA.MAX_DEPTH || childCopy.getRoot().getSubnodes() > AntTrailGA.MAX_NODES) {
            return child;
        }
        return childCopy;
    }
}
