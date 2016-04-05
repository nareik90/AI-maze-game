package AI;

import java.util.*;
import ie.gmit.sw.*;
import ie.gmit.sw.ai.GameView;
import ie.gmit.sw.ai.Node;
import ie.gmit.sw.ai.NodeType;
import ie.gmit.sw.ai.Traversator;

public class RandomWalk implements Traversator {
	private Node[][] maze;
	private GameView g;
	private volatile Node currentNode, nextNode;
	private boolean complete = false;
	
	public RandomWalk(Node[][] maze, Node currentNode, GameView g){
		this.maze = maze;
		this.currentNode = currentNode;
		this.g = g;
		
		nextNode = currentNode;

		new Thread(new Runnable() {
			public void run() {
				search(currentNode);
			}
		}).start();
	}
	
	public void search(Node currentNode) {
		
		while(!complete){			
			try { // Move once per second
				Thread.sleep(1250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}		
			//Pick a random adjacent node
        	List<Node> children = currentNode.getAdjacentNodes(maze);
        	int nextNodeIndex = (int)(children.size() * Math.random());
        	boolean playerFound = false;
        	
        	for(Node neighbour : children) { 
        		if (neighbour.getNodeType() == NodeType.player) {
        			nextNode = neighbour;
        			complete = true;
        			break;
        		}
        	}
        	
        	// Pick random nearby Node until an available position is found
        	while (!playerFound && (children.get(nextNodeIndex).getNodeType() != NodeType.floor
        			&& children.get(nextNodeIndex).getNodeType() != NodeType.player)) {
           		nextNodeIndex = (int)(children.size() * Math.random());
        		
        	}
        	// Move enemy in new direction
        	nextNode = children.get(nextNodeIndex);

        	g.updateEnemyPositions(currentNode, nextNode);
        	currentNode = nextNode;
		}
	}
	
	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	
	public Node getCurrentNode() {
		return currentNode;
	}
	
	public void setCurrentNode(Node currentNode) {
		this.currentNode = currentNode;
	}
	
	public void setNextNode(Node nextNode) {
		this.nextNode = nextNode;
	}
	
	public Node getNextNode() {
		return nextNode;
	}

	public void setMaze(Node[][] maze) {
		this.maze = maze;
	}	
}
