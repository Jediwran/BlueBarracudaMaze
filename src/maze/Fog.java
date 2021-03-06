package maze;

import java.awt.Graphics;
import java.awt.Image;
import java.io.Serializable;

import javax.swing.ImageIcon;

public class Fog implements Serializable{
	
	private static final long serialVersionUID = 7807654125998313877L;
	private transient Image fog, fogOpaque;
	private int fogMapSize = 14, fish_sight = Settings.getSettings().getSight();
	private int[][] fogMap;
	
	public Fog(int size){
		fogMapSize = size;
		fogMap = new int[fogMapSize][fogMapSize];
		ImageIcon img = new ImageIcon(Constants.FOG_IMAGE);
		fog = img.getImage();
		img = new ImageIcon(Constants.FOG_OPAQUE_IMAGE);
		fogOpaque = img.getImage();
	}
	
	public void setFogMapSize(int size){
		fogMapSize = size;
		fogMap = new int[fogMapSize][fogMapSize];
	}
	
	public void iAmHereFog(int x, int y){
		fog(x,y,0);
	}
	
	private void fog(int x, int y, int set_type)
	{
		for(int i = x - fish_sight; i <= x + fish_sight;i++)
		{
			for(int j = y - fish_sight;j <= y + fish_sight;j++)
			{
				if(i >= 0 && i < fogMapSize)
				{
					if(j >= 0 && j < fogMapSize)
					{
						if(Math.abs(i -x) +  Math.abs(j -y) <= fish_sight )
						{
							fogMap[i][j] = set_type;			
						}								
					}
				}
			}
		}
	}
	
	
	public void reFog(int x, int y,String direction)
	{
		fog(x,y,2);
	}
	
	public void createFog(int x, int y) {
		for(int i = 0; i < fogMapSize; i++) {
			for (int j = 0; j < fogMapSize; j++){
				fogMap[i][j] = 1;
			}
		}
		fog(x,y,0);
	}
	
	public void draw(Graphics g) {
		for(int i = 0; i < fogMap.length; i++){
			for(int j = 0; j < fogMap.length; j++){
				if(fogMap[i][j] == 1){
					g.drawImage(getFog(),i * 32, j * 32, null);
				}
				else if(fogMap[i][j] == 2){
					g.drawImage(getFogOpaque(), i * 32, j * 32, null);
				}
			}
		}
	}
	
	public int getFishSight() {
		return fish_sight;
	}

	public void setFishSight(int fish_sight) {
		this.fish_sight = fish_sight;
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
