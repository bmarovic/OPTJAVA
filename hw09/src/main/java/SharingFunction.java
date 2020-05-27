public class SharingFunction {

    private static final double sigmaShare = 0.1;
    private static final double alpha = 2;


    public double valueOfFitness(double[] solution1, double[] solution2,
                                 double[] maxValues, double[] minValues) {
        double distance = 0;
        for (int i = 0; i < solution1.length; i++) {
            if (maxValues == minValues) continue;
            distance += Math.pow((solution1[i] - solution2[i]) / (maxValues[i] - minValues[i]), 2);
        }
        distance = Math.sqrt(distance);
        if (distance < sigmaShare) return 1.0 - Math.pow(distance / sigmaShare, alpha);
        return 0;
    }

}
