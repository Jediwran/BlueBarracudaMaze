package maze;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Fog {
	
	private Image fog, fogOpaque;
	private int fogMapSize = 14;
	private int[][] fogMap = new int[fogMapSize][fogMapSize];
	
	public Fog(){
		ImageIcon img = new ImageIcon("src/resources/fog.png");
		fog = img.getImage();
		img = new ImageIcon("src/resources/fog-opaque.png");
		fogOpaque = img.getImage();
	}
		
	public void reFog(int x, int y) {
		for(int i = 0; i < fogMapSize; i++) {
			for (int j = 0; j < fogMapSize; j++){
				if(i == x && j == y) {
					fogMap[i][j] = 0;
				} else if(i == x && j == (y - 1)){
					fogMap[i][j] = 0;
				} else if(i == x && j == (y + 1)){
					fogMap[i][j] = 0;
				} else if(i == x + 1 && j == y){
					fogMap[i][j] = 0;
				} else if(i == x - 1 && j == y){
					fogMap[i][j] = 0;
				}
				else fogMap[i][j] = 1;
			}
		}
	}
	
	public void createFog(int x, int y) {
		for(int i = 0; i < fogMapSize; i++) {
			for (int j = 0; j < fogMapSize; j++){
				if(i == x && j == y) {
					fogMap[i][j] = 0;
				} else if(i == x && j == (y - 1)){
					fogMap[i][j] = 0;
				} else if(i == x && j == (y + 1)){
					fogMap[i][j] = 0;
				} else if(i == x + 1 && j == y){
					fogMap[i][j] = 0;
				} else if(i == x - 1 && j == y){
					fogMap[i][j] = 0;
				}
				else fogMap[i][j] = 1;
			}
		}
	}
	
	public int[][] getFogMap() {
		return fogMap;
	}
		
	public Image getFog() {
		return fog;
	}
	
	public Image getFogOpaque() {
		return fogOpaque;
	}
}
