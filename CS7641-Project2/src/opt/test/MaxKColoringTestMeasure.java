package opt.test;

import java.util.Arrays;
import java.util.Random;

import opt.ga.MaxKColorFitnessFunction;
import opt.ga.Vertex;

import dist.DiscreteDependencyTree;
import dist.DiscretePermutationDistribution;
import dist.DiscreteUniformDistribution;
import dist.Distribution;
import opt.DiscreteChangeOneNeighbor;
import opt.EvaluationFunction;
import opt.SwapNeighbor;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.ga.CrossoverFunction;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.SingleCrossOver;
import opt.ga.SwapMutation;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.ga.UniformCrossOver;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;


/**
 * Copied from ContinuousPeaksTest
 * @version 1.0
 */
public class MaxKColoringTestMeasure {
   /** The n value */
   private static final int N = 50; // number of vertices
   private static final int L =4; // L adjacent nodes per vertex
   private static final int K = 8; // K possible colors
   /**
    * The test main
    * @param args ignored
    */
   public static void main(String[] args) {
       Random random = new Random(N*L);
       // create the random velocity
       Vertex[] vertices = new Vertex[N];
       for (int i = 0; i < N; i++) {
           Vertex vertex = new Vertex();
           vertices[i] = vertex;	
           vertex.setAdjMatrixSize(L);
           for(int j = 0; j <L; j++ ){
                vertex.getAadjacencyColorMatrix().add(random.nextInt(N*L));
           }
       }

       MaxKColorFitnessFunction ef = new MaxKColorFitnessFunction(vertices);
       Distribution odd = new DiscretePermutationDistribution(K);
       NeighborFunction nf = new SwapNeighbor();
       MutationFunction mf = new SwapMutation();
       CrossoverFunction cf = new SingleCrossOver();
       HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
       GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
       
       Distribution df = new DiscreteDependencyTree(.1); 
       ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);

        final double[] overThr = {100, 200, 300, 350};

        TemplateTest.testRHC(hcp, ef, overThr);
        System.out.println(ef.foundConflict());

        {
            double[] tBEST = {1E5};
            double[] coolingBEST = {0.25};
            TemplateTest.testSA(hcp, ef, overThr, 60, 0, tBEST, coolingBEST);
            System.out.println(ef.foundConflict());
        }
        {
            int[] populationSizeBEST = {200};
            int[] toMateBEST = {50};
            int[] toMutateBEST = {112};
            // CrossoverFunction cf = new SingleCrossOver();
            TemplateTest.testGA(gap, ef, overThr, 60, 0, populationSizeBEST, toMateBEST, toMutateBEST);
            System.out.println(ef.foundConflict());
        }

        {
            int[] populationSizeBEST = {200};
            int[] toMateBEST = {100};
            TemplateTest.testMIMIC(pop, ef, overThr, 60, 0, populationSizeBEST, toMateBEST);
            System.out.println(ef.foundConflict());
        }

    }

}
