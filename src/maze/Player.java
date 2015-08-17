package maze;

import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

public class Player{

	private int x, y, tileX, tileY, number, deathOnLevel;
	private Image leftImage, downImage, rightImage, upImage;
	private int stepsTaken = 0;
	private int timesCaught = 0;
	private int direction = 3;
	private int health = 50;
	private int maxHealth = 50;
	private String color, prevColor;
	private Map m;
	private Fog f;
	private boolean isDead = false;
	private boolean caught = false;
	private boolean finish = false;
	private boolean shark = false;
	private Thread sharkTimeThread;
	private boolean caughtRecent = false;
	private Timer playerTimer;
	private long sharkTime;
		
	public Player(Map m, Fog f) {
		this.m = m;
		this.f = f;
	}

	public void setImages(){
		ImageIcon img = new ImageIcon(Constants.FISH_LEFT_IMAGE + color + ".png");
		leftImage = img.getImage();
		img = new ImageIcon(Constants.FISH_DOWN_IMAGE + color + ".png");
		downImage = img.getImage();
		img = new ImageIcon(Constants.FISH_RIGHT_IMAGE + color + ".png");
		rightImage = img.getImage();
		img = new ImageIcon(Constants.FISH_UP_IMAGE + color + ".png");
		upImage = img.getImage();
	}
	
	public void setCaughtImage(){
		ImageIcon img = new ImageIcon(Constants.FISH_CAUGHT_LEFT_IMAGE);
		leftImage = img.getImage();
		img = new ImageIcon(Constants.FISH_CAUGHT_DOWN_IMAGE);
		downImage = img.getImage();
		img = new ImageIcon(Constants.FISH_CAUGHT_RIGHT_IMAGE);
		rightImage = img.getImage();
		img = new ImageIcon(Constants.FISH_CAUGHT_UP_IMAGE);
		upImage = img.getImage();
	}
	
	public void setDeadImage(){
		ImageIcon img = new ImageIcon(Constants.FISH_DEAD_IMAGE);
		leftImage = img.getImage();
		downImage = img.getImage();
		rightImage = img.getImage();
		upImage = img.getImage();
	}
	
	public void setNotSharkImages(){
		color = prevColor;
		ImageIcon img = new ImageIcon(Constants.FISH_LEFT_IMAGE + color + ".png");
		leftImage = img.getImage();
		img = new ImageIcon(Constants.FISH_DOWN_IMAGE + color + ".png");
		downImage = img.getImage();
		img = new ImageIcon(Constants.FISH_RIGHT_IMAGE + color + ".png");
		rightImage = img.getImage();
		img = new ImageIcon(Constants.FISH_UP_IMAGE + color + ".png");
		upImage = img.getImage();
	}
	
	public void moveToStart(int tX, int tY) {
		tileX = tX;
		tileY = tY;
		
		//32x32 character
		x = tX * 32;
		y = tY * 32;
	}
	
	private void move(int dx, int dy, int tx, int ty) {
		if (!Board.run)
		{
			return;
		}
		x += dx;
		y += dy;
		
		tileX += tx;
		tileY += ty;
	}
	
	public Image draw(){
		Image playerImage = null;
		switch(direction){
			case 0:{
				playerImage = getUpImage();
				break;
			}
			case 1:{
				playerImage = getRightImage();
				break;
			}
			case 2:{
				playerImage = getDownImage();
				break;
			}
			case 3:{
				playerImage = getLeftImage();
				break;
			}
		}
		return playerImage;
	}
	
	public void moveUp(){
		if(m.getMap(tileX, tileY - 1) != 'w') {
			f.reFog(tileX, tileY, "U");
			move(0, -32, 0, -1);
			setDirection(0);
			setStepsTaken(getStepsTaken() + 1);
			
			f.iAmHereFog(tileX, tileY);
		}
	}
	
	public void moveDown(){
		if(m.getMap(tileX, tileY + 1) != 'w' && m.getMap(tileX, tileY + 1) != 'b' ) {
			f.reFog(tileX, tileY, "D");

			move(0, 32, 0, 1);
			setDirection(2);
			setStepsTaken(getStepsTaken() + 1);
			f.iAmHereFog(tileX, tileY);
		}
	}
	
	public void moveLeft(){
		if(m.getMap(tileX - 1, tileY) != 'w' && m.getMap(tileX, tileY + 1) != 'b' ) {
			f.reFog(tileX, tileY, "L");
			move(-32, 0, -1, 0);
			setDirection(3);
			setStepsTaken(getStepsTaken() + 1);
			f.iAmHereFog(tileX, tileY);
		}
	}
	
	public void moveRight(){
		if(m.getMap(getTileX() + 1, getTileY()) != 'w' && m.getMap(tileX, tileY + 1) != 'b' ) {
			f.reFog(tileX, tileY, "R");
			move(32, 0, 1, 0);
			setDirection(1);
			setStepsTaken(getStepsTaken() + 1);
			f.iAmHereFog(tileX, tileY);
		}
	}
	
	public void isAtFinish(){
		if(m.getMap(tileX, tileY) == 'f'){
			finish = true;
		}
	}
	
	public void getTimer(int time){
		playerTimer = new Timer();
		if(time == 2000){
			playerTimer.schedule(new NotInvincible(), time);
		}
		if(time == 10000){
			sharkTime = 10;
			shark = true;
			sharkTimeThread = new Thread(new SharkTimerCountDown());
			sharkTimeThread.start();
			playerTimer.schedule(new NotSharkTime(), time);
		}
	}
	
	public String getTimer(){
		return " :" + sharkTime;
	}
	
	public void stopThread(){
		Thread.interrupted();
	}
	
	public void died(){
		health = 0;
		caught = false;
		isDead = true;
		setDeadImage();
		Thread.interrupted();
	}
	
	public void undead(){
		health = 50;
		isDead = false;
		setImages();
	}
	
	public void decreaseHealth(){
		health = health - 5;
	}

	public void setCaughtRecent(boolean caughtRecent) {
		this.caughtRecent = caughtRecent;
		setCaughtImage();
	}
	
	private class NotInvincible extends TimerTask {
		public void run(){
			caught = false;
			caughtRecent = false;
			playerTimer.cancel();
			setImages();
		}	
	}
	
	private class SharkTimerCountDown implements Runnable{

		@Override
		public void run() {
			while(sharkTime > 0){
				try {
					Thread.sleep(1000);
					sharkTime -= 1;
				} catch (InterruptedException e) {
					System.out.println("no thread to sleep");
				}
			}
			if(sharkTime == 0) {
				shark = false;
				setNotSharkImages();
				setImages();
				Thread.currentThread().interrupt();
			}
		}
	}
	
	public boolean getShark(){
		return shark;
	}
	
	public void stopSharkTimer(){
		shark = false;
		sharkTimeThread.interrupt();
	}
	
	private class NotSharkTime extends TimerTask {
		public void run() {
			playerTimer.cancel();
			setNotSharkImages();
			setImages();
			shark = false;
		}
	}

	public void setHealth(int value){
		health = value;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void setMaxHealth(int value){
		maxHealth = value;
	}
	
	public int getMaxHealth(){
		return maxHealth;
	}

	public boolean getCaught(){
		return caught;
	}
	
	public void setCaught(boolean value){
		caught = value;
	}
	
	public boolean getFinish(){
		return finish;
	}
	
	public void setFinished(boolean value){
		finish = value;
	}
	
	private Image getLeftImage() {
		return leftImage;
	}
	
	private Image getDownImage(){
		return downImage;
	}
	
	private Image getRightImage(){
		return rightImage;
	}
	
	private Image getUpImage(){
		return upImage;
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
	
	public void setPrevColor() {
		prevColor = color;
	}

	public int getTimesCaught() {
		return timesCaught;
	}

	public void setTimesCaught(int timesCaught) {
		this.timesCaught = timesCaught;
	}

	public int getStepsTaken() {
		return stepsTaken;
	}

	public void setStepsTaken(int playerStepsTaken) {
		this.stepsTaken = playerStepsTaken;
	}
	
	public boolean isDead() {
		return isDead;
	}

	public int getDeathOnLevel() {
		return deathOnLevel;
	}

	public void setDeathOnLevel(int deathOnLevel) {
		this.deathOnLevel = deathOnLevel;
	}

	public boolean isCaughtRecent() {
		return caughtRecent;
	}
	
	public String getPrevColor() {
		return prevColor;
	}
}
