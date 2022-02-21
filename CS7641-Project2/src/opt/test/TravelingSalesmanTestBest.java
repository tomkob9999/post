package opt.test;

import java.util.Arrays;
import java.util.Random;

import dist.DiscreteDependencyTree;
import dist.DiscretePermutationDistribution;
import dist.DiscreteUniformDistribution;
import dist.Distribution;

import opt.SwapNeighbor;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.*;
import opt.ga.CrossoverFunction;
import opt.ga.SwapMutation;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

/**

import opt.example.*;
/**
 * Copied from ContinuousPeaksTest
 * @version 1.0
 */
public class TravelingSalesmanTestBest {
    /** The n value */
    private static final int N = 50;
    /**
     * The test main
     * @param args ignored
     */
    public static void main(String[] args) {
        Random random = new Random();
        // create the random points
        double[][] points = new double[N][2];
        for (int i = 0; i < points.length; i++) {
            points[i][0] = random.nextDouble();
            points[i][1] = random.nextDouble();
        }
        // for rhc, sa, and ga we use a permutation based encoding
        TravelingSalesmanEvaluationFunction ef = new TravelingSalesmanRouteEvaluationFunction(points);
        Distribution odd = new DiscretePermutationDistribution(N);
        NeighborFunction nf = new SwapNeighbor();
        MutationFunction mf = new SwapMutation();
        CrossoverFunction cf = new TravelingSalesmanCrossOver(ef);
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        
        Distribution df = new DiscreteDependencyTree(.1); 
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);

        final double[] overThr = {0.07, 0.08, 0.09, 0.1};

        TemplateTest.testRHC(hcp, ef, overThr);

        {
            double[] t = {1E5, 1E5, 1E5};
            double[] cooling = {0.25, 0.5, 0.75};
            TemplateTest.testSA(hcp, ef, overThr, 60, 1, t, cooling);
        }
        {
            int[] populationSize = {200, 200, 200};
            int[] toMate = {50, 100, 150};
            int[] toMutate = new int[populationSize.length];
            for (int i=0; i<populationSize.length; i++) {
                toMutate[i] = (populationSize[i]-toMate[i])*3/4;
        }
            TemplateTest.testGA(gap, ef, overThr, 60, 1, populationSize, toMate, toMutate);
        }

        {
            int[] populationSize = {50,  100, 150, 200};
            int[] toMate = {25, 50, 75, 100};
            TemplateTest.testMIMIC(pop, ef, overThr, 60, 1, populationSize, toMate);
        }
    }
}
