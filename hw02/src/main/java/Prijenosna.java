import Jama.Matrix;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Prijenosna {

    static Matrix xValues;
    static Matrix yValues;
    static IHFunction function;


    public static void main(String[] args) {

        String method = null;
        int maxIterations = 0;
        String filePath;

        try {
            method = args[0];
            maxIterations = Integer.parseInt(args[1]);
            filePath = args[2];

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new BufferedInputStream(Sustav.class.getResourceAsStream(filePath)), "UTF-8"));

            String line = br.readLine();
            while (line.startsWith("#")) {
                line = br.readLine();
            }

            xValues = new Matrix(20,5);
            yValues = new Matrix(20,1);

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
                    xValues.set(i, j, Double.parseDouble(values[j]));
                    j++;
                }
                yValues.set(i, 0, Double.parseDouble(values[j]));
                line = br.readLine();
            }

        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }

        function = new FunctionTransfer(xValues, yValues);
        Matrix theta = null;

        switch (method){
            case "grad":
                theta = NumOptAlgorithms.gradientDescend(function, maxIterations, null);
                System.out.println("Value of cost function: " + costFunction(theta));
                break;
            case "newton":
                theta = NumOptAlgorithms.newtonMethod(function, maxIterations, null);
                System.out.println("Value of cost function: " + costFunction(theta));
                break;
        }
    }

    private static double costFunction(Matrix theta) {

        double cost = 0;

        for (int i = 0; i < xValues.getRowDimension(); i++) {
            cost += Math.pow(function.valueOfFunction(theta, i) - yValues.get(i, 0), 2);
        }

        return cost;
    }
}
