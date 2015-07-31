package maze;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;

public class Board extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private Timer timer;
	//private LocalTime sTime, eTime;
	private long sTime, eTime;
	private Map m;
	private Player p;
	private Fisherman[] fisherMen;
	private Fog f;
	private int stepCount, caughtCounter,direction;
	int level = 0;
	private boolean fogEnabled = true;
	private boolean caught = false;
	
	public Board() {
		m = new Map();
		p = new Player();
		f = new Fog();
		addKeyListener(new Al());
		setFocusable(true);
		startLevel();
	}
	
	public void startLevel(){
		stepCount = 0;
		caughtCounter = 0;
		level += 1;
		fisherMen = new Fisherman[level];
		for(int i = 0; i < level; i++){
			fisherMen[i] = new Fisherman();
		}
		direction = 3;
		p.setPlayerStart(m.getStartX(), m.getStartY());
		randomStartFisherman(fisherMen[level - 1]);
		
		f.createFog(p.getTileX(), p.getTileY());
		
		//sTime = LocalTime.now();
		sTime = System.currentTimeMillis();
		timer = new Timer(25, this);
		timer.start();
	}
	
	public Image drawPlayer(){
		Image temp = null;
		switch(direction){
			case 0:{
				temp = p.getPlayerUp();
				break;
			}
			case 1:{
				temp = p.getPlayerRight();
				break;
			}
			case 2:{
				temp = p.getPlayerDown();
				break;
			}
			case 3:{
				temp = p.getPlayer();
				break;
			}
		}
		return temp;
	}
	
	public void actionPerformed(ActionEvent e) {
		repaint();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		for(int y = 0; y < 14; y++) {
			for(int x = 0; x < 14; x++) {
				if(m.getMap(x, y).equals("g")){
					g.drawImage(m.getGround(), x * 32, y *32, null);
				}
				if(m.getMap(x, y).equals("w")){
					g.drawImage(m.getWall(), x * 32, y *32, null);
				}
				if(m.getMap(x, y).equals("f")){
					g.drawImage(m.getFinish(), x * 32, y * 32, null);
				}
				if(m.getMap(x, y).equals("s")){
					g.drawImage(m.getStart(), x * 32, y * 32, null);
				}
			}
		}
		g.drawImage(drawPlayer(), p.getX(), p.getY(), null);
		int[][] fog = f.getFogMap();
		if(fogEnabled){
			for(int i = 0; i < fog.length; i++){
				for(int j = 0; j < fog.length; j++){
					if(fog[i][j] == 1){
						g.drawImage(f.getFog(),i * 32, j * 32, null);
					}
					else if(fog[i][j] == 2){
						g.drawImage(f.getFogOpaque(), i * 32, j * 32, null);
					}
				}
			}
		} else {
			for(int i = 0; i < fog.length; i++){
				for(int j = 0; j < fog.length; j++){
					fog[i][j] = 0;
				}
			}
		}
		for (Fisherman fisherman : fisherMen)
		if(fog[fisherman.getFishermanTileX()][fisherman.getFishermanTileY()] == 0){
			g.drawImage(fisherman.getFisherman(), fisherman.getFishermanX(), fisherman.getFishermanY(), null);
		}
	}
	
	public void randomStartFisherman(Fisherman f) {
		Random rand = new Random();
		int randX = rand.nextInt(13);
		int randY = rand.nextInt(13);
		while(!m.getMap(randX, randY).equals("w")){
			randX = rand.nextInt(13);
			randY = rand.nextInt(13);
		}
		if(m.getMap(randX, randY).equals("w")) {
			f.setFishermanStartLocation(randX, randY);
		}
	}
	
	/*public void moveFishermanCloser() {
		boolean fishermanMoved = false;
		int fishermanX = fisherMan.getFishermanTileX();
		int fishermanY = fisherMan.getFishermanTileY();
		while (!fishermanMoved){
			int direction = new Random().nextInt(4);
			switch(direction){
				case 0 : {				
					if(!(fishermanY - 1 < 0) && m.getMap(fishermanX, fishermanY - 1).equals("w")) {
						//fisherMan.setFishermanLocation(fishermanX, fishermanY - 1);
						fisherMan.move(0, -32, 0, -1);
						fishermanMoved = true;
					} else{
						moveFisherman();
					}
					break;
				}
				case 1 : {
					if(!(fishermanX + 1 > m.getMapSize() - 1) && m.getMap(fishermanX + 1, fishermanY).equals("w")) {
						//fisherMan.setFishermanLocation(fishermanX + 1, fishermanY);
						fisherMan.move(32, 0, 1, 0);
						fishermanMoved = true;
					} else{
						moveFisherman();
					}
					break;
				}
				case 2 : {
					if(!(fishermanY + 1 > m.getMapSize() - 1) && m.getMap(fishermanX, fishermanY + 1).equals("w")) {
						//fisherMan.setFishermanLocation(fishermanX, fishermanY + 1);
						fisherMan.move(0, 32, 0, 1);
						fishermanMoved = true;
					} else{
						moveFisherman();
					}
					break;
				}
				case 3 : {
					if(!(fishermanX - 1 < 0) && m.getMap(fishermanX - 1, fishermanY).equals("w")) {
						//fisherMan.setFishermanLocation(fishermanX - 1, fishermanY);
						fisherMan.move(-32, 0, -1, 0);
						fishermanMoved = true;
					} else{
						moveFisherman();
					}
					break;
				}
			}
		}
	}*/
	
	public void moveFisherman() {
		for(int i = 0; i < fisherMen.length; i++){
			boolean fishermanMoved = false;
			while (!fishermanMoved){
				int direction = new Random().nextInt(4);
				switch(direction){
					case 0 : {
							if(!(fisherMen[i].getFishermanTileY() - 1 < 0) && m.getMap(fisherMen[i].getFishermanTileX(), fisherMen[i].getFishermanTileY() - 1).equals("w")) {
								//fisherMan.setFishermanLocation(fishermanX, fishermanY - 1);
								fishermanMoved = true;
								fisherMen[i].move(0, -32, 0, -1);
								break;
							}
						break;
					}
					case 1 : {
							if(!(fisherMen[i].getFishermanTileX() + 1 > m.getMapSize() - 1) && m.getMap(fisherMen[i].getFishermanTileX() + 1, fisherMen[i].getFishermanTileY()).equals("w")) {
								//fisherMan.setFishermanLocation(fishermanX + 1, fishermanY);
								fisherMen[i].move(32, 0, 1, 0);
								fishermanMoved = true;
								break;
							}
						break;
					}
					case 2 : {
							if(!(fisherMen[i].getFishermanTileY() + 1 > m.getMapSize() - 1) && m.getMap(fisherMen[i].getFishermanTileX(), fisherMen[i].getFishermanTileY() + 1).equals("w")) {
								//fisherMan.setFishermanLocation(fishermanX, fishermanY + 1);
								fisherMen[i].move(0, 32, 0, 1);
								fishermanMoved = true;
								break;
						}
						break;
					}
					case 3 : {
							if(!(fisherMen[i].getFishermanTileX() - 1 < 0) && m.getMap(fisherMen[i].getFishermanTileX() - 1, fisherMen[i].getFishermanTileY()).equals("w")) {
								//fisherMan.setFishermanLocation(fishermanX - 1, fishermanY);
								fisherMen[i].move(-32, 0, -1, 0);
								fishermanMoved = true;
								break;
						}
						break;
					}
				}
			}
		}
	}
	
	public void fishermanCaughtFish() {
		JOptionPane.showMessageDialog(new JFrame(), "You have been caught! \nFisherman released you back into the water.");
		caught = false;
		for(Fisherman fm : fisherMen){
			randomStartFisherman(fm);
		}
		moveFishToStart();
		caughtCounter++;
	}
	
	public boolean isFishCaught() {
		for(Fisherman fm : fisherMen){
			if(fm.getFishermanTileX() + 1 == p.getTileX() && (fm.getFishermanTileY() == p.getTileY())){
				caught = true;
			} else if(fm.getFishermanTileX() - 1 == p.getTileX() && (fm.getFishermanTileY() == p.getTileY())){
				caught = true;
			}else if(fm.getFishermanTileX() == p.getTileX() && fm.getFishermanTileY() == p.getTileY() + 1){
				caught = true;
			}else if(fm.getFishermanTileX() == p.getTileX() && fm.getFishermanTileY() == p.getTileY() - 1){
				caught = true;
			}
		}
		return caught;
	}
	
	public void moveFishToStart(){
		f.reFog(p.getTileX(), p.getTileY(), "C");
		p.setPlayerStart(m.getStartX(), m.getStartY());
		f.iAmHereFog(m.getStartX(), m.getStartY());
	}
	
	public void isFishermanNear() {
		moveFisherman();
		if(isFishCaught()){
			fishermanCaughtFish();
		}
		/*else{
			System.out.println("Tiles: FM: (" + fisherMan.getFishermanTileX() + ", " + fisherMan.getFishermanTileY() + ") F: (" + p.getTileX() + ", " + p.getTileY() + ")");
			System.out.println("FM: (" + fisherMan.getFishermanX() + ", " + fisherMan.getFishermanY() + ") F: (" + p.getX() + ", " + p.getY() + ")");
		}*/
	}
	
	public void createNewFisherman(){
		Fisherman[] newFisherman = new Fisherman[level + 1];
		System.arraycopy(fisherMen, 0, newFisherman, 0, level);
		newFisherman[level + 1] = new Fisherman();
		fisherMen = new Fisherman[level + 1];
		System.arraycopy(newFisherman, 0, fisherMen, 0, level + 1);
	}
	
	//TODO: Need to make a new map, and increment fisherman by level number
	public void nextLevel(){
		//System.exit(0);
		startLevel();
	}
	
	public class Al extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int keycode = e.getKeyCode();
			if(keycode == KeyEvent.VK_W || keycode == KeyEvent.VK_UP){
				if(!m.getMap(p.getTileX(), p.getTileY() - 1).equals("w")) {
					p.move(0, -32, 0, -1);
					direction = 0;
					stepCount++;
					f.reFog(p.getTileX(), p.getTileY(), "U");
					isFishermanNear();
				}
			}
			if(keycode == KeyEvent.VK_S || keycode == KeyEvent.VK_DOWN){
				if(!m.getMap(p.getTileX(), p.getTileY() + 1).equals("w")) {
					p.move(0, 32, 0, 1);
					direction = 2;
					stepCount++;
					f.reFog(p.getTileX(), p.getTileY(), "D");
					isFishermanNear();
				}
			}
			if(keycode == KeyEvent.VK_A || keycode == KeyEvent.VK_LEFT){
				if(!m.getMap(p.getTileX() - 1, p.getTileY()).equals("w")) {
					p.move(-32, 0, -1, 0);
					direction = 3;
					stepCount++;
					f.reFog(p.getTileX(), p.getTileY(), "L");
					isFishermanNear();
				}
			}
			if(keycode == KeyEvent.VK_D || keycode == KeyEvent.VK_RIGHT){
				if(!m.getMap(p.getTileX() + 1, p.getTileY()).equals("w")) {
					p.move(32, 0, 1, 0);
					direction = 1;
					stepCount++;
					f.reFog(p.getTileX(), p.getTileY(), "R");
					isFishermanNear();
				}
			}
		}
		
		public void keyReleased(KeyEvent e) {
			if(m.getMap(p.getTileX(), p.getTileY()).equals("f")) {
				timer.stop();
				//eTime = LocalTime.now();
				eTime = System.currentTimeMillis();
				long seconds = (eTime - sTime) / 1000;
 		        //long seconds = ChronoUnit.SECONDS.between(sTime, eTime);
 		        long minutes = seconds / 60;
			    long secondsRemaining = seconds % 60;
			    String time = minutes + "m : " + secondsRemaining + "s";
				JOptionPane.showMessageDialog(new JFrame(), "You have won! \nLevel: " + level + "\nYour Time: " + time + " \nSteps Taken: " + stepCount + "\nTimes Caught: " + caughtCounter);
				nextLevel();
			}
			
			
		}
		
		public void keyTyped(KeyEvent e) {
			
		}
	}
}
