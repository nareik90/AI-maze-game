package AI;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import ie.gmit.sw.ai.GameView;
import ie.gmit.sw.ai.Node;
import ie.gmit.sw.ai.NodeType;

public class GBFS {
	private GameView g;
	private Node[][] maze;
	private Node goal;
	
	private HeuristicNodeComparator heuristicComparer;
	List<Node> closed = new ArrayList<Node>();
	private PriorityQueue<Node> open;
	
	public GBFS(Node[][] maze, Node start, Node goal, GameView g) {
		this.maze = maze;
		System.out.println(goal);
		this.goal = goal;
		this.g= g;
		heuristicComparer = new HeuristicNodeComparator();
		open  = new PriorityQueue<Node>(20, heuristicComparer);
		new Thread(new Runnable() {
			public void run () {
				search(start);
			}
		}).start();
		setNodesAsUnvisited();
	}
	
	public void search(Node start) {
		open.add(start);

		while(!open.isEmpty()) {
			Node next = open.poll();
			System.out.println(next);
			if (next.isGoalNode()) {
				List<Node> path = new ArrayList<Node>();
				
				while (next.getParent() != null) {
					path.add(next);
					next = next.getParent();
				}
				// return with List of Nodes and repaint the scene 
				// to show path to goal
				g.showPath(path);
			}
			
			pushSuccessors(next);
			closed.add(next);
		}
		System.out.println("GOAL: " + goal.getRow() + "/" + goal.getCol());
	}
	
	public void pushSuccessors(Node next) {
		List<Node> children = next.getAdjacentNodes(maze);
		int score = 0, bestScore = 0;
		for (Node child : children) {
			score = child.getHeuristic(goal)+1;

			if(child.getNodeType() != NodeType.floor && child.getNodeType() != NodeType.enemy
					&& child.getNodeType() != NodeType.player && child.getNodeType()!= NodeType.trophy) continue;

			if ((open.contains(child) || closed.contains(child)) && child.getHeuristic(goal) < score) continue;
			
			else {
				
				bestScore = score;
				System.out.println("NODE : " + child.getRow() + " " + child.getCol());
				open.remove(next);
				closed.add(next);
				child.setParent(next);
				open.add(child);
			}
		}
	}
	
	private void setNodesAsUnvisited() {
		for(int r=0; r<maze.length;r++) {
			for(int c=0; c<maze[0].length;c++)
				maze[r][c].setVisited(false);
		}
	}
}
