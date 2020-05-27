import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TriSATSolver {

    private static Random rand = new Random();

    public static void main(String[] args){

        int numberOfVariables = -1;
        int numberOfClauses;
        SATFormula formula;
        Clause[] clauses = null;
        int maxIteration = 100000;
        int multiStartLimit = 100;
        int algorithm = 0;

        try {
            algorithm = Integer.parseInt(args[0]);
            String fileName = args[1];


            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new BufferedInputStream(TriSATSolver.class.getResourceAsStream(fileName)), StandardCharsets.UTF_8));

            String line = br.readLine();
            while (line.charAt(0) == 'c'){
                line = br.readLine();
            }
            String[] definitionLine = line.replace("  ", " ").split(" ");

            numberOfVariables = Integer.parseInt(definitionLine[2]);
            numberOfClauses = Integer.parseInt(definitionLine[3]);

            clauses = new Clause[numberOfClauses];

            for (int i = 0; i < numberOfClauses; i++){

                line = br.readLine().trim();
                String[] clauseVariables = line.substring(0, line.length() - 2).split(" ");
                int[] intClauseVariables = new int[clauseVariables.length];

                for (int j = 0; j < clauseVariables.length; j++){
                    intClauseVariables[j] = Integer.parseInt(clauseVariables[j]);
                }
                clauses[i] = new Clause(intClauseVariables);
            }
            br.close();

        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        formula = new SATFormula(numberOfVariables, clauses);

        if (algorithm == 1){
            algorithm_1(numberOfVariables, formula);
            return;
        }

        BitVector vector = new BitVector(rand, numberOfVariables);
        MutableBitVector inputVector = vector.copy();
        BitVector solution;

        switch (algorithm) {
            case 2: solution = algorithm_2(formula, maxIteration, inputVector);
                      System.out.println(solution);
                      break;
            case 3: solution = algorithm_3(numberOfVariables, formula);
                      System.out.println(solution);
                      break;
            case 4: solution = algorithm_4(formula, multiStartLimit, maxIteration, inputVector);
                      System.out.println(solution);
                      break;
            case 5: solution = algorithm_5(numberOfVariables, formula, maxIteration, multiStartLimit);
                      System.out.println(solution);
                      break;
            case 6: solution = algorithm_6(numberOfVariables, formula, maxIteration, inputVector);
                      System.out.println(solution);
                      break;
            default:
                System.out.println("Algorithm does not exist");
        }
    }

    private static void algorithm_1(int numberOfVariables, SATFormula formula){

        MutableBitVector vector = new MutableBitVector(numberOfVariables);
        for (int i = 1; i < Math.pow(2,numberOfVariables); i++) {
            if (formula.isSatisfied(vector)){
                System.out.println(vector);
            }
            String binary = Integer.toBinaryString(i);
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < numberOfVariables - binary.length(); j++) {
                sb.append("0");
            }
            binary = sb.toString() + binary;
            for (int j = 0; j < binary.length(); j++) {
                if (binary.charAt(j) == '1') vector.set(j + 1, true);
                else vector.set(j + 1, false);
            }
        }
    }

    private static BitVector algorithm_2(SATFormula formula, int maxIteration, MutableBitVector inputVector){

        BitVectorNGenerator nGenerator = new BitVectorNGenerator(inputVector);
        int maxFitness = fit(inputVector,formula);
        ArrayList<MutableBitVector> maxNeighbors = new ArrayList<>();


        if (maxFitness == formula.getNumberOfClauses()) return inputVector;


        for (int i = 0; i < maxIteration ; i++) {
            MutableBitVector[] neighborhood = nGenerator.createNeighborhood();
            boolean localOptima = true;
            for (MutableBitVector mutVec : neighborhood) {
                int fitness = fit(mutVec, formula);

                if (fitness == formula.getNumberOfClauses()) return mutVec;

                if (fitness > maxFitness){
                    maxNeighbors.clear();
                    maxNeighbors.add(mutVec);
                    maxFitness = fitness;
                    localOptima = false;
                }
                else if (fitness == maxFitness) {
                    maxNeighbors.add(mutVec);
                    localOptima = false;
                }
            }
            if (localOptima){
                throw new RuntimeException();
            }
            inputVector = maxNeighbors.get(rand.nextInt(maxNeighbors.size()));
            nGenerator = new BitVectorNGenerator(inputVector);
        }

        return null;
    }

    private static BitVector algorithm_3(int numberOfVariables, SATFormula formula){

        BitVector vector = new BitVector(rand, numberOfVariables);
        MutableBitVector startingVector = vector.copy();
        BitVectorNGenerator nGenerator = new BitVectorNGenerator(startingVector);
        int maxIteration = 100000;
        SATFormulaStats formulaStats = new SATFormulaStats(formula);

        for (int i = 0; i < maxIteration; i++) {
            formulaStats.setAssignment(startingVector, true);

            if(formulaStats.isSatisfied()) return startingVector;

            MutableBitVector[] neighborhood = nGenerator.createNeighborhood();
            MutableBitVector[] bestOf = new MutableBitVector[SATFormulaStats.numberOfBest];
            double[] bestOfValues = new double[SATFormulaStats.numberOfBest];

            for (MutableBitVector v : neighborhood) {

                formulaStats.setAssignment(v,false);
                double fitness = formulaStats.getNumberOfSatisfied() + formulaStats.getPercentageBonus();

                for (int j = 0; j < SATFormulaStats.numberOfBest; j++) {
                    if (fitness > bestOfValues[j]){
                        bestOfValues[j] = fitness;
                        bestOf[j] = v;
                    }
                }

            }

            startingVector = bestOf[rand.nextInt(SATFormulaStats.numberOfBest)];
            nGenerator = new BitVectorNGenerator(startingVector);

        }

        return null;
    }

    private static BitVector algorithm_4(SATFormula formula, int maxIteration, int multiStartLimit,
                                         MutableBitVector inputVector){

        for (int i = 0; i < multiStartLimit; i++) {
            try {
                BitVector vector = algorithm_2(formula, maxIteration, inputVector);
                if (vector != null) return vector;
            }catch (RuntimeException ignored){}
        }

        return null;


    }

    private static BitVector algorithm_5(int numberOfVariables, SATFormula formula,
                                         int maxIteration, int multiStartLimit){

        double probability = 0.5;

        for (int i = 0; i < multiStartLimit; i++) {

            BitVector vector = new BitVector(rand, numberOfVariables);
            MutableBitVector startingVector = vector.copy();

            for (int j = 0; j < maxIteration; j++) {

                if (fit(startingVector,formula) == formula.getNumberOfClauses()) return startingVector;

                ArrayList<Clause> clauses = new ArrayList<>();

                for (int k = 0; k < formula.getNumberOfClauses(); k++) {
                    if (!formula.getClause(k).isSatisfied(startingVector)){
                        clauses.add(formula.getClause(k));
                    }
                }

                Clause randClause = clauses.get(rand.nextInt(clauses.size()));
                double probabilityResult = rand.nextDouble();

                if (probabilityResult > probability){
                    int randVar = randClause.getLiteral(rand.nextInt(randClause.getSize()));
                    randVar = Math.abs(randVar);
                    assert startingVector != null;
                    startingVector.set(randVar, !startingVector.get(randVar));
                }
                else{
                    int maxFitness = 0;
                    MutableBitVector fittestVector = null;
                    for (int k = 0; k < randClause.getSize(); k++) {

                        int testLiteral = Math.abs(randClause.getLiteral(k));
                        assert startingVector != null;
                        MutableBitVector testVector = startingVector.copy();

                        testVector.set(testLiteral, !testVector.get(testLiteral));
                        if (maxFitness < fit(testVector, formula)){
                            maxFitness = fit(testVector, formula);
                            fittestVector = testVector;
                        }
                    }
                    startingVector = fittestVector;
                }

            }

        }
        return null;
    }

    private static BitVector algorithm_6(int numberOfVariables, SATFormula formula,
                                         int maxIteration, MutableBitVector inputVector){

        double percentage = 0.3;
        int numberOfVariablesToFlip = (int)(percentage * numberOfVariables);

        for (int i = 0; i < maxIteration; i++) {
            try {
                BitVector vector = algorithm_2(formula, maxIteration, inputVector);
                if (vector != null) return vector;
            }catch (RuntimeException e){
                ArrayList<Integer> variables = new ArrayList<>();
                for (int j = 1; j <= numberOfVariables; j++) {
                    variables.add(j);
                }
                Collections.shuffle(variables);
                for (int j = 0; j < numberOfVariablesToFlip; j++) {
                    int variableToFlip = variables.get(j);
                    inputVector.set(variableToFlip, inputVector.get(variableToFlip));
                }
            }
        }
        return null;
    }

    private static int fit(BitVector bitVector, SATFormula formula){

        int fitness = 0;

        for (int i = 0; i < formula.getNumberOfClauses(); i++) {
            if(formula.getClause(i).isSatisfied(bitVector)) fitness++;
        }
        return fitness;
    }
}
