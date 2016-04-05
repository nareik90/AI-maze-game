package ie.gmit.sw.ai;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import AI.AStar;

public class GameView extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_VIEW_SIZE = 800;	
	private static final int IMAGE_COUNT = 11;
	private static final int MAZE_DIMENSION = 60;
	private MazeGenerator m = new MazeGenerator(MAZE_DIMENSION, MAZE_DIMENSION);
	private int cellspan = 5;	
	private int cellpadding = 2;
	private Node[][] maze;
	private BufferedImage[] images;
	private int enemy_state = 5;
	private int currentRow;
	private int currentCol;
	private Timer timer;
	private boolean zoomOut = false;
	private int imageIndex = -1;
	private List<Enemy> enemies = new ArrayList<Enemy>();
	private Player p;
	private Node goalNode;
	public Node goalValue;
	private Long startGameTime;
	
	public GameView () throws Exception {
		maze = m.getMaze();
		init();
		initializeEntities();
		setBackground(Color.LIGHT_GRAY);
		setDoubleBuffered(true);
		timer = new Timer(300, this);
		timer.start();
	}
	
	public void setCurrentRow(int row) {
		if (row < cellpadding){
			currentRow = cellpadding;
		}else if (row > (maze.length - 1) - cellpadding){
			currentRow = (maze.length - 1) - cellpadding;
		}else{
			currentRow = row;
		}
	}

	public void setCurrentCol(int col) {
		if (col < cellpadding){
			currentCol = cellpadding;
		}else if (col > (maze[currentRow].length - 1) - cellpadding){
			currentCol = (maze[currentRow].length - 1) - cellpadding;
		}else{
			currentCol = col;
		}
	}

	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        cellspan = zoomOut ? maze.length : 5;         
        final int size = DEFAULT_VIEW_SIZE/cellspan;
        g2.drawRect(0, 0, GameView.DEFAULT_VIEW_SIZE, GameView.DEFAULT_VIEW_SIZE);
        
        for(int row = 0; row < cellspan; row++) {
        	for (int col = 0; col < cellspan; col++){  
        		int x1 = col * size;
        		int y1 = row * size;
        		
        		NodeType ch = NodeType.wall;
       		
        		if (zoomOut){
        			ch = maze[row][col].getNodeType();
        			// Display character on map
        			if (row == currentRow && col == currentCol){
        				g2.setColor(Color.BLUE);
        				g2.fillRect(x1, y1, size, size);
        				continue;
        			} else if (maze[row][col].getNodeType() == NodeType.enemy) {
        				g2.setColor(Color.RED);
        				g2.fillRect(x1, y1, size, size);
        			} else if (maze[row][col].getNodeType() == NodeType.trophy) {
        				g2.setColor(Color.YELLOW);
        				g2.fillRect(x1, y1, size, size);
        			}
        		}else{
        			ch = maze[currentRow - cellpadding + row][currentCol - cellpadding + col].getNodeType();
        		}
        		
        		
        		if (ch == NodeType.wall){        			
        			imageIndex = 0;;
        		}else if (ch == NodeType.weapon){
        			imageIndex = 1;;
        		}else if (ch == NodeType.hint){
        			imageIndex = 2;;
        		}else if (ch == NodeType.bomb){
        			imageIndex = 3;;
        		}else if (ch == NodeType.hBomb){
        			imageIndex = 4;;
        		}else if (ch == NodeType.enemy){
        			imageIndex = 7;;     			
        		}else if (ch == NodeType.player) {
        			imageIndex = enemy_state;;      
        		}
        		else if (ch == NodeType.trophy) {
        			imageIndex = 9;;      
        		}
        		else if (ch == NodeType.arrow) {
        			imageIndex = 10;;      
        		}else{
        			imageIndex = -1;
        		}
        		if (imageIndex >= 0){
        			g2.drawImage(images[imageIndex], x1, y1, null);
        		}else{
        			g2.setColor(Color.WHITE);
        			g2.fillRect(x1, y1, size, size);
        		}      		
        	}
        }
	}
	
	public void toggleZoom(){
		zoomOut = !zoomOut;		
	}

	public void actionPerformed(ActionEvent e) {	
		if (enemy_state < 0 || enemy_state == 5){
			enemy_state = 6;
		}else{
			enemy_state = 5;
		}
		this.repaint();
	}
	
	public void updateEnemyPositions(Node current, Node next) {
		current.setNodeType(NodeType.floor);
		next.setNodeType(NodeType.enemy);
		
	}

	private void init() throws Exception{
		images = new BufferedImage[IMAGE_COUNT];
		images[0] = ImageIO.read(new java.io.File("resources/bush.png"));
		images[1] = ImageIO.read(new java.io.File("resources/sword.png"));		
		images[2] = ImageIO.read(new java.io.File("resources/help.png"));
		images[3] = ImageIO.read(new java.io.File("resources/bomb.png"));
		images[4] = ImageIO.read(new java.io.File("resources/hBomb.png"));
		images[5] = ImageIO.read(new java.io.File("resources/standing.png"));
		images[6] = ImageIO.read(new java.io.File("resources/runningleft.png"));
		images[7] = ImageIO.read(new java.io.File("resources/spider_down.png"));
		images[8] = ImageIO.read(new java.io.File("resources/spider_up.png"));
		images[9] = ImageIO.read(new java.io.File("resources/trophy.png"));
		images[10] = ImageIO.read(new java.io.File("resources/arrow.png"));
	}

	public MazeGenerator getMazeGenerator() {
		return m;
	}
	
	public Node[][] getMaze(){
		System.out.print("Start....");
		return m.getMaze();
	}
	
	public void setMaze(Node[][] maze) {
		m.setMaze(maze);
	}
	
	public int getMazeDimension() {
		return MAZE_DIMENSION;
	}
	
	public void showPath(List<Node> path) {
		startGameTime = System.currentTimeMillis();
		for(Node node : path) 
		{
			if (node.getNodeType() != NodeType.player &&
					node.getNodeType() != NodeType.enemy && node.getNodeType()!= NodeType.trophy) 
				node.setNodeType(NodeType.arrow);
		}
		if(System.currentTimeMillis() - startGameTime >= 5000){
		    stop(path);
		}

	}
	public void stop(List<Node> path){
		for(Node node : path) 
		{
			if (node.getNodeType() != NodeType.player &&
					node.getNodeType() != NodeType.enemy && node.getNodeType()!= NodeType.trophy) 
				node.setNodeType(NodeType.floor);
		}
	}
	
	/*private void initializeEnemys() throws Exception 
	{
		for(int row=0; row<maze.length; row++) 
		{
			for(int col=0; col<maze[0].length; col++)
			{	
				// Initialize enemy objects
				Node n = maze[row][col];
				if (n.getNodeType() == NodeType.enemy) {
					Enemy e = new EnemyImpl(maze, n, this);
					e.setCurrentNode(n);
					enemies.add(e);
				}
				else if (n.getNodeType() == NodeType.trophy) {
					goalNode = n;
					goalNode.setGoalNode(true);
					System.out.println("Goal Node: "+goalNode);
				}
				
			}
		}
	}*/

	private void initializeEntities() throws Exception {
		System.out.println("In");
		for(int row=0; row<maze.length; row++) {
			for(int col=0; col<maze[0].length; col++) {
				// Initialize enemy objects
				Node n = maze[row][col];
				if (n.getNodeType() == NodeType.enemy) {
					Enemy e = new EnemyImplementation(maze, n, this);
					e.setCurrentNode(n);
					enemies.add(e);
					//n.setEnemy(e);
				}
				// Initialize Player Object
				else if (n.getNodeType() == NodeType.player) {
					System.out.println("Player");
			    	p = new Player(n);
				}
				else if (n.getNodeType() == NodeType.trophy) {
					goalNode = n;
					goalNode.setGoalNode(true);
					System.out.println("Trophy: " + goalNode);
					goalValue = n;
				}
			}
	}
}
}