package ie.gmit.sw.ai;

import java.util.Random;

import AI.RandomWalk;

public class EnemyImpl implements Enemy {
	
	/* Enemy Object Class
	 * Every Node that is a type enemy will have
	 * an independantly threaded Enemy Object 
	 */
	private Traversator t;
	private Random r = new Random(); 
	private int health = 1;
	
	public EnemyImpl(Node[][] maze, Node startNode, GameView g) throws Exception  {
		search(maze, startNode, g);
	}
	
	public void search(Node[][] maze, Node startNode, GameView g) {
		int i = r.nextInt(1);
		
		if(i == 0) t = new RandomWalk(maze, startNode, g); 
	}
	
	public void setMaze(Node[][] maze) {
		t.setMaze(maze);
	}
	
	public Node getCurrentNode() {
		return t.getCurrentNode();
	}
	
	public void setCurrentNode(Node currentNode) {
		t.setCurrentNode(currentNode);
	}
	
	public void setNextNode(Node nextNode) {
		t.setNextNode(nextNode);
	}

	public Node getNextNode() {
		return t.getNextNode();
	}
	
	public int getEnemyHealth() {
		return health;
	}

	public static void setEnemyHealth(int health) {
		health = health;
	}
}
