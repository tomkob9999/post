package opt.test;

import java.util.Arrays;

import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;

import opt.DiscreteChangeOneNeighbor;
import opt.EvaluationFunction;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.*;
import opt.ga.CrossoverFunction;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.SingleCrossOver;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;


import java.util.Vector;


import shared.Instance;
// import shared.FixedIterationTrainer;


// import shared.Trainer;
// import opt.OptimizationAlgorithm;

/**
 * Copied from ContinuousPeaksTest
 * @version 1.0
 */
public class TemplateTest {
    // /** The n value */
    // private static final int N = 200;
    // /** The t value */
    // private static final int T = N / 5;

    public static void main(String[] args) {
        // int[] ranges = new int[N];
        // Arrays.fill(ranges, 2);
        // FourPeaksEvaluationFunction ef = new FourPeaksEvaluationFunction(T);

        // final int[] overThr = {50, 100, 150, 198};

        // testRHC(ef, ranges, overThr);

        // {
        //     // double[] t = {1E5, 1E5, 1E5};
        //     // double[] cooling = {0.25, 0.5, 0.75};
        //     // testSA(ef, ranges, overThr, 60, 1, t, cooling);
        //     double[] tBEST = {1E5};
        //     double[] coolingBEST = {0.5};
        //     testSA(ef, ranges, overThr, 60, 0, tBEST, coolingBEST);
        // }
        // {
        //     // int[] populationSize = {200, 200, 200};
        //     // int[] toMate = {50, 100, 150};
        //     // int[] toMutate = new int[populationSize.length];
        //     // for (int i=0; i<populationSize.length; i++) {
        //     //     toMutate[i] = (populationSize[i]-toMate[i])*3/4;
        //     // }
        //     // testGA(ef, ranges, overThr, 60, 1, populationSize, toMate, toMutate);
        //     int[] populationSizeBEST = {200};
        //     int[] toMateBEST = {100};
        //     int[] toMutateBEST = {75};
        //     testGA(ef, ranges, overThr, 60, 0, populationSizeBEST, toMateBEST, toMutateBEST);
        // }

        // {
        //     // int[] populationSize = {50,  100, 150, 200};
        //     // int[] toMate = {25, 50, 75, 100};
        //     // testMIMIC(ef, ranges, overThr, 60, 1, populationSize, toMate);
        //     int[] populationSizeBEST = {50};
        //     int[] toMateBEST = {25};
        //     testMIMIC(ef, ranges, overThr, 60, 0, populationSizeBEST, toMateBEST);
        // }

    }

    // public static void testRHC(Distribution odd, NeighborFunction nf, EvaluationFunction ef, int[] ranges, int[] overThr) {
    // public static void testRHC(Distribution odd, NeighborFunction nf, EvaluationFunction ef, int[] overThr) {
    public static void testRHC(HillClimbingProblem hcp, EvaluationFunction ef, double[] overThr) {

        // Distribution odd = new DiscreteUniformDistribution(ranges);
        // NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
        // HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);

        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
        TestFit.fitAndTime(rhc, rhc, ef, 120, overThr, -1);

        Instance bestData = rhc.getOptimal();
        System.out.println("RHC data=");
        for (int i=0; i<bestData.getData().size(); i++) {
        // for (int i=0; i<ranges.length; i++) {
            System.out.print("" + bestData.getData().get(i) + " ");
        }
        System.out.println("");
        System.out.println("RHC: " + ef.value(rhc.getOptimal()));
    }

    // public static void testSA(Distribution odd, NeighborFunction nf, EvaluationFunction ef, int[] ranges, int[] overThr, int maxSecs, int findBest, double[] t, double[] cooling) {
    // public static void testSA(Distribution odd, NeighborFunction nf, EvaluationFunction ef, int[] overThr, int maxSecs, int findBest, double[] t, double[] cooling) {
    public static void testSA(HillClimbingProblem hcp, EvaluationFunction ef, double[] overThr, int maxSecs, int findBest, double[] t, double[] cooling) {

        // Distribution odd = new DiscreteUniformDistribution(ranges);
        // NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
        // HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        Instance bestData = null;
        {
            int mybest = 10000000;
            int bestint = 0;
            SimulatedAnnealing sa = null;
            for (int i=0; i<t.length; i++) {
                System.out.println("SA Executing t=[" + t[i] + "], cooling=[" + cooling[i] + "]");
                sa = new SimulatedAnnealing(t[i], cooling[i], hcp);
                int tmpBest;
                if (findBest == 1) {
                    tmpBest = TestFit.fitAndTime(sa, sa, ef, maxSecs, overThr, mybest);
                    if (i == t.length-1) {
                        bestData = sa.getOptimal();
                    }

                } else {
                    tmpBest = TestFit.fitAndTime(sa, sa, ef, maxSecs, overThr, -1);
                }
                if (mybest > tmpBest) {
                    mybest = tmpBest;
                    bestint = i;
                }
            }
            if (findBest == 1) {

                if (mybest < 10000000) {
                    System.out.println("SA data=");
                    for (int i=0; i<bestData.getData().size(); i++) {
                    // for (int i=0; i<ranges.length; i++) {
                        System.out.print("" + bestData.getData().get(i) + " ");
                    }
                    System.out.println("");
                }

                System.out.println("SA Best Time=[" + mybest + "]");
                System.out.println("SA Best t=[" + t[bestint] + "]");
                System.out.println("SA Best cooling=[" + cooling[bestint] + "]");
            }
        }

    }



    // public static void testGA(EvaluationFunction ef, CrossoverFunction cf, int[] ranges, int[] overThr, int maxSecs, int findBest, int[] populationSize, int[] toMate, int[] toMutate) {
    // public static void testGA(Distribution odd, MutationFunction mf, EvaluationFunction ef, CrossoverFunction cf, int[] overThr, int maxSecs, int findBest, int[] populationSize, int[] toMate, int[] toMutate) {
    public static void testGA(GeneticAlgorithmProblem gap, EvaluationFunction ef, double[] overThr, int maxSecs, int findBest, int[] populationSize, int[] toMate, int[] toMutate) {
            // Distribution odd = new DiscreteUniformDistribution(ranges);
        // MutationFunction mf = new DiscreteChangeOneMutation(ranges);

        // GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        int mybest = 10000000;
        int bestint = 0;
        StandardGeneticAlgorithm ga = null;
        Instance bestData = null;
        for (int i=0; i<populationSize.length; i++) {
            System.out.println("GA Executing populationSize=[" + populationSize[i] + "], toMate=[" + toMate[i] + "], toMutate=[" + toMutate[i] + "]");
            ga = new StandardGeneticAlgorithm(populationSize[i], toMate[i], toMutate[i], gap);
            int tmpBest;
            if (findBest == 1) {
                tmpBest = TestFit.fitAndTime(ga, ga, ef, maxSecs, overThr, mybest);
                if (i == populationSize.length-1) {
                    bestData = ga.getOptimal();
                }
            } else {
                tmpBest = TestFit.fitAndTime(ga, ga, ef, maxSecs, overThr, -1);
            }
            if (mybest > tmpBest) {
                mybest = tmpBest;
                bestint = i;
            }
        }
        if (findBest == 1) {
            if (mybest < 10000000) {
                System.out.println("GA data=");
                for (int i=0; i<bestData.getData().size(); i++) {
                // for (int i=0; i<ranges.length; i++) {
                    System.out.print("" + bestData.getData().get(i) + " ");
                }
                System.out.println("");
            }
            System.out.println("GA Best Time=[" + mybest + "]");
            System.out.println("GA Best populationSize=[" + populationSize[bestint] + "]");
            System.out.println("GA Best toMate=[" + toMate[bestint] + "]");
            System.out.println("GA Best toMutate=[" + toMutate[bestint] + "]");
        }
    }

    // public static void testMIMIC(Distribution odd, EvaluationFunction ef, int[] ranges, int[] overThr, int maxSecs, int findBest, int[] populationSize, int[] toMate) {
    // public static void testMIMIC(Distribution odd, Distribution df, EvaluationFunction ef, int[] overThr, int maxSecs, int findBest, int[] populationSize, int[] toMate) {
    public static void testMIMIC(ProbabilisticOptimizationProblem pop, EvaluationFunction ef, double[] overThr, int maxSecs, int findBest, int[] populationSize, int[] toMate) {

        // Distribution odd = new DiscreteUniformDistribution(ranges);
        // Distribution df = new DiscreteDependencyTree(.1, ranges);
        // ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);

        int mybest = 10000000;
        int bestint = 0;
        MIMIC mimic = null;
        Instance bestData = null;
        for (int i=0; i<populationSize.length; i++) {
            System.out.println("MIMIC Executing populationSize=[" + populationSize[i] + "], toMate=[" + toMate[i] + "]");
            mimic = new MIMIC(populationSize[i], toMate[i], pop);
            // int tmpBest = TestFit.fitAndTime(mimic, mimic, ef, 120, overThr, mybest);
            int tmpBest;
            if (findBest == 1) {
                tmpBest = TestFit.fitAndTime(mimic, mimic, ef, maxSecs, overThr, mybest);
                if (i == populationSize.length-1) {
                    bestData = mimic.getOptimal();
                }
            } else {
                tmpBest = TestFit.fitAndTime(mimic, mimic, ef, maxSecs, overThr, -1);
            }
            if (mybest > tmpBest) {
                mybest = tmpBest;
                bestint = i;
            }
        }
        if (findBest == 1) {
            if (mybest < 10000000) {
                System.out.println("MIMIC data=");
                for (int i=0; i<bestData.getData().size(); i++) {
                // for (int i=0; i<ranges.length; i++) {
                    System.out.print("" + bestData.getData().get(i) + " ");
                }
            }
            System.out.println("");
            System.out.println("MIMIC Best Time=[" + mybest + "]");
            System.out.println("MIMIC Best populationSize=[" + populationSize[bestint] + "]");
            System.out.println("MIMIC Best toMate=[" + toMate[bestint] + "]");
        }
    }

}
