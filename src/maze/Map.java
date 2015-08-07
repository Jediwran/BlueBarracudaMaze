package maze;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.ImageIcon;

public class Map {
	
	private Scanner m;
	private int arraySize;
	private int startX = 0;
	private int startY = 0;
	private String Map[];
	private Image ground, wall, start, finish, block;
	private String mapName;
	
	
	private char[][] map;
	
	public Map() {
		//arraySize = 14;
		ImageIcon img = new ImageIcon("src/resources/water.png");
		ground = img.getImage();
		img = new ImageIcon("src/resources/wall.png");
		wall = img.getImage();
		img = new ImageIcon("src/resources/start.png");
		start = img.getImage();
		img = new ImageIcon("src/resources/finish.png");
		finish = img.getImage();
		img = new ImageIcon("src/resource/block.png");
		block = img.getImage();
	}
	
	public void newMap(int size) {
		map = Generate.newMaze(size);
		
		setStartLocation();
	}
	
	public void setupMap(){
		openFile();
		readFile();
		closeFile();
		
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
		Map = new String[arraySize];
	}
	
	public int getMapSize() {
		return Map.length;
	}
	
	public char getMap(int x, int y) {
		char spot = map[y][x];
		return spot;
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
	
	public int getStartY() {
		return startY;
	}
	
	public void openFile() {
		try{
			m = new Scanner(new File("src/resources/maps/" + mapName));
		} catch(Exception e){
			System.out.println("Error loading map");
		}
	}
	
	public void readFile() {
		while(m.hasNext()) {
			for(int i = 0; i < arraySize; i++) {
				Map[i] = m.next();
			}
		}
	}
	
	public void closeFile() {
		m.close();
	}
	
	public String getMapName(){
		return mapName;
	}
	
	public StringBuilder printMap(){
		StringBuilder allText = new StringBuilder();
		allText.append(this.getMapName()
				+ "\n==============");
		
		for (int i = 0; i < arraySize; i++){
			allText.append(Map[i] + "\n");
		}
		
		return allText;
	}
	public void setMapName(int mapNum){
		mapName = "Map"+ mapNum +".txt";
	}
	
	/**
	 * For when you need to open a file not in the map's folder.
	 */
	public void openCustomFile(String path){
		try {
			m = new Scanner (new File(path));
		} catch (Exception e){
			System.out.println("Failed to load file from given path: " + path);
		}
	}
}