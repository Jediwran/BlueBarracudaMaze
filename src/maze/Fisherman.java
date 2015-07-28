package maze;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Fisherman {
	
	private Image fisherman;
	private int x, y, tileX, tileY;
	private String fishermanFile = "";
	
	public Fisherman() {
		ImageIcon img = new ImageIcon(fishermanFile);
	}
}
