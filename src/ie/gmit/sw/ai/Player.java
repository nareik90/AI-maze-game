package ie.gmit.sw.ai;

public class Player {
	private Node currentNode;

	private int health = 1;
	
	public Player(Node currentNode) {
		this.currentNode = currentNode;
	}
	
	public Node getCurrentNode() {
		return currentNode;
	}
	
	public void setCurrentNode(Node currentNode) {
		this.currentNode = currentNode;
	}	

	public int getHealth() {
		return health;
	}

	public static void setHealth(int health) {
		health = health;
	}
}
