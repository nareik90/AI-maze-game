package AI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ie.gmit.sw.ai.GameRunner;
import ie.gmit.sw.ai.Node;

public class HillClimbing {
	
	LinkedList<Node> queue = new LinkedList<Node>();
	List<Node> closed = new ArrayList<Node>();
	HeuristicNodeComparator sorter = new HeuristicNodeComparator();
	
	
	public void search(Node node){
		queue.addFirst(node);
		while(!queue.isEmpty()){
			queue.removeFirst();
			closed.add(node);
			System.out.print(node.getCol());
			if (node.isGoalNode()){
				System.out.println("Reached goal node " + node.getNodeType() + " after " + calcTotalDistanceTravelled() + " miles.");
				System.out.println("Path: " + closed);
				System.out.println(queue);
				System.exit(0);
			}else{
				Node[] children = node.children();
				Collections.sort(Arrays.asList(children), sorter);
				for (int i = 0; i < children.length; i++) {
					if (!children[i].isVisited() && !queue.contains(children[i])){
						children[i].setParent(node);
						queue.addFirst(children[i]);
					}
				}
				System.out.println(queue);
				node = queue.getFirst();
				node.setVisited(true);
			}
		}
	}
	
	private int calcTotalDistanceTravelled(){
		int totalDistance = 0;
		for (int j = 0; j < closed.size(); j++) {
			Node n = closed.get(j);
			Node parent = n.getParent();
			if (parent != null){
				totalDistance = totalDistance + parent.getDistance(n);
			}
		}
		return totalDistance;
	}
	
/*	public static void main(String[] args) {
		GameRunner gv = null;
		try {
			gv = new GameRunner();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Node start = gv.getStartNode();
		start.setVisited(true);
		HillClimbing hc = new HillClimbing();
		hc.search(start);
	}*/
}
