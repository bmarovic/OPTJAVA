import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Dataset {

    private double[][] inputs;
    private double[] outputs;

    public Dataset(String filePath, int numberOfInputs, int numberOfSamples) {

        List<Double> data = new ArrayList<>();

        try{
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new BufferedInputStream(
                                    Objects.requireNonNull(ANNTrainer.class.getClassLoader()
                                            .getResourceAsStream(filePath))),
                            StandardCharsets.UTF_8));

            String line = br.readLine();

            while (line != null){
                data.add(Double.parseDouble(line.trim()));
                line = br.readLine();
            }
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }

        if(numberOfSamples < 0){
            numberOfSamples += data.size();
        }

        double minValue = data.stream().min(Double::compareTo).get();
        double maxValue = data.stream().max(Double::compareTo).get();

        for (int i = 0; i < numberOfSamples; i++) {
            data.set(i, -1 + ((data.get(i) - minValue) * 2) / (maxValue - minValue));
        }

        this.inputs = new double[numberOfSamples - numberOfInputs][numberOfInputs];
        this.outputs = new double[numberOfSamples - numberOfInputs];

        for (int i = 0; i < numberOfSamples - numberOfInputs; i++) {
            for (int j = 0; j < numberOfInputs; j++) {
                inputs[i][j] = data.get(i + j);
            }
            outputs[i] = data.get(i + numberOfInputs);
        }

    }

    int numberOfSamples(){
        return inputs.length;
    }

    int numberOfFeatures(){
        return inputs[0].length;
    }

    int numberOfOutputs(){
        return 1;
    }

    double[] getInputAtIndex(int index){
        return inputs[index];
    }

    double getOutputAtIndex(int index){
        return outputs[index];
    }

}
