package alogrithm.EvolutionaryProgramming;

import alogrithm.ANN;
import java.util.Random;

/**
 * Created by Chris on 2017/2/8.
 */
public class Chromosome {

    public ANN ann = null;
    private double fitness;
    private int win;

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
        fitness = 0;
        for (int i = 0; i < ann.InNum; i++) {
            for (int j = 0; j < ann.HideNum; j++)
                if (ann.w[i][j] > 0.5) fitness++;
        }
        for (int i = 0; i < ann.HideNum; i++) {
            if (ann.v[i] > 0.5) fitness++;
        }
    }

}
