package com.maze;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Player {

	private int x, y, tileX, tileY;
	private Image player;
	
	public Player() {
		ImageIcon img = new ImageIcon("src/resources/player.png");
		player = img.getImage();
	}
	
	public void setPlayerStart(int tX, int tY) {
		tileX = tX;
		tileY = tY;
		
		x = 0 -752;
		y = tY + 32 -784;
	}
	
	public Image getPlayer() {
		return player;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getTileX() {
		return tileX;
	}
	
	public int getTileY() {
		return tileY;
	}
	
	public void move(int dx, int dy, int tx, int ty) {
		x += dx;
		y += dy;
		
		tileX += tx;
		tileY += ty;
	}
}
