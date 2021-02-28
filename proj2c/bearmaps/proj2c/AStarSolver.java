package bearmaps.proj2c;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import bearmaps.proj2ab.ExtrinsicMinPQ;
import edu.princeton.cs.algs4.Stopwatch;
import bearmaps.proj2ab.ArrayHeapMinPQ;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private double timeout;
    private SolverOutcome outcome;
    private List<Vertex> solution;
    private double solutionWeight;
    private HashMap<Vertex, Double> distTo;
    private double timeSpent;
    private int dequeueTimes;
    private HashMap<Vertex, Vertex> pathFrom;
    private Vertex start;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        Stopwatch sw = new Stopwatch();
        this.start = start;
        solutionWeight = 0.;
        solution = new LinkedList<>();
        distTo = new HashMap<>();
        pathFrom = new HashMap<>();
        ExtrinsicMinPQ<Vertex> pq = new ArrayHeapMinPQ<>();
        pq.add(start, 0);
        distTo.put(start, 0.);
        while (pq.size() > 0) {
            Vertex v = pq.removeSmallest();
            timeSpent = sw.elapsedTime();
            if (timeSpent > timeout) {
                outcome = SolverOutcome.TIMEOUT;
                return;
            }
            dequeueTimes += 1;
            if (v.equals(end)) {
                outcome = SolverOutcome.SOLVED;
                solutionWeight = distTo.get(v);
                constructPath(v, solution);
                return;
            }
            double distToV = distTo.get(v);
            for (WeightedEdge<Vertex> e: input.neighbors(v)) {
                if (!distTo.containsKey(e.to())) {
                    distTo.put(e.to(), distToV + e.weight());
                    pathFrom.put(e.to(), v);
                    pq.add(e.to(), distToV + e.weight() + input.estimatedDistanceToGoal(e.to(), end));
                }
                else if (distToV + e.weight() < distTo.get(e.to())) {
                    distTo.put(e.to(), distToV + e.weight());
                    pathFrom.put(e.to(), v);
                    if (pq.contains(e.to())) {
                        pq.changePriority(e.to(), distToV + e.weight() + input.estimatedDistanceToGoal(e.to(), end));
                    }
                    else {
                        pq.add(e.to(), distToV + e.weight() + input.estimatedDistanceToGoal(e.to(), end));
                    }
                }
            }
        }
        outcome = SolverOutcome.UNSOLVABLE;
        timeSpent = sw.elapsedTime();
    }
    public SolverOutcome outcome() {
        return outcome;
    }
    @Override
    public List<Vertex> solution() {
        return solution;
    }

    @Override
    public double solutionWeight() {
        return solutionWeight;
    }

    @Override
    public int numStatesExplored() {
        return dequeueTimes;
    }

    @Override
    public double explorationTime() { return timeSpent; }

    private void constructPath(Vertex v, List<Vertex> path) {
        if (v.equals(start)) {
            path.add(start);
        }
        else {
            constructPath(pathFrom.get(v), path);
            path.add(v);
        }
    }
}