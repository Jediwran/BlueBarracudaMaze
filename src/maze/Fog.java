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
	
	public void iAmHereFog(int x, int y){
		fogMap[x][y] = 0;
		fogMap[x-1][y] = 0;
		fogMap[x-1][y+1] = 0;
		fogMap[x-1][y-1] = 0;
		fogMap[x+1][y] = 0;
		fogMap[x+1][y+1] = 0;
		fogMap[x+1][y-1] = 0;
		fogMap[x][y+1] = 0;
		fogMap[x][y-1] = 0;
		if(x - 2 >= 0 && y >= 0){
			fogMap[x-2][y] = 0;
		}
		if(x + 2 <= fogMapSize - 1 && y >= 0){
			fogMap[x+2][y] = 0;
		}
		if(y + 2 <= fogMapSize - 1 && x >= 0){
			fogMap[x][y+2] = 0;
		}
		if(y - 2 >= 0 && x >= 0){
			fogMap[x][y-2] = 0;
		}
	}
	
	
	public void reFog(int x, int y,String direction) {
		//Sets the current occupied space to visible
		
		switch(direction){
			case "U":{
				//fogMap[x-1][y+2] = 2;
				//fogMap[x][y+2] = 2;
				//fogMap[x+1][y+2] = 2;
				if(y+3 < fogMapSize){
					fogMap[x][y+3] = 2;
				}
				if(x-2 >= 0){
					fogMap[x-2][y+1] = 2;
				}
				if(y+2 < fogMapSize){
					fogMap[x-1][y+2] = 2;
					fogMap[x+1][y+2] = 2;
				}
				if(x+2 < fogMapSize){
					fogMap[x+2][y+1] = 2;
				}
				iAmHereFog(x,y);
				break;
			}
			case "D":{
				//fogMap[x-1][y-2] = 2;
				//fogMap[x][y-2] = 2;
				//fogMap[x+1][y-2] = 2;
				if(x-2 >= 0){
					fogMap[x-2][y-1] = 2;
				}
				if(y-2 >= 0){
					fogMap[x-1][y-2] = 2;
					fogMap[x+1][y-2] = 2;
				}
				if(y-3 >= 0){
					fogMap[x][y-3] = 2;
				}
				if(x+2 < fogMapSize){
					fogMap[x+2][y-1] = 2;
				}
				iAmHereFog(x,y);
				break;
			}
			case "L":{
				//fogMap[x+2][y-1] = 2;
				//fogMap[x+2][y] = 2;
				//fogMap[x+2][y+1] = 2;
				if(y-2 >= 0){
					fogMap[x+1][y-2] = 2;
				}
				if(x+2 < fogMapSize){
					fogMap[x+2][y-1] = 2;
					fogMap[x+2][y+1] = 2;
				}
				if(x+3 < fogMapSize){
					fogMap[x+3][y] = 2;
				}
				if(y+2 < fogMapSize){
					fogMap[x+1][y+2] = 2;
				}
				iAmHereFog(x,y);
				break;
			}
			case "R":{
				//fogMap[x-2][y-1] = 2;
				//fogMap[x-2][y] = 2;
				//fogMap[x-2][y+1] = 2;
				if(y+2 < fogMapSize){
					fogMap[x-1][y+2] = 2;
				}
				if(x-2 >= 0){
					fogMap[x-2][y+1] = 2;
					fogMap[x-2][y-1] = 2;
				}
				if(x-3 >= 0){
					fogMap[x-3][y] = 2;
				}
				if(y-2 >= 0){
					fogMap[x-1][y-2] = 2;
				}
				iAmHereFog(x,y);
				break;
			}
			//reset fish to starting position when caught
			case "C":{
				fogMap[x][y] = 2;
				fogMap[x-1][y] = 2;
				fogMap[x-1][y+1] = 2;
				fogMap[x-1][y-1] = 2;
				fogMap[x+1][y] = 2;
				fogMap[x+1][y+1] = 2;
				fogMap[x+1][y-1] = 2;
				fogMap[x][y+1] = 2;
				fogMap[x][y-1] = 2;
				if(y+2 < fogMapSize){
					fogMap[x][y+2] = 2;
				}
				if(y-2 >= 0){
					fogMap[x][y-2] = 2;
				}
				if(x-2 >= 0){
					fogMap[x-2][y] = 2;
				}
				if(x+2 < fogMapSize){
					fogMap[x+2][y] = 2;
				}
			}
			default:{
				//code for bad argument here
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
		if(x - 2 >= 0 && y >= 0){
			fogMap[x-2][y] = 0;
		}
		if(x + 2 <= fogMapSize - 1 && y >= 0){
			fogMap[x+2][y] = 0;
		}
		if(y + 2 <= fogMapSize - 1 && x >= 0){
			fogMap[x][y+2] = 0;
		}
		if(y - 2 >= 0 && x >= 0){
			fogMap[x][y-2] = 0;
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
