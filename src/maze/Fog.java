package maze;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Fog {
	
	private Image fog, fogOpaque;
	private int fogMapSize = 14;
	private int[][] fogMap = new int[fogMapSize][fogMapSize];
	private String left;
	
	public Fog(){
		ImageIcon img = new ImageIcon("src/resources/fog.png");
		fog = img.getImage();
		img = new ImageIcon("src/resources/fog-opaque.png");
		fogOpaque = img.getImage();
	}
		
	public void reFog(int x, int y,String direction) {
		//Sets the current occupied space to visible
		fogMap[x][y] = 0;
		fogMap[x-1][y] = 0;
		fogMap[x-1][y+1] = 0;
		fogMap[x-1][y-1] = 0;
		fogMap[x+1][y] = 0;
		fogMap[x+1][y+1] = 0;
		fogMap[x+1][y-1] = 0;
		fogMap[x][y+1] = 0;
		fogMap[x][y-1] = 0;
		
		switch(direction){
			case "U":{
				fogMap[x-1][y+2] = 2;
				fogMap[x][y+2] = 2;
				fogMap[x+1][y+2] = 2;
				break;
			}
			case "D":{
				fogMap[x-1][y-2] = 2;
				fogMap[x][y-2] = 2;
				fogMap[x+1][y-2] = 2;
				break;
			}
			case "L":{
				fogMap[x+2][y-1] = 2;
				fogMap[x+2][y] = 2;
				fogMap[x+2][y+1] = 2;
				break;
			}
			case "R":{
				fogMap[x-2][y-1] = 2;
				fogMap[x-2][y] = 2;
				fogMap[x-2][y+1] = 2;
				break;
			}
		}
		
		//sets the 3 spaces from the direction of travel as opaque
		/*if (direction == "U"){
			fogMap[x-1][y-1] = 2;
			fogMap[x][y-2] = 2;
			fogMap[x+1][y-1] = 2;
		}else if (direction == "D"){
			fogMap[x-1][y+1] = 2;
			fogMap[x][y+2] = 2;
			fogMap[x+1][y+1] = 2;
		}else if (direction == "L"){
			fogMap[x-2][y-1] = 2;
			fogMap[x-2][y+2] = 2;
			fogMap[x-2][y+1] = 2;
		}else if (direction == "R"){
			fogMap[x-2][y-1] = 2;
			fogMap[x-2][y+2] = 2;
			fogMap[x-2][y+1] = 2;
		}*/
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
				} else if(i == (x - 1) && j == (y - 1)){
					fogMap[i][j] = 0;
				} else if(i == (x - 1) && j == (y + 1)){
					fogMap[i][j] = 0;
				} else if(i == (x + 1) && j == (y - 1)){
					fogMap[i][j] = 0;
				} else if(i == (x + 1) && j == (y + 1)){
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
