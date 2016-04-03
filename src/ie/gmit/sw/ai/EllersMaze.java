package ie.gmit.sw.ai;


import java.util.*;

public class EllersMaze  implements Generator{
	private Random r = new Random();
	private Set<Set<Node>> sets = new HashSet<Set<Node>>();
	private Node[][] maze;
	public EllersMaze(Node[][] maze) {
		super();
		this.maze = maze;
		buildMaze();
	}
	
	// Initialize each Node
	// Starting off the maze in a grid pattern
	private void init(){
		int rowCounter = 0; int colCounter =0;
		int row = 0; int col = 0;
		for (row = 0; row < maze.length; row++){
			rowCounter++;
			for (col = 0; col < maze[row].length; col++){
				maze[row][col] = new Node();
				maze[row][col].setRow(row); maze[row][col].setCol(col);
				colCounter++;
				if(row < 1 || col < 1 
						|| row > maze.length-1 
						|| col > maze[0].length-1) maze[row][col].setNodeType(NodeType.wall);
				else if(colCounter % 2 == 0 && rowCounter % 2 == 0)  {
					maze[row][col].setNodeType(NodeType.floor);
					maze[row][col].setStartingCell(true);
					colCounter = 0;
				} else {
					maze[row][col].setNodeType(NodeType.wall);
				}
			}
		}
	}
	
	public void buildMaze() {
		init();
		// Implementation of Eller's Algorithm
		// 1. Initialize first row
		// Foreach col node in first row below border
		for(int i=1; i<maze.length;i+=2) {
			for(Node node : maze[i]) {
				if (node.getNodeType() == NodeType.floor && node.isStartingCell()) {
					// Initialize a new set for each starting node in the row
					if (node.isInBorder(maze) && node.getCol() < maze[0].length-1) {
						breakRightWall(node, node.getRow(), node.getCol());
					}					
				}
			}
			sets.clear();
			for(Node node : maze[i]) {
				if(node.isStartingCell()) sets.add(node.getNodeSet());
			}
			for (Set<Node> set : sets) {
				if(set.size() > 0 && i < maze.length-2)	breakBottomWall(set, i);
				else {
					finishMaze(set);
				}
			}
		}
	}
		
	// Randomize whether or not to break a wall
	private void breakRightWall(Node node, int row, int col) {
		int toBreak = r.nextInt(6);
		Node adjacentNode = maze[row][col+2];

		// Break down the wall to the right
		// If the two nodes are already part of the same set 
		// leave wall intact (This prevents loops)
		if(toBreak <= 4 && !node.getNodeSet().contains(adjacentNode)) {
			
			maze[row][col+1].setNodeType(NodeType.floor);
			Set<Node> tmp = node.getNodeSet();
			tmp.addAll(adjacentNode.getNodeSet());

			node.setNodeSet(tmp);
			adjacentNode.setNodeSet(node.getNodeSet());
		}
	}
	
	private void breakBottomWall(Set<Node> set, int level) {
		List<Node> bottomNodes = new ArrayList<Node>();
		
		bottomNodes = getBottomNodes(set, level);

		
		// Decide on how many times the set should break a wall
		// Must be at least one break for each set
		int numberOfBreaks = r.nextInt(bottomNodes.size())+1;
		int broken = 0;
		
		while (broken < numberOfBreaks) {
			// Pick random index from bottomNodes List and remove its bottom border
			int randNode = r.nextInt(bottomNodes.size());
			Node nodeToBreak = bottomNodes.get(randNode);
			Node bottomNode = maze[nodeToBreak.getRow()+1][nodeToBreak.getCol()];
			bottomNode.setNodeType(NodeType.floor);
			Node nextCell = maze[bottomNode.getRow()+1][bottomNode.getCol()];
			broken++;
			nodeToBreak.addNodeToSet(nextCell);
			nextCell.setNodeSet(nodeToBreak.getNodeSet());
		}
	}
	
	private void finishMaze(Set<Node> set) {
		List<Node> bottomNodes = getBottomNodes(set, maze.length-1);
		
		// Check adjacent node, if it isn't in the same set, break wall		
		for (Node n : bottomNodes) {
			if (n.getCol() < maze[0].length-2) {
				Node adjacentNode = maze[n.getRow()][n.getCol()+2];
				if (!n.getNodeSet().contains(adjacentNode)) 
					maze[n.getRow()][n.getCol()+1].setNodeType(NodeType.floor);
			}
		}
	}
	
	
	// Get the lowest Nodes in each set
	private List<Node> getBottomNodes(Set<Node> set, int level) {
		List<Node> nodes = new ArrayList<>();
		for(Node tmpNode : set) { 
			int currRow = tmpNode.getRow();
			if (currRow == level) {
				nodes.add(tmpNode);
			}
		}
		return nodes;
	}
}

