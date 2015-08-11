
package maze;

import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;

public class Fisherman extends Thread {
	
	private Image fisherman;
	private Map map;
	private int x, y, tileX, tileY;
	private String fishermanFile = "src/resources/player.png";
	private boolean stopRequested = false; 
	public Fisherman(Map m) {
		ImageIcon img = new ImageIcon(fishermanFile);
		fisherman = img.getImage();
		map = m;
	}
	
	public void run() {
		while(!stopRequested){
			try {
				sleep(1500);
				//System.out.println("move fisherman");
				move();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Fisherman stopping");
	}
	public void requestStop()
	{
		stopRequested = true;
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
	
	public void setStartLocation(int dx, int dy) {
		x = dx * 32;
		y = dy * 32;
		
		tileX = dx;
		tileY = dy;
	}
	
	//public void move(int dx, int dy, int tx, int ty){
	public void move(){
		int tx = 0;
		int ty = 0;
		boolean fishermanMoved = false;
		while (!fishermanMoved){
			int direction = new Random().nextInt(8);
			switch(direction){
				case 0 : {
					if(!(getFishermanTileY() - 1 < 0) && map.getMap(getFishermanTileX(), getFishermanTileY() - 1) == 'w') {
						fishermanMoved = true;
						//fm.move(0, -32, 0, -1);
						tx = 0;
						ty = -1;
						break;
					}
					break;
				}
				case 1 : {
					if(!(getFishermanTileY() - 1 < 0) && !(getFishermanTileX() + 1 > map.getMapSize() - 1) && map.getMap(getFishermanTileX() + 1, getFishermanTileY() - 1) == 'w') {
						fishermanMoved = true;
						//fm.move(0, -32, 0, -1);
						tx = 1;
						ty = -1;
						break;
					}
					break;
				}
				case 2 : {
						if(!(getFishermanTileX() + 1 > map.getMapSize() - 1) && map.getMap(getFishermanTileX() + 1, getFishermanTileY()) == 'w') {
							//fm.move(32, 0, 1, 0);
							tx = 1;
							ty = 0;
							fishermanMoved = true;
							break;
						}
					break;
				}
				case 3 : {
					if(!(getFishermanTileY() + 1 > map.getMapSize()) && !(getFishermanTileX() + 1 > map.getMapSize() - 1) && map.getMap(getFishermanTileX() + 1, getFishermanTileY() + 1) == 'w') {
						fishermanMoved = true;
						//fm.move(0, -32, 0, -1);
						tx = 1;
						ty = 1;
						break;
					}
					break;
				}
				case 4 : {
						if(!(getFishermanTileY() + 1 > map.getMapSize() - 1) && map.getMap(getFishermanTileX(), getFishermanTileY() + 1) == 'w') {
							//fm.move(0, 32, 0, 1);
							tx = 0;
							ty = 1;
							fishermanMoved = true;
							break;
					}
					break;
				}
				case 5 : {
					if(!(getFishermanTileX() - 1 < 0) && !(getFishermanTileY() + 1 > map.getMapSize() - 1) && map.getMap(getFishermanTileX() - 1, getFishermanTileY() + 1) == 'w') {
						fishermanMoved = true;
						//fm.move(0, -32, 0, -1);
						tx = -1;
						ty = 1;
						break;
					}
					break;
				}
				case 6 : {
						if(!(getFishermanTileX() - 1 < 0) && map.getMap(getFishermanTileX() - 1, getFishermanTileY()) == 'w') {
							//fm.move(-32, 0, -1, 0);
							tx = -1;
							ty = 0;
							fishermanMoved = true;
							break;
					}
					break;
				}
				case 7 : {
					if(!(getFishermanTileY() - 1 < 0) && !(getFishermanTileX() - 1 < 0) && map.getMap(getFishermanTileX() - 1, getFishermanTileY() - 1) == 'w') {
						fishermanMoved = true;
						//fm.move(0, -32, 0, -1);
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
	
	public void isNear(Player p) {
		//moveFisherman();
		//if(isFishCaught()){
			//fishermanCaughtFish(p);
		//}
	}
}
