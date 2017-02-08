package alogrithm.EvolutionaryProgramming;

/**
 * Created by Chris on 2017/2/8.
 */
public class EvolutionaryProgramming {
    public static void main(String[] args) {
//        // TODO code application logic here
        int numOfPop = 0;
        int numOfGen = 0;
        for (int k = 0; k < 4; k++) {
            System.out.println("==========================================");
            System.out.println("==========================================");
            System.out.println("");
            if (k == 3) {
                numOfGen = 50;
                numOfPop = 400;
            } else if (k == 2) {
                numOfGen = 100;
                numOfPop = 200;
            } else if (k == 1) {
                numOfGen = 200;
                numOfPop = 100;
            } else if (k == 0) {
                numOfGen = 400;
                numOfPop = 50;
            } else {
                numOfGen = 0;
                numOfPop = 0;
            }

            System.out.println("numOfGen = " + numOfGen);
            System.out.println("numOfPop = " + numOfPop);
            for (int j = 0; j < 5; j++) {
                System.out.println("percobaan ke " + (j + 1));
                Population pop = new Population(numOfPop, true);
                System.out.println("Nilai awal");
                System.out.println("X1 = " + pop.getFittest().getVariable(0));
                System.out.println("X2 = " + pop.getFittest().getVariable(1));
                double a = pop.getFittest().getFitness();
                for (int i = 0; i < numOfGen; i++) {
                    pop = EP.Evolution(pop);
                }

                System.out.println("Nilai Akhir");
                System.out.println("X1 = " + pop.getFittest().getVariable(0));
                System.out.println("X2 = " + pop.getFittest().getVariable(1));
                System.out.println("Fitness Awal = " + a);
                System.out.println("Fitness Akhir= " + pop.getFittest().getFitness());
                System.out.println("");
                System.out.println("");
            }
        }
        Population a = new Population(5, true);
        for (int i = 0 ; i<a.getNumbOfPop();i++){
            
            a.getChromosome(i).setWin(i);
            System.out.println(a.getChromosome(i).getWin());
        }
        System.out.println("");
        a.sortByWin();
         for (int i = 0 ; i<a.getNumbOfPop();i++){
             System.out.println(a.getChromosome(i).getWin());
        }
    }
}
