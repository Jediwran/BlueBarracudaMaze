package maze;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import javax.swing.ImageIcon;

public class Fisherman extends Thread {
	
	private Image fisherman;
	private Map map;
	private int x, y, tileX, tileY;
	private int caughtPlayer = 5;
	private boolean stopRequested = false; 
	private boolean dead = false;

	private ArrayList<Player> players;
	private PathFinding pf;
	private Stack<int[]> path;
	private int count;

	public Fisherman(Map m, ArrayList<Player> players) {
		drawFisherman();
		map = m;
		this.players = players;
		pf = new PathFinding(map, 'w', !stopRequested);
		path = new Stack<>();
		count = 8;
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
		pf.kill();
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

	/**
	 *  Direction is based on Up:0, Right:2, Down::4, Left:6
	 *  Diagonal directions are other numbers between ie. UpRight:1, UpLeft:7
	 */
	public void move(){
		if (!Board.run)
		{
			return;
		}
		int tx = 0;
		int ty = 0;
		boolean fishermanMoved = false;
		while (!fishermanMoved && !dead){
			
			
			
			int direction = -1;
			
			if (count > 4 || path == null || path.size() < 5) {
				direction = findPath();
				count = 2;
			} else {
				direction = getDirection(path.get(count));
				count++;
			}
			
			
			
			
			
//			int direction = findPath();
			
			
			
			if (direction == -1) {
				direction = new Random().nextInt(8);
			}
			
			if (stopRequested) {
				return;
			}

			
//			int direction = new Random().nextInt(8);
			
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
	
	private int findPath() {
		
		Stack<int[]> closestWalls = findClosestWalls(findClosestPlayer(), 8);
		
		if (stopRequested) {
			return -1;
		}
		
//		Stack<int[]> path = new Stack<>();
		
		while (!closestWalls.isEmpty() && !stopRequested) {
			int[] closestWall = closestWalls.pop();
			path = pf.findPath(new int[] {tileX, tileY}, new int[] {closestWall[0], closestWall[1]});
			
			if (path != null) {
				break;
			}
		}
		
		if (path != null) {
			return getDirection(path.get(1));
		}
		
		return -1;
	}
	
	private int[] findClosestPlayer() {
		int closestPlayer = 0;
		double distance2Player = Double.MAX_VALUE;
		for (int i = 0; i < players.size(); i++) {
			if (stopRequested) {
				return null;
			}
			double tempDist = Math.sqrt(Math.pow(tileX - players.get(i).getTileX(),2) + Math.pow(tileY - players.get(i).getTileY(),2));
			if (tempDist < distance2Player) {
				distance2Player = tempDist;
				closestPlayer = i;
			}
		}
		return new int[] {players.get(closestPlayer).getTileX(), players.get(closestPlayer).getTileY()};
	}
	
	private Stack<int[]> findClosestWalls(int[] playerLocation, int n) {
		ArrayList<int[]> closestWalls = new ArrayList<>();
		
		if (stopRequested) {
			return null;
		}
		
		int[] closestWall = new int[2];
		double distance2Wall = Double.MAX_VALUE;
		int count = 0;
		while (closestWalls.size() < n && count < map.getMapSize() * map.getMapSize() + 1) {
	
			closestWall = new int[2];
			distance2Wall = Double.MAX_VALUE;
			for (int i = 0; i < map.getMapSize(); i++) {
				for (int j = 0; j < map.getMapSize(); j++) {
					if (!stopRequested) {
						if (map.getMap(i, j) == 'w') {
							double tempDist = Math.sqrt(Math.pow(i - playerLocation[0],2) + Math.pow(j - playerLocation[1],2));
							if (tempDist < distance2Wall && !inClosestWallList(closestWalls, new int[] {i, j})) {
								distance2Wall = tempDist;
								closestWall[0] = i;
								closestWall[1] = j;
							}
						}
					}
				}
			}
			closestWalls.add(closestWall);
			count++;
		}
		
		Stack<int[]> temp = new Stack<>();
		
		for (int i = closestWalls.size() - 1; i > 0; i--) {
			temp.push(closestWalls.get(i));
		}
		
		return temp;
	}
	private boolean inClosestWallList(ArrayList<int[]> list, int[] toCheck) {
		for (int[] l : list) {
			if (l[0] == toCheck[0] && l[1] == toCheck[1]) {
				return true;
			}
		}
		return false;
	}
	
	private int getDirection(int[] pos) {
		int dir = -1;
		
		//System.out.println(checkMap(new int[] {pos[0],pos[1]}) + "\tgetDirection()");
		if (checkMap(new int[] {pos[0],pos[1]})) {
			//System.out.println(pos[0] + "\t" + pos[1] + "\tgetDirection()");
			//System.out.println(tileX + "\t" + tileY + "\tgetDirection()");
			if (tileX > pos[0] && tileY > pos[1]) {
				dir = 7;
			} else if (tileX > pos[0] && tileY < pos[1]) {
				dir = 5;
			} else if (tileX < pos[0] && tileY > pos[1]) {
				dir = 1;
			} else if (tileX < pos[0] && tileY < pos[1]) {
				dir = 3;
			} else if (tileX > pos[0]) {
				dir = 6;
			} else if (tileX < pos[0]) {
				dir = 2;
			} else if (tileY > pos[1]) {
				dir = 0;
			} else if (tileY < pos[1]) {
				dir = 4;
			}
		}
		
		return dir;
	}
	
	private boolean checkMap(int[] pos2Check) {
		
		if (pos2Check[0] <= map.getMapSize() - 1 && pos2Check[0] >= 0 &&
				pos2Check[1] <= map.getMapSize() - 1 && pos2Check[1] >= 0 &&
				map.getMap(pos2Check[0], pos2Check[1]) == 'w') {
			return true;
		}
		
		return false;
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

			//test that the location generated falls on a Wall "w" space	
		}while(map.getMap(randX, randY) != 'w');
		setStartLocation(randX, randY);
	}

	public int isPlayerNear(Player player){
		if(!player.isGhostMode() && !player.isDead()){
			if(player.getTileX() == tileX + 1 && player.getTileY() == tileY){
				caughtPlayer = player.getNumber();
			}else if(player.getTileX() == tileX - 1 && player.getTileY() == tileY){
				caughtPlayer = player.getNumber();
			}else if(player.getTileX() == tileX && player.getTileY() == tileY + 1){
				caughtPlayer = player.getNumber();
			}else if(player.getTileX() == tileX && player.getTileY() == tileY - 1){
				caughtPlayer = player.getNumber();
			}
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
	
	public void setTileX(int tileX) {
		this.tileX = tileX;
	}

	public void setTileY(int tileY) {
		this.tileY = tileY;
	}

	public boolean getDead(){
		return dead;
	}

	public void isDead(){
		dead = true;
		requestStop();
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
