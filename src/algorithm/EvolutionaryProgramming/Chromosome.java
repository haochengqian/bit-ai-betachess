package algorithm.EvolutionaryProgramming;

import algorithm.ANN;
import control.RobotTrain;

/**
 * Created by Chris on 2017/2/8.
 */
public class Chromosome {

    public ANN ann = null;
    private double fitness;
    private int win;
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

    // TODO: ...
    public double getFitness() {
        return fitness;
    }

    public void setFitness() {

        RobotTrain robotTrain = new RobotTrain(this.ann);
        this.fitness = robotTrain.simulate();
        System.out.println("The fitness is " + this.fitness);
//        fitness = 0;
//        for (int i = 0; i < ann.InNum; i++) {
//            for (int j = 0; j < ann.HideNum; j++)
//                if (ann.w[i][j] > 0.5) fitness++;
//        }
//        for (int i = 0; i < ann.HideNum; i++) {
//            if (ann.v[i] > 0.5) fitness++;
//        }
    }

}
