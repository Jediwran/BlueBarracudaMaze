package maze;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.ImageIcon;

public class Map {
	
	private Scanner m;
	private int arraySize = 14;
	private String Map[] = new String[arraySize];
	private Image grass, wall, start, finish;
	private String mapName = "Map.txt";
	
	
	public Map() {
		ImageIcon img = new ImageIcon("src/resources/grass.png");
		grass = img.getImage();
		img = new ImageIcon("src/resources/wall.png");
		wall = img.getImage();
		img = new ImageIcon("src/resources/start.png");
		start = img.getImage();
		img = new ImageIcon("src/resources/finish.png");
		finish = img.getImage();
		
		openFile();
		readFile();
		closeFile();
	}
	
	public Image getGrass() {
		return grass;
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
	
	public String getMap(int x, int y) {
		String index = Map[y].substring(x, x + 1);
		return index;
	}
	
	public int getStartX() {
		int startX = 0;
		for(int y = 0; y < arraySize; y++) {
			for(int x = 0; x < arraySize; x++) {
				if(getMap(x, y).equals("s")){
					startX = x;
					return startX;
				}
			}
		}
		return startX;
	}
	
	public int getStartY() {
		int startY = 0;
		for(int y = 0; y < arraySize; y++) {
			for(int x = 0; x < arraySize; x++) {
				if(getMap(x, y).equals("s")){
					startY = y;
					return startY;
				}
			}
		}
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
