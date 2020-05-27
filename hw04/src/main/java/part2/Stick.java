package part2;

import java.util.Objects;

public class Stick {

    private int length;
    private int id;

    public Stick(int length, int id) {
        this.length = length;
        this.id = id;
    }

    public int getLength() {
        return length;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stick stick = (Stick) o;
        return id == stick.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "id: " + id + " len: " + length;
    }
}
