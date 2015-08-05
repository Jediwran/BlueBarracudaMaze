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
	private Player p2;
	private Fisherman[] fisherMen;
	private Fog f;
	private int stepCount, caughtCounter,direction;
	private int mapSize = 14;
	private int level = 0;
	private boolean fogEnabled = true;
	private boolean caught = false;
	private Random r = new Random();
	private int lives = 5;
	
	public Board() {
		m = new Map();
		m.setSize(mapSize);
		//m.setupMap();
		p = new Player();
		p2 = new Player();
		f = new Fog();
		addKeyListener(new Al());
		setFocusable(true);
		startLevel();
	}
	
	public void startLevel(){
		stepCount = 0;
		caughtCounter = 0;
		level += 1;
		m.setMapName(r.nextInt(8)+1);
		m.setupMap();
		fisherMen = new Fisherman[level];
		for(int i = 0; i < level; i++){
			fisherMen[i] = new Fisherman();
		}
		//player 1
		direction = 3;
		p.setPlayerStart(m.getStartX(), m.getStartY());
		//player 2
		p2.setPlayerStart(m.getStartX(), m.getStartY());
		randomStartFisherman(fisherMen[level - 1]);
		//player 1
		f.createFog(p.getTileX(), p.getTileY());
		//player 2
		f.createFog(p2.getTileX(), p2.getTileY());
		
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
			case 4:{
				temp = p2.getPlayerUp();
				break;
			}
			case 5:{
				temp = p2.getPlayerDown();
				break;
			}
			case 6:{
				temp = p2.getPlayer();
				break;
			}
			case 7:{
				temp = p2.getPlayerRight();
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
		for(int y = 0; y < m.getMapSize(); y++) {
			for(int x = 0; x < m.getMapSize(); x++) {
				if(m.getMap(x, y).equals("g")){
					g.drawImage(m.getGround(), x * 32, y * 32, null);
				}
				if(m.getMap(x, y).equals("w")){
					g.drawImage(m.getWall(), x * 32, y * 32, null);
				}
				if(m.getMap(x, y).equals("f")){
					g.drawImage(m.getFinish(), x * 32, y * 32, null);
				}
				if(m.getMap(x, y).equals("s")){
					g.drawImage(m.getStart(), x * 32, y * 32, null);
				}
			}
		}
		//draw player 1
		g.drawImage(drawPlayer(), p.getX(), p.getY(), null);
		//Draw Player 2
		g.drawImage(drawPlayer(), p2.getX(), p2.getY(), null);
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
		g.setColor(new Color(255,255,255));
		g.setFont(new Font("default", Font.BOLD, 16));
		g.drawString("Level: " + level + "   Steps: " + stepCount + "   Lives: " + lives, 16, 24);
	}
	
	public void randomStartFisherman(Fisherman f) {
		Random rand = new Random();
		int randX;
		int randY;
		//int xIterate = 0;
		//int yIterate = 0;
		//int fIterate = 0;
		//generate random number to assign placement of fisherman
		//continues to generate a random number if position falls
		//within 3 squares of the starting position
		do{
			//fIterate++; //iterator for Fisherman 
			
			do{
			//xIterate++;	//iterator for x coordinate
			randX = rand.nextInt(13);
			}while(Math.abs(m.getStartX()-randX) < 3);
			
			do{
			randY = rand.nextInt(13);
			//yIterate++;	//iterator for y coordinate
			}while(Math.abs(m.getStartY()-randY) < 3);
			
		//test that the location generated falls on a Wall "w" space	
		}while(!m.getMap(randX, randY).equals("w"));
		//System.out.println("X generated " + xIterate + "x");		//displays the number of times the x value had to generate
		//System.out.println("Y generated " + yIterate + "x");		//displays the number of times the y value had to generate
		//System.out.println("FISHERMAN PLACED AFTER " + fIterate +" ATTEMPTS!");	//prints the number of times the fisherman coordinates had to be generated
		//if(m.getMap(randX, randY).equals("w")) {
			f.setFishermanStartLocation(randX, randY);
		//}
	}
	
	public void moveFisherman() {
		for(Fisherman fm : fisherMen){
			boolean fishermanMoved = false;
			while (!fishermanMoved){
				int direction = new Random().nextInt(4);
				switch(direction){
					case 0 : {
							if(!(fm.getFishermanTileY() - 1 < 0) && m.getMap(fm.getFishermanTileX(), fm.getFishermanTileY() - 1).equals("w")) {
								//fisherMan.setFishermanLocation(fishermanX, fishermanY - 1);
								fishermanMoved = true;
								fm.move(0, -32, 0, -1);
								break;
							}
						break;
					}
					case 1 : {
							if(!(fm.getFishermanTileX() + 1 > m.getMapSize() - 1) && m.getMap(fm.getFishermanTileX() + 1, fm.getFishermanTileY()).equals("w")) {
								//fisherMan.setFishermanLocation(fishermanX + 1, fishermanY);
								fm.move(32, 0, 1, 0);
								fishermanMoved = true;
								break;
							}
						break;
					}
					case 2 : {
							if(!(fm.getFishermanTileY() + 1 > m.getMapSize() - 1) && m.getMap(fm.getFishermanTileX(), fm.getFishermanTileY() + 1).equals("w")) {
								//fisherMan.setFishermanLocation(fishermanX, fishermanY + 1);
								fm.move(0, 32, 0, 1);
								fishermanMoved = true;
								break;
						}
						break;
					}
					case 3 : {
							if(!(fm.getFishermanTileX() - 1 < 0) && m.getMap(fm.getFishermanTileX() - 1, fm.getFishermanTileY()).equals("w")) {
								//fisherMan.setFishermanLocation(fishermanX - 1, fishermanY);
								fm.move(-32, 0, -1, 0);
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
		lives--;
		if (lives ==0){
			Object[] selectMenuOptions={"New Game", "Exit"};
			int n = JOptionPane.showOptionDialog(null, "Select an option", "Game Over", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, selectMenuOptions, selectMenuOptions[0]);
				switch (n) {
				case 0:
					level = 0;
					lives = 5;
					startLevel();
					break;
				case 1:
					System.exit(0);
					break;
				default:
					//you're an idiot, you shouldn't reach here!
						break;
				}
		}
	}
	
	public boolean isFishCaught() {
		for(Fisherman fm : fisherMen){
			if((fm.getFishermanTileX() + 1 == p.getTileX() && (fm.getFishermanTileY() == p.getTileY())) || (fm.getFishermanTileX() + 1 == p2.getTileX() && (fm.getFishermanTileY() == p2.getTileY()))){
				caught = true;
			} else if((fm.getFishermanTileX() - 1 == p.getTileX() && (fm.getFishermanTileY() == p.getTileY())) || (fm.getFishermanTileX() - 1 == p2.getTileX() && (fm.getFishermanTileY() == p2.getTileY()))){
				caught = true;
			}else if((fm.getFishermanTileX() == p.getTileX() && fm.getFishermanTileY() == p.getTileY() + 1) || (fm.getFishermanTileX() == p2.getTileX() && fm.getFishermanTileY() == p2.getTileY() + 1)){
				caught = true;
			}else if((fm.getFishermanTileX() == p.getTileX() && fm.getFishermanTileY() == p.getTileY() - 1) || (fm.getFishermanTileX() == p2.getTileX() && fm.getFishermanTileY() == p2.getTileY() - 1)){
				caught = true;
			}
		}
		return caught;
	}
	
	public void moveFishToStart(){
		//player one
		f.reFog(p.getTileX(), p.getTileY(), "C");
		p.setPlayerStart(m.getStartX(), m.getStartY());
		f.iAmHereFog(m.getStartX(), m.getStartY());
		//player two
		p2.setPlayerStart(m.getStartX(), m.getStartY());
		f.reFog(p2.getTileX(), p2.getTileY(), "C");
	}
	
	public void isFishermanNear() {
		moveFisherman();
		if(isFishCaught()){
			fishermanCaughtFish();
		}
	}
	
	public void createNewFisherman(){
		Fisherman[] newFisherman = new Fisherman[level + 1];
		System.arraycopy(fisherMen, 0, newFisherman, 0, level);
		newFisherman[level + 1] = new Fisherman();
		fisherMen = new Fisherman[level + 1];
		System.arraycopy(newFisherman, 0, fisherMen, 0, level + 1);
	}
	
	public void isFinish(){
		if(m.getMap(p.getTileX(), p.getTileY()).equals("f") || m.getMap(p2.getTileX(), p2.getTileY()).equals("f")) {
			repaint();
			timer.stop();
			//eTime = LocalTime.now();
			eTime = System.currentTimeMillis();
			long seconds = (eTime - sTime) / 1000;
		        //long seconds = ChronoUnit.SECONDS.between(sTime, eTime);
		        long minutes = seconds / 60;
		    long secondsRemaining = seconds % 60;
		    String time = minutes + "m : " + secondsRemaining + "s";
			JOptionPane.showMessageDialog(new JFrame(), "You have won! \nLevel: " + level + "\nYour Time: " + time + " \nSteps Taken: " + stepCount + "\nTimes Caught: " + caughtCounter);
			startLevel();
		}else{
			isFishermanNear();
		}
	}
	
	public class Al extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int keycode = e.getKeyCode();
			if(keycode == KeyEvent.VK_UP){
				if(!m.getMap(p.getTileX(), p.getTileY() - 1).equals("w")) {
					p.move(0, -32, 0, -1);
					direction = 0;
					stepCount++;
					f.reFog(p.getTileX(), p.getTileY(), "U");
					f.iAmHereFog(p2.getTileX(), p2.getTileY());
					isFinish();
				}
			}
			if(keycode == KeyEvent.VK_DOWN){
				if(!m.getMap(p.getTileX(), p.getTileY() + 1).equals("w")) {
					p.move(0, 32, 0, 1);
					direction = 2;
					stepCount++;
					f.reFog(p.getTileX(), p.getTileY(), "D");
					f.iAmHereFog(p2.getTileX(), p2.getTileY());
					isFinish();
				}
			}
			if(keycode == KeyEvent.VK_LEFT){
				if(!m.getMap(p.getTileX() - 1, p.getTileY()).equals("w")) {
					p.move(-32, 0, -1, 0);
					direction = 3;
					stepCount++;
					f.reFog(p.getTileX(), p.getTileY(), "L");
					f.iAmHereFog(p2.getTileX(), p2.getTileY());
					isFinish();
				}
			}
			if(keycode == KeyEvent.VK_RIGHT){
				if(!m.getMap(p.getTileX() + 1, p.getTileY()).equals("w")) {
					p.move(32, 0, 1, 0);
					direction = 1;
					stepCount++;
					f.reFog(p.getTileX(), p.getTileY(), "R");
					f.iAmHereFog(p2.getTileX(), p2.getTileY());
					isFinish();
				}
			}
			//Player 2 controls (WASD)
			if(keycode == KeyEvent.VK_W){
				if(!m.getMap(p2.getTileX(), p2.getTileY() - 1).equals("w")) {
					p2.move(0, -32, 0, -1);
					direction = 4;
					stepCount++;
					f.reFog(p2.getTileX(), p2.getTileY(), "U");
					f.iAmHereFog(p.getTileX(), p.getTileY());
					isFinish();
				}
			}
			if(keycode == KeyEvent.VK_S){
				if(!m.getMap(p2.getTileX(), p2.getTileY() + 1).equals("w")) {
					p2.move(0, 32, 0, 1);
					direction = 5;
					stepCount++;
					f.reFog(p2.getTileX(), p2.getTileY(), "D");
					f.iAmHereFog(p.getTileX(), p.getTileY());
					isFinish();
				}
			}
			if(keycode == KeyEvent.VK_A){
				if(!m.getMap(p2.getTileX() - 1, p2.getTileY()).equals("w")) {
					p2.move(-32, 0, -1, 0);
					direction = 6;
					stepCount++;
					f.reFog(p2.getTileX(), p2.getTileY(), "L");
					f.iAmHereFog(p.getTileX(), p.getTileY());
					isFinish();
				}
			}
			if(keycode == KeyEvent.VK_D){
				if(!m.getMap(p2.getTileX() + 1, p2.getTileY()).equals("w")) {
					p2.move(32, 0, 1, 0);
					direction = 7;
					stepCount++;
					f.reFog(p2.getTileX(), p2.getTileY(), "R");
					f.iAmHereFog(p.getTileX(), p.getTileY());
					isFinish();
				}
			}
		}
		
		public void keyReleased(KeyEvent e) {
			
		}
		
		public void keyTyped(KeyEvent e) {
			
		}
	}
}
