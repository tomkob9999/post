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
// import opt.RandomizedHillClimbing;
// import opt.SimulatedAnnealing;
import opt.example.*;
import opt.ga.CrossoverFunction;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
// import opt.ga.StandardGeneticAlgorithm;
import opt.ga.UniformCrossOver;
import opt.prob.GenericProbabilisticOptimizationProblem;
// import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
// import shared.FixedIterationTrainer;

/**
 * Copied from ContinuousPeaksTest
 * @version 1.0
 */
public class TwoColorsTestBest {
    /** The number of colors */
    private static final int k = 2;
    /** The N value */
    private static final int N = 100*k;


    public static void main(String[] args) {
        int[] ranges = new int[N];
        Arrays.fill(ranges, k+1);
        EvaluationFunction ef = new TwoColorsEvaluationFunction();
        Distribution odd = new DiscreteUniformDistribution(ranges);
        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
        MutationFunction mf = new DiscreteChangeOneMutation(ranges);
        CrossoverFunction cf = new UniformCrossOver();
        Distribution df = new DiscreteDependencyTree(.1, ranges); 
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);

        final double[] overThr = {50, 100, 150, 198};

        TemplateTest.testRHC(hcp, ef, overThr);

        {
            double[] t = {1E3, 1E4, 1E5, 1E6};
            // double[] cooling = {0.25, 0.5, 0.75};
            double[] cooling = {0.5, 0.5, 0.5, 0.5};
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
