package ie.gmit.sw.ai;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.management.modelmbean.ModelMBean;
import javax.swing.*;

import AI.AStar;
import ie.gmit.sw.ai.GameView;
import ie.gmit.sw.ai.Node;
import ie.gmit.sw.ai.NodeType;
import ie.gmit.sw.ai.*;

public class GameRunner implements KeyListener{
	private Node[][] model;
	private GameView view;
	private AStar star;
	private int currentRow;
	private int currentCol;
	private int mazeDimension;
	private int counter = 0;
	public Node startNode;
	static boolean[][] mazeCheck = new boolean[5][5];
	public Node getter;
	private Node starting, finish;
	static Node start = new Node(0, 0);
	
	public GameRunner() throws Exception{
		view = new GameView();
		model = view.getMaze();
    	mazeDimension = view.getMazeDimension();

    	placePlayer();
    	
    	Dimension d = new Dimension(GameView.DEFAULT_VIEW_SIZE, GameView.DEFAULT_VIEW_SIZE);
    	view.setPreferredSize(d);
    	view.setMinimumSize(d);
    	view.setMaximumSize(d);
    	
    	JFrame f = new JFrame("GMIT - B.Sc. in Computing (Software Development)");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.addKeyListener(this);
        f.getContentPane().setLayout(new FlowLayout());
        f.add(view);
        f.setSize(1000,1000);
        f.setLocation(100,100);
        f.pack();
        f.setVisible(true);
        star = new AStar(model, starting, start, view);
	}
	
	public void placePlayer(){   	
		boolean placed = false;
		
		while(!placed) {
	    	currentRow = (int) (mazeDimension * Math.random());
	    	currentCol = (int) (mazeDimension * Math.random());
	    	
	    	if (model[currentRow][currentCol].getNodeType() == NodeType.floor) 
	    	{
	    		placed = true;	
	    		starting = model[currentRow][currentCol];
	    	}
		}
		if(placed == true){
			startNode = model[currentRow][currentCol];
		}
    	model[currentRow][currentCol].setNodeType(NodeType.player);
    	updateView(); 		
	}
	
	public static void longestPath(){
		List< Node > path = findLongestPath(mazeCheck);
		if(NodeType.floor != null)
		{
			 if (path == null)
			  {
				 System.out.println("None");
				 System.out.println("No path possible");
				 return;
			  }
			  for (Node node : path)
			  {
				  //System.out.print("pathways: "+ node + ",");
				  
			  }
		  }
		return;
	}
	
	private void updateView(){
		view.setCurrentRow(currentRow);
		view.setCurrentCol(currentCol);
	}

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && currentCol < mazeDimension - 1) {
        	if (isValidMove(currentRow, currentCol + 1)) currentCol++;	
        }else if (e.getKeyCode() == KeyEvent.VK_LEFT && currentCol > 0) {
        	if (isValidMove(currentRow, currentCol - 1)) currentCol--;	
        }else if (e.getKeyCode() == KeyEvent.VK_UP && currentRow > 0) {
        	if (isValidMove(currentRow - 1, currentCol)) currentRow--;
        }else if (e.getKeyCode() == KeyEvent.VK_DOWN && currentRow < mazeDimension - 1) {
        	if (isValidMove(currentRow + 1, currentCol)) currentRow++;        	  	
        }else if (e.getKeyCode() == KeyEvent.VK_Z){
        	view.toggleZoom();
        	if (isValidMove(currentRow + 1, currentCol)) currentRow++;        	  	
        }else if (e.getKeyCode() == KeyEvent.VK_X){
        	//view.toggleZoom();
        }else{
        	return;
        }
        
        updateView();       
    }
    public void keyReleased(KeyEvent e) {} //Ignore
	public void keyTyped(KeyEvent e) {} //Ignore

    
	private boolean isValidMove(int r, int c){
		if (r <= model.length - 1 && c <= model[r].length - 1 && model[r][c].getNodeType() == NodeType.floor){
			model[currentRow][currentCol].setNodeType(NodeType.floor);
			model[r][c].setNodeType(NodeType.player);
			return true;
		}
		else if(r <= model.length - 1 && c <= model[r].length - 1 && model[r][c].getNodeType() == NodeType.weapon){
			System.out.print("Sword Swishhh...");
			model[r][c].setNodeType(NodeType.wall);
			return false;
		}
	    else if(r <= model.length - 1 && c <= model[r].length - 1 && model[r][c].getNodeType() == NodeType.bomb){
			System.out.print("Bomb Booommm...");
			model[r][c].setNodeType(NodeType.wall);
			return false;
		}
	    else if(r <= model.length - 1 && c <= model[r].length - 1 && model[r][c].getNodeType() == NodeType.hBomb){
			System.out.print("Sure ill Help:....");
			model[r][c].setNodeType(NodeType.wall);
			//GameView.longestPath();
			return false;
		}
	    else if(r <= model.length - 1 && c <= model[r].length - 1 && model[r][c].getNodeType() == NodeType.enemy){
			System.out.print("Bomb Booommm...");
			model[r][c].setNodeType(NodeType.floor);
			return false;
		}
	    else{
			return false; //Can't move
		}
	}
	
	private static List< Node > findLongestPath(boolean[][] maze)
	 {
	  //Node start = new Node(0, 0);
	 // start =  runer.startNode ;
	 // Node start = runer.getStartingPosition();
	  System.out.println("List longest path: "+start);
	  Node end = new Node(maze.length - 1, maze[0].length - 1);
	 //System.out.println("End: "+end);
	  List< Node > path = findLongestPath(maze, start, end);
	 // System.out.println("find method" + path);
	  return path;
	 }
	
	private static List< Node > findLongestPath(boolean[][] maze, Node start, Node end)
	{
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
		  
		 }
	
	public static void main(String[] args) throws Exception{
		new GameRunner();
		
		longestPath();
	}
}