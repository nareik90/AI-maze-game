package ie.gmit.sw.ai;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import java.io.FileInputStream;

import AI.AStar;

public class GameRunner implements KeyListener{
	//String fileName = "AI/contact.fcl";
	//FIS fis = FIS.load(fileName,true);
	
	public Node position;
	private Node[][] model;
	private GameView view;
	private AStar star;
	private int currentRow;
	private int currentCol;
	private static int mazeDimension;
	public static Node startNode;
	static Node start = new Node(0, 0);
	static Node end;
	Player p;
	int health;
	int currentHealth = 100;
	int enemyHealth;
	int currentEnemyHealth = 10;
	private Node goal;
	private HUD hud = new HUD(mazeDimension);
	
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
        longestPath();
       
        
	}
	
	public void placePlayer(){   	
		boolean placed = false;
		while(!placed) {
	    	currentRow = (int) (mazeDimension * Math.random());
	    	currentCol = (int) (mazeDimension * Math.random());
	    	
	    	if (model[currentRow][currentCol].getNodeType() == NodeType.floor) 
	    	{
	    		placed = true;	
	    	}
		}
		if(placed == true){
			startNode = model[currentRow][currentCol];
		}
    	model[currentRow][currentCol].setNodeType(NodeType.player);
    	updateView(); 		
	}
	
	@SuppressWarnings("unused")
	public static void longestPath(){
		boolean[][] mazeCheck = new boolean[mazeDimension - 1][mazeDimension -1];
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
				  System.out.print(path + ",");
				  System.out.println();
				 end.setNodeType(NodeType.trophy);
			  }
		  }
		return;
	}
	
	private void updateView(){
		view.setCurrentRow(currentRow);
		view.setCurrentCol(currentCol);
	}
	
	@SuppressWarnings("unused")
	private void activateSearch(Node[][] maze, Node startNode, Node goal, GameView view){
		new AStar(maze, startNode, goal, view);
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
			 health = (int) (currentHealth * Math.random());
			 Player.setHealth(health);
			 System.out.println("Health: " +health);
			 return false;
		}
	    else if(r <= model.length - 1 && c <= model[r].length - 1 && model[r][c].getNodeType() == NodeType.bomb){
			System.out.print("Bomb Booommm...");
			health = 10;
			model[r][c].setNodeType(NodeType.wall);
			return false;
		}
	    else if(r <= model.length - 1 && c <= model[r].length - 1 && model[r][c].getNodeType() == NodeType.arrow){
			System.out.print("Follow");
			model[r][c].setNodeType(NodeType.wall);
			return false;
		}
	    else if(r <= model.length - 1 && c <= model[r].length - 1 && model[r][c].getNodeType() == NodeType.hBomb)
	    {
			System.out.print("Helper:....");
			model[r][c].setNodeType(NodeType.wall);
			view.goalValue = position;
			//System.out.println("goal Value: "+position);
		    new AStar(model, startNode, position, view);
			return false;
		}
	    else if(r <= model.length - 1 && c <= model[r].length - 1 && model[r][c].getNodeType() == NodeType.trophy)
	    {
			System.out.print("Helper:....");
			model[r][c].setNodeType(NodeType.floor);
			//star = new AStar(model, starting, start, view);
			return false;
		}
	    else if(r <= model.length - 1 && c <= model[r].length - 1 && model[r][c].getNodeType() == NodeType.enemy){
			System.out.print("Enemy Ahhhh...");
			try {
				enemyFight();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			model[r][c].setNodeType(NodeType.floor);
			health = 1;
			return false;
		}
	    else{
			return false; //Can't move
		}
	}

	private void enemyFight() throws Exception{
		 enemyHealth = (int) (currentEnemyHealth * Math.random());
		 EnemyImplementation.setEnemyHealth(enemyHealth);
		 System.out.println("Enemy Health: " + enemyHealth);
		 System.out.println("Player Health: " + health);
		
		 if(enemyHealth > health)
		 {
			new GameRunner();
		 }
		 
	}
	
	private static List< Node > findLongestPath(boolean[][] maze)
	 {
		  //Node start = new Node(0, 0);
		start = startNode;
		System.out.println("Start Node: "+startNode);
		end = new Node(maze.length - 1, maze[0].length - 1);
		end.setNodeType(NodeType.trophy); 
		  
		List< Node > path = findLongestPath(maze, start, end);
			
		  
		  return path;
	 }
	
	private static List< Node > findLongestPath(boolean[][] maze, Node start, Node end)
	{	
		  List< Node > result = null;
		  int rows = maze.length;
		  int cols = maze[0].length;
		  
		if(start.getNodeType() == NodeType.floor)
		{
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
					    System.out.println("find method" + path);
				   }
			  }
		
			  maze[start.getRow()][start.getCol()] = false;
			  if (result == null || result.size() == 0)
			   return null;
		}
			  return result;
			
	}
	
	public static void main(String[] args) throws Exception{
		
		new GameRunner();
		
	}
}