package hr.fer.zemris.optjava.dz12.gp;

import hr.fer.zemris.optjava.dz12.AntTrailGA;
import hr.fer.zemris.optjava.dz12.nodes.Node;
import hr.fer.zemris.optjava.dz12.nodes.NodeManagement;

import java.util.ArrayList;
import java.util.List;

public class Crossover {

    public static List<Chromosome> crossover(Chromosome parent1, Chromosome parent2) {
        Chromosome p1Copy = parent1.copy();
        Chromosome p2Copy = parent2.copy();

        Node p1RandomNode = NodeManagement.getRandomNode(p1Copy.getRoot());
        Node p2RandomNode = NodeManagement.getRandomNode(p2Copy.getRoot());

        p1RandomNode.setChosen(true);
        p2RandomNode.setChosen(true);

        Node p1ParentNode = p1RandomNode.getParent();
        Node p2ParentNode = p2RandomNode.getParent();

        replaceSubtrees(p1Copy, p1RandomNode, p2RandomNode, p1ParentNode);
        replaceSubtrees(p1Copy, p2RandomNode, p1RandomNode, p2ParentNode);

        List<Chromosome> children = new ArrayList<>();
        if (p1Copy.getDepth() > AntTrailGA.MAX_DEPTH || p1Copy.getRoot().getSubnodes() > AntTrailGA.MAX_NODES) {
            children.add(null);
        } else children.add(p1Copy);

        if (p2Copy.getDepth() > AntTrailGA.MAX_DEPTH || p2Copy.getRoot().getSubnodes() > AntTrailGA.MAX_NODES) {
            children.add(null);
        } else children.add(p2Copy);

        return children;
    }

    private static void replaceSubtrees(Chromosome p1Copy, Node p1RandomNode, Node p2RandomNode, Node p1ParentNode) {
        if (p1ParentNode == null){
            p1Copy.setRoot(p2RandomNode.copy());
        } else {
            for (int i = 0; i < p1ParentNode.requiredChildren(); i++) {
                if (p1ParentNode.getChild(i).isChosen()){
                    p1RandomNode.setChosen(false);
                    p1ParentNode.setChildAtIndex(p2RandomNode, i);
                    p2RandomNode.setParent(p1ParentNode);
                    break;
                }
            }
        }
    }


}
