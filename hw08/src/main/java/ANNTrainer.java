import java.util.Arrays;

public class ANNTrainer {
    //file net n merr maxiter
    public static void main(String[] args) {

        if (args.length != 5){
            System.out.println("Wrong number of parameters, must be 5");
            System.exit(1);
        }

        String filePath = args[0];
        String networkDetails = args[1];
        int velPop = Integer.parseInt(args[2]);
        double errorThreshold = Double.parseDouble(args[3]);
        int maxIter = Integer.parseInt(args[4]);

        if (networkDetails.startsWith("tdnn-")){

            String[] architecture = networkDetails.substring("tdnn-".length()).split("x");
            int[] layers = new int[architecture.length];
            for (int i = 0; i < layers.length; i++) {
                layers[i] = Integer.parseInt(architecture[i]);
            }
            ITransferFunction[] transferFunctions = new ITransferFunction[layers.length - 1];
            Arrays.fill(transferFunctions, new ScaledSigmoidTransferFunction());
            Dataset dataset = new Dataset(filePath, layers[0], 600);
            NeuralNetwork TDNN = new FFANN(layers, transferFunctions, dataset);
            DifferentialEvolution differentialEvolution = new DifferentialEvolution(maxIter, velPop,
                    errorThreshold, TDNN);
            differentialEvolution.run();

        }else if(networkDetails.startsWith("elman-")){

            String[] architecture = networkDetails.substring("elman-".length()).split("x");
            int[] layers = new int[architecture.length];
            for (int i = 0; i < layers.length; i++) {
                layers[i] = Integer.parseInt(architecture[i]);
            }

            ITransferFunction[] transferFunctions = new ITransferFunction[layers.length - 1];
            Arrays.fill(transferFunctions, new ScaledSigmoidTransferFunction());
            Dataset dataset = new Dataset(filePath, layers[0], 600);
            NeuralNetwork elmanNN = new ElmanNN(layers, transferFunctions, dataset);
            DifferentialEvolution differentialEvolution = new DifferentialEvolution(maxIter, velPop,
                    errorThreshold, elmanNN);
            differentialEvolution.run();
        }

    }

}
