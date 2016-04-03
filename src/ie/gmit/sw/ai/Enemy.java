package ie.gmit.sw.ai;

import ie.gmit.sw.ai.GameView;
import ie.gmit.sw.ai.Node;

public interface Enemy {
	
	public void search(Node[][] maze, Node startNode, GameView g);
	
	public void setMaze(Node[][] maze); 
	
	public void setCurrentNode(Node currentNode);	public Node getCurrentNode();
	public void setNextNode(Node nextNode); public Node getNextNode();
}

