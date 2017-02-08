package alogrithm.EvolutionaryProgramming;

import alogrithm.ANN;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Chris on 2017/2/8.
 */
public class Population {

    public Chromosome[] population;

    public Population(int populationSize, boolean create) {
        population = new Chromosome[populationSize];
        if (create) {
            for (int i = 0; i < populationSize; i++) {
                this.population[i] = new Chromosome(new ANN());
            }
        }
    }

    public int getNumbOfPop() {
        return population.length;
    }

    public Chromosome getChromosome(int index) {
        return population[index];
    }

    public Chromosome getFittest() {
        Chromosome fittest = population[0];
        for (int i = 1; i < this.population.length; i++) {
            if (population[i].getFitness() > fittest.getFitness()) {
                fittest = this.population[i];
            }
        }
        return fittest;
    }
    
    public void setChromosome(int index, Chromosome k){
        this.population[index] = k;
    }
    
    public void clearWin(){
        for (int i = 0; i< this.population.length;i++){
            population[i].setWin(0);
        }
    }
    public void sortByFitness(){
        Arrays.sort(population, new Comparator<Chromosome>(){
            @Override
            public int compare(Chromosome o1, Chromosome o2) {
                if (o1.getFitness() == o2.getFitness()){
                    return 0;
                }else if(o1.getFitness() < o2.getFitness()){
                    return 1;
                }else{
                    return -1;
                }
            }
            
        });
    }
     public void sortByWin(){
        Arrays.sort(population, new Comparator<Chromosome>(){
            @Override
            public int compare(Chromosome o1, Chromosome o2) {
                if (o1.getWin() == o2.getWin()){
                    return 0;
                }else if(o1.getWin() < o2.getWin()){
                    return 1;
                }else{
                    return -1;
                }
            }
            
        });
    }

}
