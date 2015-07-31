package maze;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Fisherman {
	
	private Image fisherman;
	private int x, y, tileX, tileY;
	private String fishermanFile = "src/resources/player.png";
	
	public Fisherman() {
		ImageIcon img = new ImageIcon(fishermanFile);
		fisherman = img.getImage();
	}
	
	public Image getFisherman() {
		return fisherman;
	}
	
	public int getFishermanX() {
		return x;
	}
	
	public int getFishermanY() {
		return y;
	}
	
	public int getFishermanTileX() {
		return tileX;
	}
	
	public int getFishermanTileY() {
		return tileY;
	}
	
	public void setFishermanStartLocation(int dx, int dy) {
		x = dx * 32;
		y = dy * 32;
		
		tileX = dx;
		tileY = dy;
	}
	
	public void move(int dx, int dy, int tx, int ty){
		x += tx * 32;
		y += ty * 32;
		
		tileX += tx;
		tileY += ty;
	}
}
