package part2;

import java.util.ArrayList;

public class Container{

    private ArrayList<Stick> sticks;
    private int emptySpace;
    private static int maxHeight;

    public Container(){
        this.sticks = new ArrayList<>();
        this.emptySpace = Container.getMaxHeight();
    }


    public boolean addStick(Stick stick){
        if (stick.getLength() <= emptySpace){
            sticks.add(stick);
            emptySpace -= stick.getLength();
            return true;
        }
        return false;
    }

    public static void setMaxHeight(int maxHeight) {
        Container.maxHeight = maxHeight;
    }

    public static int getMaxHeight() {
        return maxHeight;
    }

    public int getHeight(){
        return maxHeight - this.emptySpace;
    }

    public ArrayList<Stick> getSticks() {
        return sticks;
    }

    public int getEmptySpace() {
        return emptySpace;
    }

    @Override
    public String toString() {
        return "Container{" +
                "sticks=" + sticks +
                ", emptySpace=" + emptySpace +
                '}';
    }

    public Container duplicate(){
        ArrayList<Stick> sticks = new ArrayList<>();
        for (Stick stick : this.sticks) {
            sticks.add(new Stick(stick.getLength(), stick.getId()));
        }
        Container clone = new Container();
        for (Stick stick : sticks) {
            clone.addStick(stick);
        }
        return clone;
    }
}
