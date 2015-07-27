package maze;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Player {

	private int x, y, tileX, tileY;
	private Image player;
	private String playerFile = "src/resources/player.png";
	
	public Player() {
		ImageIcon img = new ImageIcon(playerFile);
		player = img.getImage();
	}
	
	public void setPlayerStart(int tX, int tY) {
		tileX = tX;
		tileY = tY;
		
		//Normal 32x32 character
		//x = tX * 32;
		//y = tY * 32;
		
		//Character with black around him
		x = tX * 32 - 783;
		y = tY * 32 - 784;
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
