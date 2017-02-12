package algorithm.EvolutionaryProgramming;

import algorithm.ANN;
import control.RobotTrain;

/**
 * Created by Chris on 2017/2/8.
 */
public class Chromosome {

    public ANN ann = null;  // ANN for training
    private double fitness; // Fitness of ANN
    private int win;        // Win times in SurvivorSelection
    public Chromosome()
    {
        this.ann = new ANN();
        this.win = 0;
    }

    public Chromosome(ANN ann)
    {
        this.ann = ann;
        setFitness();
        this.win = 0;
    }


    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness() {
        RobotTrain robotTrain = new RobotTrain(this.ann);
        this.fitness = robotTrain.simulate();
        System.out.println("The fitness is " + this.fitness);
    }

}
