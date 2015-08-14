package maze;

import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;

public class Fisherman extends Thread {
	
	private Image fisherman;
	private Map map;
	private int x, y, tileX, tileY;
	private int caughtPlayer = 5;
	private boolean stopRequested = false; 
	private boolean dead = false;
	
	public Fisherman(Map m) {
		drawFisherman();
		map = m;
	}
	
	public void run() {
		while(!stopRequested){
			try {
				sleep(1500);
				move();
			} catch (InterruptedException e) {
				System.out.println("Fisherman did not move.");
			}
		}
	}
	
	public void requestStop()
	{
		stopRequested = true;
	}
	
	public void resetStop()
	{
		stopRequested = false;
	}
	
	public void setStartLocation(int dx, int dy) {
		x = dx * 32;
		y = dy * 32;
		
		tileX = dx;
		tileY = dy;
	}
	
	public void move(){
		if (!Board.run)
		{
			return;
		}
		int tx = 0;
		int ty = 0;
		boolean fishermanMoved = false;
		while (!fishermanMoved && !dead){
			int direction = new Random().nextInt(8);
			switch(direction){
				case 0 : {
					if(!(getTileY() - 1 < 0) && map.getMap(getTileX(), getTileY() - 1) == 'w') {
						fishermanMoved = true;
						tx = 0;
						ty = -1;
						break;
					}
					break;
				}
				case 1 : {
					if(!(getTileY() - 1 < 0) && !(getTileX() + 1 > map.getMapSize() - 1) && map.getMap(getTileX() + 1, getTileY() - 1) == 'w') {
						fishermanMoved = true;
						tx = 1;
						ty = -1;
						break;
					}
					break;
				}
				case 2 : {
						if(!(getTileX() + 1 > map.getMapSize() - 1) && map.getMap(getTileX() + 1, getTileY()) == 'w') {
							tx = 1;
							ty = 0;
							fishermanMoved = true;
							break;
						}
					break;
				}
				case 3 : {
					if(!(getTileY() + 1 > map.getMapSize() - 1) && !(getTileX() + 1 > map.getMapSize() - 2) && map.getMap(getTileX() + 1, getTileY() + 1) == 'w') {
						fishermanMoved = true;
						tx = 1;
						ty = 1;
						break;
					}
					break;
				}
				case 4 : {
						if(!(getTileY() + 1 > map.getMapSize() - 1) && map.getMap(getTileX(), getTileY() + 1) == 'w') {
							tx = 0;
							ty = 1;
							fishermanMoved = true;
							break;
					}
					break;
				}
				case 5 : {
					if((!(getTileX() - 1 < 0)) && (!(getTileY() + 1 > map.getMapSize() - 1)) && map.getMap(getTileX() - 1, getTileY() + 1) == 'w') {
						fishermanMoved = true;
						tx = -1;
						ty = 1;
						break;
					}
					break;
				}
				case 6 : {
						if(!(getTileX() - 1 < 0) && map.getMap(getTileX() - 1, getTileY()) == 'w') {
							tx = -1;
							ty = 0;
							fishermanMoved = true;
							break;
					}
					break;
				}
				case 7 : {
					if(!(getTileY() - 1 < 0) && !(getTileX() - 1 < 0) && map.getMap(getTileX() - 1, getTileY() - 1) == 'w') {
						fishermanMoved = true;
						tx = -1;
						ty = -1;
						break;
					}
					break;
				}
			}
		}
		x += tx * 32;
		y += ty * 32;
		
		tileX += tx;
		tileY += ty;
	}
	
	public void randomStart() {
		Random rand = new Random();
		int randX;
		int randY;
		do{
			do{
			randX = rand.nextInt(13);
			}while(Math.abs(map.getStartX()-randX) < 3);
			
			do{
			randY = rand.nextInt(13);
			}while(Math.abs(map.getStartY()-randY) < 3);
			
		//test that the location generated falls on a Wall "w" space	
		}while(map.getMap(randX, randY) != 'w');
			setStartLocation(randX, randY);
	}
	
	public int isPlayerNear(Player player){
		if(player.getTileX() == tileX + 1 && player.getTileY() == tileY){
			caughtPlayer = player.getNumber();
		}else if(player.getTileX() == tileX - 1 && player.getTileY() == tileY){
			caughtPlayer = player.getNumber();
		}else if(player.getTileX() == tileX && player.getTileY() == tileY + 1){
			caughtPlayer = player.getNumber();
		}else if(player.getTileX() == tileX && player.getTileY() == tileY - 1){
			caughtPlayer = player.getNumber();
		}
		return caughtPlayer;
	}
	
	public int getCaughtPlayer(){
		return caughtPlayer;
	}
	
	public void resetCaughtPlayer(){
		caughtPlayer = 5;
	}
	
	public Image getImage() {
		return fisherman;
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
	
	public boolean getDead(){
		return dead;
	}
	
	public void isDead(){
		dead = true;
	}
	
	public void drawFisherman(){
		ImageIcon img = new ImageIcon(Constants.FISHERMAN_IMAGE);
		fisherman = img.getImage();
	}
	
	public void setImage(){
		ImageIcon img = new ImageIcon(Constants.FISHERMAN_DEAD_IMAGE);
		fisherman = img.getImage();
		isDead();
		
	}
}