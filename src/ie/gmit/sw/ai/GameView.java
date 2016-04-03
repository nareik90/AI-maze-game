package ie.gmit.sw.ai;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.*;
import javax.swing.*;

import ie.gmit.sw.ai.Enemy;
import ie.gmit.sw.ai.MazeGenerator;
import ie.gmit.sw.ai.Node;
import ie.gmit.sw.ai.NodeType;

public class GameView extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_VIEW_SIZE = 800;	
	private static final int IMAGE_COUNT = 10;
	private static final int MAZE_DIMENSION = 40;
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
	private boolean hintActive = false;
	private Timer hintTimer;
	
	private static GameRunner runer;
	
	public GameView () throws Exception {
		maze = m.getMaze();
		init();
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

/*	public static void longestPath(){
		System.out.print("Searching...");
		List< Node > path = findLongestPath(mazeCheck);
		 if (path == null)
		  {
			 System.out.println("None");
			 System.out.println("No path possible");
			 return;
		  }
		  for (Node node : path)
		  {
			  System.out.print(node + ",");
			  System.out.println();
		  }
		return;
	}*/

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
        				g2.setColor(Color.WHITE);
        				g2.fillRect(x1, y1, size, size);
        				continue;
        			} else if (maze[row][col].getNodeType() == NodeType.enemy) {
        				g2.setColor(Color.RED);
        				g2.fillRect(x1, y1, size, size);
        			} else if (maze[row][col].getNodeType() == NodeType.goal) {
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
        		} else{
        			imageIndex = -1;
        		}
        		
        		if (imageIndex >= 0){
        			g2.drawImage(images[imageIndex], x1, y1, null);
        		}else{
        			g2.setColor(Color.LIGHT_GRAY);
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
		System.out.println("GOAL!!!");
//		if (!hintTimer.isRunning()) {
			for(Node node : path) {
				if (node.getNodeType() != NodeType.player &&
						node.getNodeType() != NodeType.enemy && node.getNodeType()!= NodeType.goal) 
					node.setNodeType(NodeType.path);
			}
//			new TimerTask() {	
//				public void run() {
					hidePath();
//				}
//			};
//		}
		hintActive = false;
	}
	
public void hidePath() {
		
	}
	
	/*private static List< Node > findLongestPath(boolean[][] maze)
	 {
	  Node start = new Node(0, 0);
	 // start =  runer.startNode ;
	 // Node start = runer.getStartingPosition();
	  System.out.println("Start: "+start);
	  Node end = new Node(maze.length - 1, maze[0].length - 1);
	  System.out.println("End: "+end);
	  List< Node > path = findLongestPath(maze, start, end);
	  System.out.println("find method" + path);
	  return path;
	 }
	
	private static List< Node > findLongestPath(boolean[][] maze, Node start, Node end)
	{
		
		Node newCol = start;
		  List< Node > result = null;
		  int rows = maze.length;
		  int cols = maze[0].length;
		  if (start.getRow() < 0 || start.getCol() < 0)
		   return null;
		  if (start.getRow() == rows || start.getCol() == cols)
		   return null;
		  if (maze[start.getRow()][start.getCol()] == true)
		   return null;
		  if (start.equals(end))
		  {
			   List< Node > path = new ArrayList< Node >();
			   path.add(start);
			  //System.out.println("start" + start);
			   return path;
		  }
		  
		  maze[start.getRow()][start.getCol()] = true;
		  Node[] nextNodes = new Node[4];
		  nextNodes[0] = new Node(start.getRow() + 1, start.getCol());
		  nextNodes[2] = new Node(start.getRow(), start.getCol() + 1);
		  nextNodes[1] = new Node(start.getRow() - 1, start.getCol());
		  nextNodes[3] = new Node(start.getRow(), start.getCol() - 1);
		  int maxLength = -1;
		  
		  for (Node nextNode : nextNodes)
		  {
			   List< Node > path = findLongestPath(maze, nextNode, end);
			   if (path != null && path.size() > maxLength)
			   {
				    maxLength = path.size();
				    path.add(0, start);
				    result = path;
				    //System.out.println("find method" + path);
			   }
		  }
		  maze[start.getRow()][start.getCol()] = false;
		  if (result == null || result.size() == 0)
		   return null;
		  return result;
		 }*/
}