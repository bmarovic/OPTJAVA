import Jama.Matrix;

public class Jednostavno {


    public static void main(String[] args) {

        LineChart chart;
        int maxIteration = 0;
        Matrix startingVector = null;

        try {
            String task = args[0];
            maxIteration = Integer.parseInt(args[1]);
            IFunction function;
            IHFunction hFunction;
            if (args.length == 4) {
                double x1 = Double.parseDouble(args[2]);
                double x2 = Double.parseDouble(args[3]);
                double[][] vectorArray = {{x1}, {x2}};
                startingVector = new Matrix(vectorArray);
            }

            switch (task){
                case "1a":
                    function = new Function1();
                    NumOptAlgorithms.gradientDescend(function, maxIteration, startingVector);
                    chart = new LineChart(NumOptAlgorithms.getDataset(), "1a.png");
                    break;
                case "1b":
                    hFunction = new Function1();
                    NumOptAlgorithms.newtonMethod(hFunction, maxIteration, startingVector);
                    chart = new LineChart(NumOptAlgorithms.getDataset(), "1b.png");
                    break;
                case "2a":
                    function = new Function2();
                    NumOptAlgorithms.gradientDescend(function, maxIteration, startingVector);
                    chart = new LineChart(NumOptAlgorithms.getDataset(), "2a.png");
                    break;
                case "2b":
                    hFunction = new Function2();
                    NumOptAlgorithms.newtonMethod(hFunction, maxIteration, startingVector);
                    chart = new LineChart(NumOptAlgorithms.getDataset(), "2b.png");
                    break;
            }

        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

    }


}
