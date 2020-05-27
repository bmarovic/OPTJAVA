package hr.fer.zemris.optjava.dz12.nodes.subroutine;

import hr.fer.zemris.optjava.dz12.nodes.Node;

import java.util.ArrayList;
import java.util.List;

public abstract class SubroutineNode extends Node {

    private List<Node> children;

    public List<Node> getChildren() {
        return children;
    }

    public Node getChild(int index) {
        return children.get(index);
    }

    public void addChild(Node child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
        child.setParent(this);
        child.updateCurrentDepth();
    }

    public void setChildAtIndex(Node child, int index) {
        child.setParent(this);
        children.set(index, child);
    }
}
