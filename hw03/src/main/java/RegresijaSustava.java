import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Random;

public class RegresijaSustava{

    private final static int NUMBER_OF_VARIABLES = 6;
    private final static double DELTA = 0.01;
    private final static double RANGE_MIN = -8;
    private final static double RANGE_MAX = 8;

    private final static double MIN_BITS = 5;
    private final static double MAX_BITS = 30;


    public final static int T_INITIAL = 10000;
    public final static double ALPHA = 0.95;
    public final static int INNER_LIMIT = 4000;
    public final static int OUTER_LIMIT = 1000;
    public final static boolean MINIMIZE = true;


    public static void main(String[] args) {

        double[][] xValues = new double[20][5];
        double[] yValues = new double[20];
        String method = null;
        String filePath;
        try{
            filePath = args[0];
            method = args[1];
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new BufferedInputStream(RegresijaSustava.class.getResourceAsStream(filePath)), "UTF-8"));

            String line = br.readLine();
            while (line.startsWith("#")) {
                line = br.readLine();
            }

            for (int i = 0; i < 20; i++) {

                String[] values = line.replace("[","")
                        .replace("]","")
                        .replace(",","")
                        .replace("   "," ")
                        .replace("  ", " ")
                        .trim()
                        .split(" ");
                int j = 0;
                while (j < 5){
                    xValues[i][j] = Double.parseDouble(values[j]);
                    j++;
                }
                yValues[i] = Double.parseDouble(values[j]);
                line = br.readLine();
            }

        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
        FunctionTransfer function = new FunctionTransfer(xValues, yValues);
        double[] deltas = new double[NUMBER_OF_VARIABLES];
        double[] mins = new double[NUMBER_OF_VARIABLES];
        double[] maxs = new double[NUMBER_OF_VARIABLES];
        Arrays.fill(deltas, DELTA);
        Arrays.fill(mins, RANGE_MIN);
        Arrays.fill(maxs, RANGE_MAX);
        ITempSchedule schedule = new GeometricTempSchedule(ALPHA, T_INITIAL, INNER_LIMIT, OUTER_LIMIT);

        if (method.equals("decimal")){

            PassThroughDecoder decoder = new PassThroughDecoder();
            DoubleArrayNormNeighborhood neighborhood = new DoubleArrayNormNeighborhood(deltas);
            DoubleArraySolution startWith = new DoubleArraySolution(NUMBER_OF_VARIABLES);
            startWith.randomize(new Random(), mins, maxs);


            IOptAlgorithm simulatedAnnealing = new SimulatedAnnealing<>(decoder, neighborhood, startWith, function, MINIMIZE, schedule);

            simulatedAnnealing.run();
            System.exit(0);
        }
        if (method.startsWith("binary:")){

            method = method.substring("binary:".length());
            int bits = Integer.parseInt(method);
            if (bits < MIN_BITS || bits > MAX_BITS){
                System.exit(1);
            }

            IDecoder<BitVectorSolution> decoder = new GrayBinaryDecoder(RANGE_MIN, RANGE_MAX, bits, NUMBER_OF_VARIABLES);
            INeighborhood<BitVectorSolution> neighborhood = new BitVectorNeighborhood();
            BitVectorSolution startWith = new BitVectorSolution(NUMBER_OF_VARIABLES * bits);
            startWith.randomize(new Random());

            IOptAlgorithm algorithm = new SimulatedAnnealing(decoder, neighborhood, startWith, function, MINIMIZE, schedule);

            algorithm.run();
        }else {
            System.out.println("Algoritam ne postoji");
            System.exit(1);
        }

    }
}
