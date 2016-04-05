package ie.gmit.sw.ai;

import java.util.HashSet;
import java.util.Set;

public class MazeGenerator {
	private Node[][] maze;
	private Generator g;
	Set<Set<Node>> sets = new HashSet<Set<Node>>();
	
	
	public MazeGenerator(int rows, int cols) {
		super();
		maze = new Node[rows][cols];
		g = new EllersMaze(maze);
		
		initializeFeatures();
	}
	
	
	private void initializeFeatures() {
		/**
		 * Initialize Game Features
		 * - 1 Enemies
		 * - 2 Weapons and Bombs 
		 * - 3 Hints
		 */
		
		addFeature(NodeType.enemy, NodeType.floor, 10);
		addFeature(NodeType.weapon, NodeType.wall, 20);
		addFeature(NodeType.bomb, NodeType.wall, 5);
		addFeature(NodeType.hBomb, NodeType.wall, 10);
		addFeature(NodeType.trophy, NodeType.floor, 1);
	}
	
	private void addFeature(NodeType feature, NodeType replace, int number){
		int counter = 0;
		while (counter < number){
			int row = (int) (maze.length * Math.random());
			int col = (int) (maze[0].length * Math.random());
					
			if (maze[row][col].getNodeType() == replace && checkBorders(row, col)){
				maze[row][col].setNodeType(feature);
				counter++;
			}
		}
	}
	
	private boolean checkBorders(int row, int col) {
		return row > 0 && col > 0 
				&& row < maze.length - 1
				&& col < maze[row].length - 1;
	}
	
	public Node[][] getMaze() {
		return maze;
	}

	public void setMaze(Node[][] maze) {
		this.maze = maze;
	}
}
