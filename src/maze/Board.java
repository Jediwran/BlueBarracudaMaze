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
	//private Player p;
	//private Player p2;
	private Player[] player;
	private Fisherman[] fisherMen;
	private Fog f;
	private int directionPlayer1, directionPlayer2;
	private int mapSize = 14;
	private int level = 0;
	private boolean fogEnabled = true;
	private boolean caught = false;
	private boolean isFinished = false;
	private Random r = new Random();
	//private int lives = 5;
	private int numPlayers = 1;
	
	public Board() {
		m = new Map();
		m.setSize(mapSize);
		//m.setupMap();
		selectPlayerNumber();
		//p = new Player();
		//p2 = new Player();
		player = new Player[numPlayers];
		for(int i = 0; i < numPlayers; i++){
			player[i] = new Player();
			player[i].setNumber(i);
		}
		f = new Fog();
		addKeyListener(new Al());
		setFocusable(true);
		startLevel();
	}
	
	public void selectPlayerNumber(){
		Object[] selectMenuOptions={"1 Player", "2 Players"};
		int n = JOptionPane.showOptionDialog(null, "Select Number of Players", "Number of Players", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, selectMenuOptions, selectMenuOptions[0]);
			switch (n) {
			case 0:
				numPlayers = 1;
				break;
			case 1:
				numPlayers = 2;
				break;
			}
	}
	
	public void startLevel(){
		for(Player p: player){
			p.setPlayerStepsTaken(0);
			p.setTimesCaught(0);
		}
		level += 1;
		m.setMapName(r.nextInt(8)+1);
		m.setupMap();
		fisherMen = new Fisherman[level];
		for(int i = 0; i < level; i++){
			fisherMen[i] = new Fisherman();
		}
		//player 1
		directionPlayer1 = 3;
		player[0].setPlayerStart(m.getStartX(), m.getStartY());
		f.createFog(player[0].getTileX(), player[0].getTileY());
		//player 2
		if(player.length > 1){
			directionPlayer2 = 3;
			player[1].setPlayerStart(m.getStartX(), m.getStartY());
			f.createFog(player[1].getTileX(), player[1].getTileY());
		}
		randomStartFisherman(fisherMen[level - 1]);
		
		//sTime = LocalTime.now();
		sTime = System.currentTimeMillis();
		timer = new Timer(25, this);
		timer.start();
	}
	public Image drawPlayer(Player p, int direction){
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
		g.drawImage(drawPlayer(player[0], directionPlayer1), player[0].getX(), player[0].getY(), null);
		//Draw Player 2
		if(player.length > 1){
			g.drawImage(drawPlayer(player[1], directionPlayer2), player[1].getX(), player[1].getY(), null);
		}
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
		g.drawString("Level: " + level, 16, 24);
		int i = 10;
		for(Player p : player){
			g.drawString("P" + (p.getNumber()+1) + "-Steps: " + p.getPlayerStepsTaken() + "   Lives: " + p.getPlayerLives(), 80 + i, 24);
			i += 180;
		}
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
	
	public void fishermanCaughtFish(Player p) {
		JOptionPane.showMessageDialog(new JFrame(), "You have been caught! \nFisherman released you back into the water.");
		caught = false;
		for(Fisherman fm : fisherMen){
			randomStartFisherman(fm);
		}
		moveFishToStart(p.getNumber());
		p.setTimesCaught(p.getTimesCaught()+1);
		p.setPlayerLives(p.getPlayerLives() - 1);
		if (p.getPlayerLives() ==0){
			Object[] selectMenuOptions={"New Game", "Exit"};
			int n = JOptionPane.showOptionDialog(null, "Select an option", "Game Over", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, selectMenuOptions, selectMenuOptions[0]);
				switch (n) {
				case 0:
					level = 0;
					for(Player newPlayer: player){
						newPlayer.setPlayerLives(5);
					}
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
			for(Player p : player){
			//for(int i=0; i < player.length; i++){
				if(fm.getFishermanTileX()+1 == p.getTileX() && fm.getFishermanTileY() == p.getTileY()){
					caught = true;
				}else if(fm.getFishermanTileX()-1 == p.getTileX() && fm.getFishermanTileY() == p.getTileY()){
					caught = true;
				}else if(fm.getFishermanTileX() == p.getTileX() && fm.getFishermanTileY() == p.getTileY() + 1){
					caught = true;
				}else if(fm.getFishermanTileX() == p.getTileX() && fm.getFishermanTileY() == p.getTileY() - 1){
					caught = true;
				}
			}
			/*if((fm.getFishermanTileX() + 1 == player[0].getTileX() && (fm.getFishermanTileY() == player[0].getTileY())) || (fm.getFishermanTileX() + 1 == player[1].getTileX() && (fm.getFishermanTileY() == player[1].getTileY()))){
				caught = true;
			} else if((fm.getFishermanTileX() - 1 == player[0].getTileX() && (fm.getFishermanTileY() == player[0].getTileY())) || (fm.getFishermanTileX() - 1 == player[1].getTileX() && (fm.getFishermanTileY() == player[1].getTileY()))){
				caught = true;
			}else if((fm.getFishermanTileX() == player[0].getTileX() && fm.getFishermanTileY() == player[0].getTileY() + 1) || (fm.getFishermanTileX() == player[1].getTileX() && fm.getFishermanTileY() == player[1].getTileY() + 1)){
				caught = true;
			}else if((fm.getFishermanTileX() == player[0].getTileX() && fm.getFishermanTileY() == player[0].getTileY() - 1) || (fm.getFishermanTileX() == player[1].getTileX() && fm.getFishermanTileY() == player[1].getTileY() - 1)){
				caught = true;
			}*/
		}
		return caught;
	}
	
	public void moveFishToStart(int caughtPlayer){
		//for(Player p : player){
		f.reFog(player[caughtPlayer].getTileX(), player[caughtPlayer].getTileY(), "C");
		player[caughtPlayer].setPlayerStart(m.getStartX(), m.getStartY());
		f.iAmHereFog(m.getStartX(), m.getStartY());
/*		//player one
		f.reFog(player[0].getTileX(), player[0].getTileY(), "C");
		player[0].setPlayerStart(m.getStartX(), m.getStartY());
		f.iAmHereFog(m.getStartX(), m.getStartY());
		//player two
		player[1].setPlayerStart(m.getStartX(), m.getStartY());
		f.reFog(player[1].getTileX(), player[1].getTileY(), "C");*/
	}
	
	public void isFishermanNear(Player p) {
		moveFisherman();
		if(isFishCaught()){
			fishermanCaughtFish(p);
		}
	}
	
	public void createNewFisherman(){
		Fisherman[] newFisherman = new Fisherman[level + 1];
		System.arraycopy(fisherMen, 0, newFisherman, 0, level);
		newFisherman[level + 1] = new Fisherman();
		fisherMen = new Fisherman[level + 1];
		System.arraycopy(newFisherman, 0, fisherMen, 0, level + 1);
	}
	
	public void isFinish(Player p){
		//for(Player p : player){
			if(m.getMap(p.getTileX(), p.getTileY()).equals("f")){
				isFinished = true;
			}
			if(!isFinished){
				isFishermanNear(p);
			}
		//}
		//if(m.getMap(player[0].getTileX(), player[0].getTileY()).equals("f") || m.getMap(player[1].getTileX(), player[1].getTileY()).equals("f")) {
		if(isFinished){
			repaint();
			isFinished = false;
			timer.stop();
			//eTime = LocalTime.now();
			eTime = System.currentTimeMillis();
			long seconds = (eTime - sTime) / 1000;
		        //long seconds = ChronoUnit.SECONDS.between(sTime, eTime);
		        long minutes = seconds / 60;
		    long secondsRemaining = seconds % 60;
		    String time = minutes + "m : " + secondsRemaining + "s";
			JOptionPane.showMessageDialog(new JFrame(), "You have won! \nLevel: " + level + "\nYour Time: " + time);
			startLevel();
		}
	}
	
	public class Al extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int keycode = e.getKeyCode();
			if(keycode == KeyEvent.VK_UP){
				if(!m.getMap(player[0].getTileX(), player[0].getTileY() - 1).equals("w")) {
					player[0].move(0, -32, 0, -1);
					directionPlayer1 = 0;
					player[0].setPlayerStepsTaken(player[0].getPlayerStepsTaken() + 1);
					f.reFog(player[0].getTileX(), player[0].getTileY(), "U");
					//f.iAmHereFog(player[1].getTileX(), player[1].getTileY());
					for(Player p: player){
						f.iAmHereFog(p.getTileX(), p.getTileY());
					}
					isFinish(player[0]);
				}
			}
			if(keycode == KeyEvent.VK_DOWN){
				if(!m.getMap(player[0].getTileX(), player[0].getTileY() + 1).equals("w")) {
					player[0].move(0, 32, 0, 1);
					directionPlayer1 = 2;
					player[0].setPlayerStepsTaken(player[0].getPlayerStepsTaken() + 1);
					f.reFog(player[0].getTileX(), player[0].getTileY(), "D");
					for(Player p: player){
						f.iAmHereFog(p.getTileX(), p.getTileY());
					}
					isFinish(player[0]);
				}
			}
			if(keycode == KeyEvent.VK_LEFT){
				if(!m.getMap(player[0].getTileX() - 1, player[0].getTileY()).equals("w")) {
					player[0].move(-32, 0, -1, 0);
					directionPlayer1 = 3;
					player[0].setPlayerStepsTaken(player[0].getPlayerStepsTaken() + 1);
					f.reFog(player[0].getTileX(), player[0].getTileY(), "L");
					//f.iAmHereFog(player[1].getTileX(), player[1].getTileY());
					for(Player p: player){
						f.iAmHereFog(p.getTileX(), p.getTileY());
					}
					isFinish(player[0]);
				}
			}
			if(keycode == KeyEvent.VK_RIGHT){
				if(!m.getMap(player[0].getTileX() + 1, player[0].getTileY()).equals("w")) {
					player[0].move(32, 0, 1, 0);
					directionPlayer1 = 1;
					player[0].setPlayerStepsTaken(player[0].getPlayerStepsTaken() + 1);
					f.reFog(player[0].getTileX(), player[0].getTileY(), "R");
					//f.iAmHereFog(player[1].getTileX(), player[1].getTileY());
					for(Player p: player){
						f.iAmHereFog(p.getTileX(), p.getTileY());
					}
					isFinish(player[0]);
				}
			}
			//Player 2 controls (WASD)
			if(keycode == KeyEvent.VK_W){
				if(!m.getMap(player[1].getTileX(), player[1].getTileY() - 1).equals("w") && player.length > 1) {
					player[1].move(0, -32, 0, -1);
					directionPlayer2 = 0;
					player[1].setPlayerStepsTaken(player[1].getPlayerStepsTaken() + 1);
					f.reFog(player[1].getTileX(), player[1].getTileY(), "U");
					//f.iAmHereFog(player[0].getTileX(), player[0].getTileY());
					for(Player p: player){
						f.iAmHereFog(p.getTileX(), p.getTileY());
					}
					isFinish(player[1]);
				}
			}
			if(keycode == KeyEvent.VK_S){
				if(!m.getMap(player[1].getTileX(), player[1].getTileY() + 1).equals("w") && player.length > 1) {
					player[1].move(0, 32, 0, 1);
					directionPlayer2 = 2;
					player[1].setPlayerStepsTaken(player[1].getPlayerStepsTaken() + 1);
					f.reFog(player[1].getTileX(), player[1].getTileY(), "D");
					//f.iAmHereFog(player[0].getTileX(), player[0].getTileY());
					for(Player p: player){
						f.iAmHereFog(p.getTileX(), p.getTileY());
					}
					isFinish(player[1]);
				}
			}
			if(keycode == KeyEvent.VK_A){
				if(!m.getMap(player[1].getTileX() - 1, player[1].getTileY()).equals("w") && player.length > 1) {
					player[1].move(-32, 0, -1, 0);
					directionPlayer2 = 3;
					player[1].setPlayerStepsTaken(player[1].getPlayerStepsTaken() + 1);
					f.reFog(player[1].getTileX(), player[1].getTileY(), "L");
					//f.iAmHereFog(player[0].getTileX(), player[0].getTileY());
					for(Player p: player){
						f.iAmHereFog(p.getTileX(), p.getTileY());
					}
					isFinish(player[1]);
				}
			}
			if(keycode == KeyEvent.VK_D){
				if(!m.getMap(player[1].getTileX() + 1, player[1].getTileY()).equals("w") && player.length > 1) {
					player[1].move(32, 0, 1, 0);
					directionPlayer2 = 1;
					player[1].setPlayerStepsTaken(player[1].getPlayerStepsTaken() + 1);
					f.reFog(player[1].getTileX(), player[1].getTileY(), "R");
					//f.iAmHereFog(player[0].getTileX(), player[0].getTileY());
					for(Player p: player){
						f.iAmHereFog(p.getTileX(), p.getTileY());
					}
					isFinish(player[1]);
				}
			}
		}
		
		public void keyReleased(KeyEvent e) {
			
		}
		
		public void keyTyped(KeyEvent e) {
			
		}
	}
}
