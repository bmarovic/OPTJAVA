import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class ANNTrainer {

    public static Dataset dataset;


    //07-iris-formatirano.data clonalg 30 0.02 200
    //07-iris-formatirano.data pso-a 30 0.02 500
    //07-iris-formatirano.data pso-b-15 50 0.02 500
    public static void main(String[] args) {

        String file = args[0];
        String alg = args[1];
        int velPop = Integer.parseInt(args[2]);
        double errorThreshold = Double.parseDouble(args[3]);
        int maxIter = Integer.parseInt(args[4]);

        try {

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new BufferedInputStream(
                                    Objects.requireNonNull(ANNTrainer.class.getClassLoader()
                                            .getResourceAsStream(file))),
                            "UTF-8"));


            String line = br.readLine();
            ArrayList<double[]> inputList = new ArrayList<>();
            ArrayList<double[]> outputList = new ArrayList<>();

            while(line != null){
                line = line.replace("(", "").replace(")", "");
                String[] inputOutput = line.split(":");
                String[] inputString = inputOutput[0].split(",");
                String[] outputString = inputOutput[1].split(",");
                double[] inputDouble = new double[inputString.length];
                double[] outputDouble = new double[outputString.length];
                for (int i = 0; i < inputString.length; i++) {
                    inputDouble[i] = Double.parseDouble(inputString[i]);
                }
                for (int i = 0; i < outputString.length; i++) {
                    outputDouble[i] = Double.parseDouble(outputString[i]);
                }
                inputList.add(inputDouble);
                outputList.add(outputDouble);

                line = br.readLine();
            }
            double[][] inputs = new double[inputList.size()][inputList.get(0).length];
            double[][] outputs = new double[outputList.size()][outputList.get(0).length];

            for (int i = 0; i < inputs.length; i++) {
                System.arraycopy(inputList.get(i), 0, inputs[i], 0, inputs[i].length);
                System.arraycopy(outputList.get(i), 0, outputs[i], 0, outputs[i].length);
            }
            dataset = new Dataset(inputs, outputs);

        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        FFANN ffann = new FFANN(new int[]{4,5,3,3}, new ITransferFunction[]{
                new SigmoidTransferFunction(),
                new SigmoidTransferFunction(),
                new SigmoidTransferFunction()},
                dataset);

        if (alg.equals("pso-a")){
            PSOGlobal psoAlgGlobal = new PSOGlobal(velPop, maxIter, errorThreshold, ffann);
            psoAlgGlobal.run();
        }else if (alg.startsWith("pso-b-")){
            int neighborhoodHalfWidth = Integer.parseInt(alg.replace("pso-b-",""));
            PSOLocal psoAlgLocal = new PSOLocal(velPop, maxIter, errorThreshold, ffann,neighborhoodHalfWidth);
            psoAlgLocal.run();
        }else if (alg.equals("clonalg")){
            ClonAlg clonAlg = new ClonAlg(velPop, maxIter, errorThreshold, ffann);
            clonAlg.run();
        }else {
            System.exit(1);
        }
    }
}
