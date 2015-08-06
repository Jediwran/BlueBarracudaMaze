package maze;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Player {

	private int x, y, tileX, tileY, number;
	private Image playerLeft, playerDown, playerRight, playerUp;
	private String playerFile = "src/resources/fish_left_";
	private String playerDownFile = "src/resources/fish_down_";
	private String playerRightFile = "src/resources/fish_right_";
	private String playerUpFile = "src/resources/fish_up_";
	private int playerLives = 5;
	private int playerStepsTaken = 0;
	private int TimesCaught = 0;
	private int direction = 3;
	private String color;
	
	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
	
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
		
	}
	
	public void setPlayerImages(){
		ImageIcon img = new ImageIcon(playerFile + color + ".png");
		playerLeft = img.getImage();
		img = new ImageIcon(playerDownFile + color + ".png");
		playerDown = img.getImage();
		img = new ImageIcon(playerRightFile + color + ".png");
		playerRight = img.getImage();
		img = new ImageIcon(playerUpFile + color + ".png");
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
	
	public Image getPlayerLeft() {
		return playerLeft;
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
