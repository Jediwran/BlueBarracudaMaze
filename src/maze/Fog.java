package maze;

public class Fog {
	
	private int fogMapA = 14;
	private int fogMapB = 14;
	private int[][] fogMap = new int[fogMapA][fogMapB];
	
	public Fog(){
	}
		
	public void reFog(int x, int y) {
		for(int i = 0; i < fogMapA; i++) {
			for (int j = 0; j < fogMapB; j++){
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
		for(int i = 0; i < fogMapA; i++) {
			for (int j = 0; j < fogMapB; j++){
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
}
