package com.maze;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.ImageIcon;

public class Map {
	
	private Scanner m;
	private String Map[] = new String[14];
	private Image grass, wall, start, finish;
	
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
		for(int y = 0; y < 14; y++) {
			for(int x = 0; x < 14; x++) {
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
		for(int y = 0; y < 14; y++) {
			for(int x = 0; x < 14; x++) {
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
			m = new Scanner(new File("src/resources/Map.txt"));
		} catch(Exception e){
			System.out.println("Error loading map");
		}
	}
	
	public void readFile() {
		while(m.hasNext()) {
			for(int i = 0; i < 14; i++) {
				Map[i] = m.next();
			}
		}
	}
	
	public void closeFile() {
		m.close();
	}
}
