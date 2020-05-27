package hr.fer.zemris.optjava.dz12.nodes.actions;

import hr.fer.zemris.optjava.dz12.nodes.Node;

import java.util.ArrayList;
import java.util.List;

public abstract class ActionNode extends Node {

    @Override
    public Node getChild(int index) {
        throw new IllegalCallerException();
    }

    @Override
    public void addChild(Node child) {
        throw new IllegalCallerException();
    }

    @Override
    public void setChildAtIndex(Node child, int index) {
        throw new IllegalCallerException();
    }

    @Override
    public int requiredChildren() {
        return 0;
    }

    @Override
    public List<Node> getChildren() {
        return new ArrayList<>();
    }
}
