package AI;

import java.util.Comparator;

import ie.gmit.sw.ai.Node;

public class HeuristicNodeComparator implements Comparator<Node>{
	
	private Node goal;
	
	public HeuristicNodeComparator(Node goal) {
		super();
		this.goal = goal;
	}
	public int compare(Node node1, Node node2) {
		if (node1.getApproximateDistanceFromGoal() > node2.getApproximateDistanceFromGoal()){
			return -1;
		}else if (node1.getApproximateDistanceFromGoal() < node2.getApproximateDistanceFromGoal()){
			return 1;
		}else{
			return 0;
		}
	}
}
