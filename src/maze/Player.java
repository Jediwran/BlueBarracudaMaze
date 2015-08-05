package maze;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Player {

	private int x, y, tileX, tileY, number;
	private Image player, playerDown, playerRight, playerUp;
	//private String playerFile = "src/resources/player.png";
	private String playerFile = "src/resources/fish_left.png";
	private String playerDownFile = "src/resources/fish_down.png";
	private String playerRightFile = "src/resources/fish_right.png";
	private String playerUpFile = "src/resources/fish_up.png";
	private int playerLives = 5;
	private int playerStepsTaken = 0;
	private int TimesCaught = 0;
	private String color;
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getTimesCaught() {
		return TimesCaught;
	}

	public void setTimesCaught(int timesCaught) {
		TimesCaught = timesCaught;
	}

	public int getPlayerStepsTaken() {
		return playerStepsTaken;
	}

	public void setPlayerStepsTaken(int playerStepsTaken) {
		this.playerStepsTaken = playerStepsTaken;
	}

	public int getPlayerLives() {
		return playerLives;
	}

	public void setPlayerLives(int playerLives) {
		this.playerLives = playerLives;
	}

	public Player() {
		ImageIcon img = new ImageIcon(playerFile);
		player = img.getImage();
		img = new ImageIcon(playerDownFile);
		playerDown = img.getImage();
		img = new ImageIcon(playerRightFile);
		playerRight = img.getImage();
		img = new ImageIcon(playerUpFile);
		playerUp = img.getImage();
	}
	
	public void setPlayerStart(int tX, int tY) {
		tileX = tX;
		tileY = tY;
		
		//Normal 32x32 character
		x = tX * 32;
		y = tY * 32;
		
		//Character with black around him
		//x = tX * 32 - 783;
		//y = tY * 32 - 784;
	}
	
	public Image getPlayer() {
		return player;
	}
	
	public Image getPlayerDown(){
		return playerDown;
	}
	
	public Image getPlayerRight(){
		return playerRight;
	}
	
	public Image getPlayerUp(){
		return playerUp;
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
	
	public void setNumber(int num) {
		number = num;
	}
	
	public int getNumber() {
		return number;
	}
	
	public void move(int dx, int dy, int tx, int ty) {
		x += dx;
		y += dy;
		
		tileX += tx;
		tileY += ty;
	}
}
