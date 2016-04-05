package ie.gmit.sw.ai;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import AI.HeuristicCalculator;
import ie.gmit.sw.ai.Enemy;
import ie.gmit.sw.ai.Node;
import ie.gmit.sw.ai.NodeType;

public class Node {
	// Each block in the maze is represented as a node.
	// The node can hold every component of the game, from
	// npc's to the player to weapons
	private NodeType nodeType;
	private int row, col;
	private boolean visited = false;
	private boolean isGoalNode = false;
	private Set<Node> nodeSet;
	private boolean startingCell = false;
	private int approximateDistanceFromGoal = 0;
	private int distanceTravelled, approxDistanceToGoal;
	private Node parent;

	public int getHeuristic(Node goal){
		double x1 = this.col; 
		double y1 = this.row;

		double x2 = goal.getCol();
		double y2 = goal.getRow();
		return (int) Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
	}
	// Returns true if a path can be made through the node
	public boolean isTraversable() {
		return nodeType == NodeType.floor
				|| nodeType == NodeType.player
				|| nodeType == NodeType.arrow;
	}
	public boolean containsItem() {
		return nodeType == NodeType.bomb 
				|| nodeType == NodeType.hint
				|| nodeType == NodeType.weapon;
	}
	
	public boolean isStartingCell() {
		return startingCell;
	}
	
	public void setStartingCell(boolean startingCell) {
		this.startingCell = startingCell;
	}
	
	public int getDistanceTravelled() {
		return distanceTravelled;
	}

	public void setDistanceTravelled(int distanceTravelled) {
		this.distanceTravelled = distanceTravelled;
	}

	public int getApproxDistanceToGoal() {
		return approxDistanceToGoal;
	}

	public void setApproxDistanceToGoal(int approxDistanceToGoal) {
		this.approxDistanceToGoal = approxDistanceToGoal;
	}
	
	public float getScore() {
		return HeuristicCalculator.getHeuristicValue(distanceTravelled, approxDistanceToGoal);
	}
	
	public boolean isVisited() {
		return visited;
	}
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	public Node() {
		nodeSet = new HashSet<Node>();
		nodeSet.add(this);
	}
	
	public Node(int row, int column)
	  {
	   this.row = row;
	   this.col = column;
	  }

	public boolean isGoalNode() {
		return isGoalNode;
	}
	public void setGoalNode(boolean isGoalNode) {
		this.isGoalNode = isGoalNode;
	}

	
	
	// Returns true if the Node isn't a border Node
	public boolean isInBoundaries(Node[][] maze) {
		return row > 0 && col > 0 
				&& row < maze.length-1 && col < maze[0].length-1;
	}
	
	public List<Node> getAdjacentNodes(Node[][] maze) {
		List<Node> adjacentNodes = new ArrayList<Node>();
		
		if(row-1 > 0) adjacentNodes.add(maze[row-1][col]); // Node Above
		if(row+1 < maze.length-1) adjacentNodes.add(maze[row+1][col]); // Node Below
		if(col-1 > 0) adjacentNodes.add(maze[row][col-1]); // Node to left
		if(col+1 < maze[0].length-1) adjacentNodes.add(maze[row][col+1]); // Node to right
		return adjacentNodes; 
	}
	
	public boolean isInBorder(Node[][] maze) {
		// Returns true if Node is inside the outer border
		return row > 0 && row < maze.length-1 &&
				col > 0 && col < maze[0].length-1;
	}
	
	public void addNodeToSet(Node n) {
		nodeSet.add(n);
	}
	
	public NodeType getNodeType() {
		return nodeType;
	}
	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public Set<Node> getNodeSet() {
		return nodeSet;
	}
	public void setNodeSet(Set<Node> nodeSet) {
		this.nodeSet = nodeSet;
	}

	public int getApproximateDistanceFromGoal() {
		return approximateDistanceFromGoal;
	}	
	
	public void setApproximateDistanceFromGoal(int approximateDistanceFromGoal) {
		this.approximateDistanceFromGoal = approximateDistanceFromGoal;
	}

	public Node[] children() {
		return null;
	}

	public int getDistance(Node n) {
		
		return 0;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	 public boolean equals(Object obj)
	  {
		   if (this == obj)
		    return true;
		   if ((obj == null) || (obj.getClass() != this.getClass()))
		    return false;
		   Node Node = (Node) obj;
		   if (row == Node.row && col == Node.col)
		    return true;
		   return false;
	  }
	
	 
}
