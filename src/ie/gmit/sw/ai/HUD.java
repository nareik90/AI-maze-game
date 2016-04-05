package ie.gmit.sw.ai;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class HUD {
	private int DEFAULT_VIEW_SIZE;
	private boolean gameOver;
	
	public HUD(int DEFAULT_VIEW_SIZE)
	{
		this.DEFAULT_VIEW_SIZE = DEFAULT_VIEW_SIZE;
	}
	public void showHealth(Player p, Graphics2D g2)
	{
		// Implement a health bar based on player's current health from 0-100
		Font font = new Font(Font.DIALOG_INPUT, Font.BOLD, 20);
		g2.setFont(font);
		FontMetrics f = g2.getFontMetrics(font);
        g2.setColor(Color.black);
        g2.fillRect(DEFAULT_VIEW_SIZE/2-100, 39, 101*2,12);
        String str = "Health";
        g2.drawString(str, DEFAULT_VIEW_SIZE/2-f.stringWidth(str)/2, 30);
        if(p.getHealth() > 30)
        {
        	g2.setColor(Color.green);
        }
        else 
    	{
        	g2.setColor(Color.red);
    	}
        if(p.getHealth() >= 0 )
        {
        	g2.fillRect(DEFAULT_VIEW_SIZE/2-100, 40, p.getHealth()*2,10);
        }
	}
}
