package AI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import ie.gmit.sw.ai.*;
import ie.gmit.sw.ai.Node;
import ie.gmit.sw.ai.NodeType;

public class AStar 
{	
	private HeuristicNodeComparator heuristicComparer;
	private List<Node> closed = new ArrayList<Node>();
	private PriorityQueue<Node> open;
	
	private GameView g;
	private Node[][] maze;
	private Node goal;
	
	public AStar(Node[][] maze, Node start, Node goal, GameView g) {
		this.maze = maze;
		System.out.println("Start GOAL!!!"+start);
		this.goal = goal;
		this.g= g;
		heuristicComparer = new HeuristicNodeComparator(goal);
		open  = new PriorityQueue<Node>(20, heuristicComparer);
		
		new Thread(new Runnable() 
		{
			public void run () 
			{
				search(start);
			}
		}).start();
	}
	
	public void search(Node start) {
		open.add(start);
		System.out.println("In search A Star");
		while(!open.isEmpty()) {
			Node next = open.poll();
			
			if (next.isGoalNode()) {
				List<Node> path = new ArrayList<Node>();
				
				while (next.getParent() != null) {
					path.add(next);
					next = next.getParent();
				}
				// return with List of Nodes and repaint the scene 
				// to show path to goal
				// 1 - Show path
				g.showPath(path);
				// 2 - wait 7 seconds
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 3 - Hide path
			}
			
			pushSuccessors(next);
			closed.add(next);
		}
	}
	
	public void pushSuccessors(Node next){
		List<Node> children = next.getAdjacentNodes(maze);
		int score = 0;
		for (Node child : children) {
			score = child.getHeuristic(goal)+1;
			System.out.println("Score: " + score);
			if(child.getNodeType() != NodeType.floor && child.getNodeType() != NodeType.enemy
					&& child.getNodeType() != NodeType.player && child.getNodeType()!= NodeType.goal) continue;

			if ((open.contains(child) || closed.contains(child)) && child.getHeuristic(goal) < score) continue;
			
			else {
				open.remove(next);
				closed.add(next);
				child.setParent(next);
				open.add(child);
			}
		}
		
	}
}
