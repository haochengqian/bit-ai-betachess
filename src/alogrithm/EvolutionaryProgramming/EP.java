package alogrithm.EvolutionaryProgramming;

import alogrithm.ANN;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Chris on 2017/2/8.
 */
public class EP {

    final static double alpha = 0.2;
    final static int selectivePressure = 25;

    public static Chromosome Mutation(Chromosome chromosome) {
        Random r = new Random();
        double a = r.nextGaussian();
        double[] newMutationStep = new double[2];
        Chromosome newChromosome = new Chromosome();
        for (int i = 0; i < chromosome.ann.InNum; i++) {
            for (int j = 0; j < chromosome.ann.HideNum; j++) {
                newChromosome.ann.w[i][j] = chromosome.ann.w[i][j] * (1 + (alpha * r.nextGaussian()));
            }
        }
        for (int i = 0; i < chromosome.ann.HideNum; i++) {
            newChromosome.ann.v[i] = chromosome.ann.v[i] * (1 + (alpha * r.nextGaussian()));
        }
        newChromosome.setFitness();
        return newChromosome;
    }

    public static Population SurvivorSelection(Population pop) {
        pop.clearWin();
        for (int i = 0; i < pop.getNumbOfPop(); i++) {
            Chromosome y = pop.getChromosome(i);
            Random r = new Random();
            Set<Integer> generated = new LinkedHashSet<Integer>();
            while (generated.size() < selectivePressure) {
                Integer next = r.nextInt(pop.getNumbOfPop());
                if (next != i) {
                    generated.add(next);
                }
            }
            Iterator<Integer> iterator = generated.iterator();
            for (int j = 0; j < selectivePressure; j++) {
                Chromosome x = pop.getChromosome(iterator.next());
                if (y.getFitness() > x.getFitness()) {
                    y.setWin(y.getWin() + 1);
                }
            }
        }
        pop.sortByWin();
        Population newPopulation = new Population(pop.getNumbOfPop() / 2, false);
        for (int i = 0; i < newPopulation.getNumbOfPop(); i++) {
            newPopulation.setChromosome(i, pop.getChromosome(i));
        }
        return newPopulation;
    }

    public static Population Evolution(Population pop) {
        Population newPopulation = new Population(pop.getNumbOfPop()*2, false);
        for (int i = 0; i < newPopulation.getNumbOfPop(); i++){
            int j = 0;
            Chromosome x;
            if(i < pop.getNumbOfPop()){
                x = pop.getChromosome(i);
                newPopulation.setChromosome(i, x);
            }
            else
            {
                x = Mutation(pop.getChromosome(j));
                newPopulation.setChromosome(i, x);
            }
        }
        newPopulation = SurvivorSelection(newPopulation);
        return newPopulation;
    }
}
