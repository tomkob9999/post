package opt.test;

// import java.util.Arrays;

// import dist.DiscreteDependencyTree;
// import dist.DiscreteUniformDistribution;
// import dist.Distribution;

// import opt.DiscreteChangeOneNeighbor;
import opt.EvaluationFunction;
// import opt.GenericHillClimbingProblem;
// import opt.HillClimbingProblem;
// import opt.NeighborFunction;
// import opt.RandomizedHillClimbing;
// import opt.SimulatedAnnealing;
// import opt.example.*;
// import opt.ga.CrossoverFunction;
// import opt.ga.DiscreteChangeOneMutation;
// import opt.ga.SingleCrossOver;
// import opt.ga.GenericGeneticAlgorithmProblem;
// import opt.ga.GeneticAlgorithmProblem;
// import opt.ga.MutationFunction;
// import opt.ga.StandardGeneticAlgorithm;
// import opt.prob.GenericProbabilisticOptimizationProblem;
// import opt.prob.MIMIC;
// import opt.prob.ProbabilisticOptimizationProblem;
// import shared.FixedIterationTrainer;


import shared.Trainer;
import opt.OptimizationAlgorithm;

/**
 * Copied from ContinuousPeaksTest
 * @version 1.0
 */
public class TestFit {

    // public static void fitAndTime(Trainer trainer, OptimizationAlgorithm algo, EvaluationFunction ef, int maxSecs, int[] overNum, int[] overThr) {
    // public static void fitAndTime(Trainer trainer, OptimizationAlgorithm algo, EvaluationFunction ef, int maxSecs) {
    public static int fitAndTime(Trainer trainer, OptimizationAlgorithm algo, EvaluationFunction ef, int maxSecs, double[] overThr, int limit) {
        if (maxSecs == 0) maxSecs = 120;
        long startTime = System.nanoTime();     
        // int[] overNum = {0, 0, 0, 0};
        // int[] ret = new int[2];
        int[] overNum = new int[overThr.length];

        // System.out.println("overNum[0]=" + overNum[0]);
        // System.out.println("overNum[1]=" + overNum[1]);
        // System.out.println("overNum[2]=" + overNum[2]);
        // System.out.println("overNum[3]=" + overNum[3]);
        // int[] overThr = {50, 100, 150, 198};
        int pass198 = 0;
        double optimal = 0.0;
        // for (int i = 0; i < 50000; i++) {
        for (int i = 0; i < 50000000; i++) {
            trainer.train();
            // long themod = ((System.nanoTime() - startTime)%10000);
            long themod = ((System.nanoTime() - startTime)%1000);
            long millisecs = ((System.nanoTime() - startTime)/1000000);
            long microsecs = ((System.nanoTime() - startTime)/1000);
            if (limit != -1 && millisecs > limit) {
                return 1000000;
            }
            if (themod == 0 ) {
                if (millisecs > maxSecs*1000) {
                    // if (limit == -1) System.out.println("TIME OUT after " + millisecs + " millisecs.");
                    if (limit == -1) System.out.println("TIME OUT after " + microsecs + " millisecs.");
                    break;
                }
                for (int k=0; k<overThr.length; k++) {
                    if (overNum[k] == 0) {
                        optimal = ef.value(algo.getOptimal());
                        if (ef.value(algo.getOptimal()) >= overThr[k]) {
                            // System.out.println("RHC: " + ef.value(rhc.getOptimal()));
                            if (limit == -1) 
                                // System.out.println(" " + overThr[k] + " with value " + optimal + " after " + ((System.nanoTime() - startTime)/1000000) + " millisecs and " + (i+1) + " iterations.");
                                System.out.println(" " + overThr[k] + " with value " + optimal + " after " + ((System.nanoTime() - startTime)/1000) + " microsecs and " + (i+1) + " iterations.");
                            overNum[k] = 1;
                            // if (ef.value(algo.getOptimal()) >= overThr[overThr.length-1]) {
                            //     pass198 = 1;
                            // }
                            if (k == overThr.length-1) {
                                pass198 = 1;
                            }
                        } else {
                            break;
                        }
                    }
                }

            }

            if (pass198 == 1) break;
        }
        // System.out.println("overNum[0]=" + overNum[0]);
        // System.out.println("overNum[1]=" + overNum[1]);
        // System.out.println("overNum[2]=" + overNum[2]);
        // System.out.println("overNum[3]=" + overNum[3]);
        // long ret = ((System.nanoTime() - startTime)/1000000);
        long ret = ((System.nanoTime() - startTime)/1000);

        return (int)((pass198 == 1)? ret: 1000000);

    }
}
