package hr.fer.zemris.optjava.dz12.nodes;

import hr.fer.zemris.optjava.dz12.World;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

public abstract class Node {

    private Node parent;
    private int currentDepth;
    private boolean chosen = false;

    public Node getParent() {
        return parent;
    }

    public int getCurrentDepth() {
        return currentDepth;
    }

    public void updateCurrentDepth(){
        if (parent == null) {
            currentDepth = 0;
        }else {
            currentDepth = parent.getCurrentDepth() + 1;
        }
    }

    public int maxDepth() {
        int max = 0;
        updateCurrentDepth();
        if (requiredChildren() == 0) return currentDepth;
        else {
            for (Node child : this.getChildren()) {
                int childDepth = child.maxDepth();
                if (childDepth > max){
                    max = childDepth;
                }
            }
        }
        return max;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getSubnodes() {
        List<Node> children = getChildren();
        int subnodes = 1;
        for (Node childNode: children) {
            subnodes += childNode.getSubnodes();
        }
        return subnodes;
    }

    public abstract Node copy();

    public abstract Node getChild(int index);

    public abstract void addChild(Node child);

    public abstract void setChildAtIndex(Node child, int index);

    public abstract void executeOrder(World world);

    public abstract int requiredChildren();

    public abstract List<Node> getChildren();

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }

    public void exportPrint(PrintWriter writer){
        if (this.requiredChildren() == 0) writer.write(this.toString());
        else {
            writer.write(this.toString() + "(");
            Iterator<Node> iterator = this.getChildren().iterator();
            while (true){
                iterator.next().exportPrint(writer);
                if (iterator.hasNext()){
                    writer.write(",");
                } else break;
            }
            writer.write(")");
        }
    }
}
