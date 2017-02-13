package algorithm.EvolutionaryProgramming;

import algorithm.ANN;

import java.io.*;

/**
 * Created by Chris on 2017/2/8.
 */
public class EvolutionaryProgramming {

    int numOfPop = 0;
    int numOfGen = 0;
    Population pop = null;
    ANN bestANN = null;

    public EvolutionaryProgramming(int numOfPop, int numOfGen) throws InterruptedException {
        this.numOfPop = numOfPop;
        this.numOfGen = numOfGen;
        this.pop = new Population(numOfPop, true);
        this.bestANN = null;
    }

    public void evolove() throws InterruptedException {
        for (int i = 0; i < numOfGen; i++) {
            System.out.println("第" + (i+1) + "代");
            System.out.println(this.pop.getFittest().ann.getWeights());
            saveToFile();
            saveAllToFile();
            this.pop = EP.Evolution(this.pop);
        }
        saveToFile();
        saveAllToFile();
    }

    public void saveToFile() {
        String str = this.pop.getFittest().ann.getWeights();

        try {
            File annOut = new File("annOut.txt");
            if(!annOut.exists()){
                annOut.createNewFile();
            }
            FileWriter fileWritter = new FileWriter(annOut.getName());
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write(str);
            bufferWritter.close();

            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveAllToFile() {

        for (int i = 0; i < this.pop.getNumbOfPop(); i++) {
            Chromosome chromosome = this.pop.getChromosome(i);

            String fileName = "annOut" + i + ".txt";
            String str = chromosome.ann.getWeights();

            try {
                File annOut = new File(fileName);
                if(!annOut.exists()){
                    annOut.createNewFile();
                }
                FileWriter fileWritter = new FileWriter(annOut.getName());
                BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                bufferWritter.write(str);
                bufferWritter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static ANN readFromFile(String ss){
        String resultw = null;
        String resultv = null;
        try {
            FileReader fileReader = null;
            BufferedReader bufferedReader = null;
            String fileName = ss;
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
            try {
                resultw = bufferedReader.readLine();
                resultv = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return ANN.fromWeights(resultw, resultv);
    }
}
