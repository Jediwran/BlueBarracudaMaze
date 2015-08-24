package maze;

import java.awt.Image;
import java.util.Random;
import javax.swing.ImageIcon;

public class Barrel extends Thread{
	
	private Image barrel;
	private Map map;
	private int x, y, tileX, tileY;
	private boolean stopRequested = false, sharkTime = false;
	
	public Barrel(Map m) {
		ImageIcon img = new ImageIcon(Constants.BARREL_IMAGE);
		barrel = img.getImage();
		map = m;
	}
	
	public void run() {
		stopRequested = false;
		while(!stopRequested){
			try {
				sleep(1500);
				move();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//System.out.println("Barrel stopping");
	}
	
	public void requestStop()
	{
		stopRequested = true;
	}
	
	public void resetStop()
	{
		stopRequested = false;
	}
	
	public Image getBarrel() {
		return barrel;
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
		boolean barrelMoved = false;
		while (!barrelMoved && !stopRequested){
			int direction = new Random().nextInt(8);
			switch(direction){
				case 0 : {
					if(!(getTileY() - 1 < 0) && map.getMap(getTileX(), getTileY() - 1) == 'g') {
						barrelMoved = true;
						tx = 0;
						ty = -1;
						break;
					}
					break;
				}

				case 2 : {
						if(!(getTileX() + 1 > map.getMapSize() - 1) && map.getMap(getTileX() + 1, getTileY()) == 'g') {
							tx = 1;
							ty = 0;
							barrelMoved = true;
							break;
						}
					break;
				}

				case 4 : {
						if(!(getTileY() + 1 > map.getMapSize() - 1) && map.getMap(getTileX(), getTileY() + 1) == 'g') {
							tx = 0;
							ty = 1;
							barrelMoved = true;
							break;
					}
					break;
				}

				case 6 : {
						if(!(getTileX() - 1 < 0) && map.getMap(getTileX() - 1, getTileY()) == 'g') {
							tx = -1;
							ty = 0;
							barrelMoved = true;
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
			randX = rand.nextInt(map.getMapSize());
			}while(Math.abs(map.getStartX()-randX) < 3);
			
			do{
			randY = rand.nextInt(13);
			}while(Math.abs(map.getStartY()-randY) < 3);
			
		//test that the location generated falls on a Ground "g" space	
		}while(map.getMap(randX, randY) != 'g');
			setStartLocation(randX, randY);
	}
		
	public Image getImage() {
		return barrel;
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
	
	public void setTileX(int tileX) {
		this.tileX = tileX;
	}

	public void setTileY(int tileY) {
		this.tileY = tileY;
	}

	public void isPlayerNear(Player player){
		if(player.getTileX() == tileX && player.getTileY() == tileY){
			if(!player.isDead() && !player.isGhostMode()){
				sharkTime = true;
			}
		}
	}
	
	public boolean getSharkTime(){
		return sharkTime;
	}
	
	public void resetsharkTime(){
		sharkTime = false;
	}

	public boolean isStopRequested() {
		return stopRequested;
	}

	public void hide() {
		x = 0;
		y = 0;
		tileX = 0;
		tileY = 0;		
	}
}
