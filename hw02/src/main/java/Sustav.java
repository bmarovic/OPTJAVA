import Jama.Matrix;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Sustav {

    private static Matrix xValues;
    private static Matrix yValues;

    public static void main(String[] args) {

        IHFunction function;
        xValues = null;
        yValues = null;
        String method = null;
        int maxIterations = 0;
        String filePath;

        try {
            method = args[0];
            maxIterations = Integer.parseInt(args[1]);
            filePath = args[2];

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new BufferedInputStream(Sustav.class.getResourceAsStream(filePath)),
                                                                            "UTF-8"));

            String line = br.readLine();
            while(line.startsWith("#")){
                line = br.readLine();
            }

            xValues = new Matrix(10,10);
            yValues = new Matrix(10,1);

            function = new FunctionSystem(null, null);

            for (int i = 0; i < function.numberOfVariables(); i++) {

                String[] values = line.replace("[","")
                        .replace("]","")
                        .replace(",","")
                        .replace("   "," ")
                        .replace("  ", " ")
                        .trim()
                        .split(" ");
                int j = 0;
                while (j < function.numberOfVariables()){
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

        function = new FunctionSystem(xValues, yValues);
        Matrix theta;

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

    private static double costFunction(Matrix theta){
        Matrix costVector = xValues.times(theta).minus(yValues);
        double cost = 0;

        for (int i = 0; i < costVector.getRowDimension(); i++) {
            double square = Math.pow(costVector.get(i, 0), 2);
            cost += square;
            costVector.set(i,0, square);
        }
        costVector.times(0.5);
        return cost;
    }
}
