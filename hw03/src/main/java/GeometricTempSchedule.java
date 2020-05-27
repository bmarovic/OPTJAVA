public class GeometricTempSchedule implements ITempSchedule {

    private double alpha;
    private double tInitial;
    private double tCurrent;
    private int innerLimit;
    private int outerLimit;

    public GeometricTempSchedule(double alpha, double tInitial, int innerLimit, int outerLimit) {
        this.alpha = alpha;
        this.tInitial = tInitial;
        this.innerLimit = innerLimit;
        this.outerLimit = outerLimit;
        this.tCurrent = tInitial / alpha;
    }

    @Override
    public double getNextTemperature() {
        tCurrent = tCurrent * alpha;
        return tCurrent;
    }

    @Override
    public int getInnerLoopCounter() {
        return innerLimit;
    }

    @Override
    public int getOuterLoopCounter() {
        return outerLimit;
    }
}
