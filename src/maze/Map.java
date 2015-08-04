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
	private Image ground, wall, start, finish;
	Random r = new Random();
	private String mapName = "Map"+ (r.nextInt(5-1)+1) +".txt";
	
	
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
	
	public void setSize(int size){
		arraySize = size;
		Map = new String[arraySize];
	}
	
	public int getMapSize() {
		return Map.length;
	}
	
	public String getMap(int x, int y) {
		String index = Map[y].substring(x, x + 1);
		return index;
	}
	
	public void setStartLocation() {
		for(int y = 0; y < arraySize; y++) {
			for(int x = 0; x < arraySize; x++) {
				if(getMap(x, y).equals("s")){
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
			m = new Scanner(new File("src/resources/" + mapName));
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
}
