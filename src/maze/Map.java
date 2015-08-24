package maze;

import java.awt.*;
import java.io.Serializable;

import javax.swing.ImageIcon;

public class Map implements Serializable {
	
	private int arraySize, startX = 0, startY = 0;
	private transient Image ground, wall, start, finish, block;
	private String mapName;
	private char[][] map;
	
	public Map() {
		ImageIcon img = new ImageIcon(Constants.WATER_IMAGE);
		ground = img.getImage();
		img = new ImageIcon(Constants.WALL_IMAGE);
		wall = img.getImage();
		img = new ImageIcon(Constants.START_IMAGE);
		start = img.getImage();
		img = new ImageIcon(Constants.FINISH_IMAGE);
		finish = img.getImage();
		img = new ImageIcon(Constants.BLOCK_IMAGE);
		block = img.getImage();
	}
	
	public void newMap(int size) {
		int numOfHoles = (int) Math.pow(size / 4, 2);
		
		map = Generate.newMaze(size, numOfHoles);
		
		setStartLocation();
	}
	
	public void setupMap(){		
		setStartLocation();
	}
	
	public Image getGround() {
		return ground;
	}
	
	public Image getWall() {
		return wall;
	}
	
	public Image getFinish() {
		return finish;
	}
	
	public Image getStart() {
		return start;
	}
	
	public Image getBlock() {
		return block;
	}
	
	public void setSize(int size){
		arraySize = size;
		map = new char[arraySize][arraySize];
	}
	
	public int getMapSize() {
		return map.length;
	}
	
	public char getMap(int x, int y) {
		char spot = map[y][x];
		return spot;
	}
	
	public void setMap(int x, int y, char c)
	{
		map[y][x] = c;
	}
	
	public void setStartLocation() {
		for(int y = 0; y < arraySize; y++) {
			for(int x = 0; x < arraySize; x++) {
				if(getMap(x, y) == 's'){
					startX = x;
					startY = y;
				}
			}
		}
	}
	
	public int getStartX() {
		return startX;
	}
	
	public void setStartX(int x)
	{
		startX = x;
	}
	
	public int getStartY() {
		return startY;
	}
	
	public void setStartY(int y)
	{
		startY = y;
	}
	
	public String getMapName(){
		return mapName;
	}
}
