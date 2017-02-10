package alogrithm.EvolutionaryProgramming;

import alogrithm.ANN;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
            this.pop = EP.Evolution(this.pop);
        }
        saveToFile();
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
//    public static void main(String[] args) {
//        int numOfPop = 50;
//        int numOfGen = 10000;
//        Population pop = new Population(numOfPop, true);
//
//        System.out.println(pop.getFittest().getFitness());
//
//        for (int i = 0; i < numOfGen; i++) {
//            pop = EP.Evolution(pop);
//        }
//
//        System.out.println(pop.getFittest().getFitness());
//
//        String str = pop.getFittest().ann.getWeights();
//        try {
//            File annOut = new File("annOut.txt");
//            if(!annOut.exists()){
//                annOut.createNewFile();
//            }
//            FileWriter fileWritter = new FileWriter(annOut.getName());
//            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
//            bufferWritter.write(str);
//            bufferWritter.close();
//
//            System.out.println("Done");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//    }
}
