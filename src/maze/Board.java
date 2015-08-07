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
	private Maze maze;
	private Player[] player;
	private Fisherman[] fisherMen;
	private Fog f;
	private int mapSize = 16;
	private int level = 0;
	private boolean fogEnabled = false;
	private boolean caught = false;
	private boolean isFinished = false;
	private Random r = new Random();
	private int numPlayers = 1;
	
	public Board(Maze maze) {
		this.maze = maze;
		m = new Map();
		m.setSize(mapSize);
		//m.setSize(18);
		maze.frame.setSize(Maze.width+(32*m.getMapSize()), Maze.height+(32*m.getMapSize()));
		//m.setupMap();
		selectPlayerNumber();
		//p = new Player();
		//p2 = new Player();
		player = new Player[numPlayers];
		for(int i = 0; i < numPlayers; i++){
			player[i] = new Player();
			player[i].setNumber(i);

			selectPlayerColor(player[i]);
			player[i].setPlayerImages();
		}
		f = new Fog();
		f.setFogMapSize(mapSize);
		addKeyListener(new Al());
		setFocusable(true);
		startLevel();
	}
	
	public void selectPlayerColor(Player p){
	Object[] selectMenuOptions={"Blue", "Orange", "Green", "Purple"};
	int n = JOptionPane.showOptionDialog(null, "Player " + (p.getNumber()+1) + "Select Color of Fish", "Fish Color", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, selectMenuOptions, selectMenuOptions[0]);
		switch (n) {
		case 0:
			p.setColor("blue");
			break;
		case 1:
			p.setColor("orange");
			break;
		case 2:
			p.setColor("green");
			break;
		case 3:
			p.setColor("purple");
			break;
		}
	}
	
	public void selectPlayerNumber(){
		Object[] selectMenuOptions={"1 Player", "2 Players", "3 Players", "4 Players"};
		int n = JOptionPane.showOptionDialog(null, "Select Number of Players", "Number of Players", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, selectMenuOptions, selectMenuOptions[0]);
			switch (n) {
			case 0:
				numPlayers = 1;
				break;
			case 1:
				numPlayers = 2;
				break;
			case 2:
				numPlayers = 3;
				break;
			case 3:
				numPlayers = 4;
				break;
			}
	}
	
	public void startLevel(){
		level += 1;
		m.setMapName(r.nextInt(8)+1);
		//m.setMapName(2);
		
		//m.setupMap();
		
		m.newMap(mapSize);
		
		maze.frame.setSize(Maze.width+(32*m.getMapSize()), Maze.height+(32*m.getMapSize()));
		maze.frame.setVisible(true);
		fisherMen = new Fisherman[level];
		for(int i = 0; i < level; i++){
			fisherMen[i] = new Fisherman();
			randomStartFisherman(fisherMen[i]);
		}
		for(Player p: player){
			p.setPlayerStepsTaken(0);
			p.setTimesCaught(0);
			p.setPlayerStart(m.getStartX(), m.getStartY());
			f.createFog(p.getTileX(), p.getTileY());
		}
		//player 1
		//directionPlayer1 = 3;
		/*player[0].setPlayerStart(m.getStartX(), m.getStartY());
		f.createFog(player[0].getTileX(), player[0].getTileY());
		//player 2
		if(player.length > 1){
			//directionPlayer2 = 3;
			player[1].setPlayerStart(m.getStartX(), m.getStartY());
			f.createFog(player[1].getTileX(), player[1].getTileY());
		}*/
		//randomStartFisherman(fisherMen[level - 1]);
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
				temp = p.getPlayerLeft();
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
				if(m.getMap(x, y) == 'g'){
					g.drawImage(m.getGround(), x * 32, y * 32, null);
				}
				if(m.getMap(x, y) == 'b'){
					g.drawImage(m.getWall(), x * 32, y * 32, null);
				}
				if(m.getMap(x, y) == 'w'){
					g.drawImage(m.getWall(), x * 32, y * 32, null);
				}
				if(m.getMap(x, y) == 'f'){
					g.drawImage(m.getFinish(), x * 32, y * 32, null);
				}
				if(m.getMap(x, y) == 's'){
					g.drawImage(m.getStart(), x * 32, y * 32, null);
				}
			}
		}
		for(Player p: player){
			g.drawImage(drawPlayer(p, p.getDirection()), p.getX(), p.getY(), null);
		}
		//Draw Player 1
		//g.drawImage(drawPlayer(player[0], directionPlayer1), player[0].getX(), player[0].getY(), null);
		//Draw Player 2
		/*if(player.length > 1){
			g.drawImage(drawPlayer(player[1], directionPlayer2), player[1].getX(), player[1].getY(), null);
		}*/
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
		int j = 10;
		for(Player p : player){
			if(i == 370){
				g.drawString("P" + (p.getNumber()+1) + "-Steps: " + p.getPlayerStepsTaken() + "   Lives: " + p.getPlayerLives(), 80 + j, 44);
				j += 180;
			}else{
				g.drawString("P" + (p.getNumber()+1) + "-Steps: " + p.getPlayerStepsTaken() + "   Lives: " + p.getPlayerLives(), 80 + i, 24);
				i += 180;
			}
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
		}while(m.getMap(randX, randY) != 'w');
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
							if(!(fm.getFishermanTileY() - 1 < 0) && m.getMap(fm.getFishermanTileX(), fm.getFishermanTileY() - 1) == 'w') {
								fishermanMoved = true;
								fm.move(0, -32, 0, -1);
								break;
							}
						break;
					}
					case 1 : {
							if(!(fm.getFishermanTileX() + 1 > m.getMapSize() - 1) && m.getMap(fm.getFishermanTileX() + 1, fm.getFishermanTileY()) == 'w') {
								fm.move(32, 0, 1, 0);
								fishermanMoved = true;
								break;
							}
						break;
					}
					case 2 : {
							if(!(fm.getFishermanTileY() + 1 > m.getMapSize() - 1) && m.getMap(fm.getFishermanTileX(), fm.getFishermanTileY() + 1) == 'w') {
								fm.move(0, 32, 0, 1);
								fishermanMoved = true;
								break;
						}
						break;
					}
					case 3 : {
							if(!(fm.getFishermanTileX() - 1 < 0) && m.getMap(fm.getFishermanTileX() - 1, fm.getFishermanTileY()) == 'w') {
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
		JOptionPane.showMessageDialog(new JFrame(), "Player " + (p.getNumber()+1) + " You have been caught! \nFisherman released you back into the water.");
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
			if(m.getMap(p.getTileX(), p.getTileY()) == 'f'){
				isFinished = true;
			}
			if(!isFinished){
				for(Player checkPlayer: player){
					isFishermanNear(checkPlayer);
				}
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
		    String stats = "";
		    for(Player finalPlayer: player){
		    	stats += "\n-----------------------\nPlayer " + (finalPlayer.getNumber()+1) + "\nSteps: " + finalPlayer.getPlayerStepsTaken() + "\nLives: " + finalPlayer.getPlayerLives();
		    }
			JOptionPane.showMessageDialog(new JFrame(), "You have won! \nLevel: " + level + "\nYour Time: " + time + stats);
			startLevel();
		}
	}
	
	public void movePlayerUp(Player p){
		if(m.getMap(p.getTileX(), p.getTileY() - 1) != 'w') {
			p.move(0, -32, 0, -1);
			//directionPlayer1 = 0;
			p.setDirection(0);
			p.setPlayerStepsTaken(p.getPlayerStepsTaken() + 1);
			f.reFog(p.getTileX(), p.getTileY(), "U");
			f.iAmHereFog(p.getTileX(), p.getTileY());
			isFinish(p);
		}
	}
	
	public void movePlayerDown(Player p){
		if(m.getMap(p.getTileX(), p.getTileY() + 1) != 'w') {
			p.move(0, 32, 0, 1);
			//directionPlayer1 = 2;
			p.setDirection(2);
			p.setPlayerStepsTaken(p.getPlayerStepsTaken() + 1);
			f.reFog(p.getTileX(), p.getTileY(), "D");
			f.iAmHereFog(p.getTileX(), p.getTileY());
			isFinish(p);
		}
	}
	
	public void movePlayerLeft(Player p){
		if(m.getMap(p.getTileX() - 1, p.getTileY()) != 'w') {
			p.move(-32, 0, -1, 0);
			//directionPlayer1 = 3;
			p.setDirection(3);
			p.setPlayerStepsTaken(p.getPlayerStepsTaken() + 1);
			f.reFog(p.getTileX(), p.getTileY(), "L");
			f.iAmHereFog(p.getTileX(), p.getTileY());
			isFinish(p);
		}
	}
	
	public void movePlayerRight(Player p){
		if(m.getMap(p.getTileX() + 1, p.getTileY()) != 'w') {
			p.move(32, 0, 1, 0);
			//directionPlayer1 = 1;
			p.setDirection(1);
			p.setPlayerStepsTaken(p.getPlayerStepsTaken() + 1);
			f.reFog(p.getTileX(), p.getTileY(), "R");
			f.iAmHereFog(p.getTileX(), p.getTileY());
			isFinish(p);
		}
	}
	
	public class Al extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int keycode = e.getKeyCode();
			switch(keycode){
				case KeyEvent.VK_UP:
					movePlayerUp(player[0]);
					break;
				case KeyEvent.VK_LEFT:
					movePlayerLeft(player[0]);
					break;
				case KeyEvent.VK_DOWN:
					movePlayerDown(player[0]);
					break;
				case KeyEvent.VK_RIGHT:
					movePlayerRight(player[0]);
					break;
				case KeyEvent.VK_W:
					if(player.length > 1){
						movePlayerUp(player[1]);
					}
					break;
				case KeyEvent.VK_A:
					if(player.length > 1){
						movePlayerLeft(player[1]);
					}
					break;
				case KeyEvent.VK_S:
					if(player.length > 1){
						movePlayerDown(player[1]);
					}
					break;
				case KeyEvent.VK_D:
					if(player.length > 1){
						movePlayerRight(player[1]);
					}
					break;
				case KeyEvent.VK_I:
					if(player.length > 2){
						movePlayerUp(player[2]);
					}
					break;
				case KeyEvent.VK_J:
					if(player.length > 2){
						movePlayerLeft(player[2]);
					}
					break;
				case KeyEvent.VK_K:
					if(player.length > 2){
						movePlayerDown(player[2]);
					}
					break;
				case KeyEvent.VK_L:
					if(player.length > 2){
						movePlayerRight(player[2]);
					}
					break;
				case KeyEvent.VK_NUMPAD8:
					if(player.length > 3){
						movePlayerUp(player[3]);
					}
					break;
				case KeyEvent.VK_NUMPAD4:
					if(player.length > 3){
						movePlayerLeft(player[3]);
					}
					break;
				case KeyEvent.VK_NUMPAD2:
					if(player.length > 3){
						movePlayerDown(player[3]);
					}
					break;
				case KeyEvent.VK_NUMPAD6:
					if(player.length > 3){
						movePlayerRight(player[3]);
					}
					break;
				/*if(!m.getMap(player[0].getTileX(), player[0].getTileY() - 1).equals("w")) {
					player[0].move(0, -32, 0, -1);
					directionPlayer1 = 0;
					player[0].setPlayerStepsTaken(player[0].getPlayerStepsTaken() + 1);
					f.reFog(player[0].getTileX(), player[0].getTileY(), "U");
					//f.iAmHereFog(player[1].getTileX(), player[1].getTileY());
					for(Player p: player){
						f.iAmHereFog(p.getTileX(), p.getTileY());
					}
					isFinish(player[0]);
				}*/
			}
			/*if(keycode == KeyEvent.VK_UP){
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
			}*/
			/*if(keycode == KeyEvent.VK_DOWN){
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
			}*/
			/*if(keycode == KeyEvent.VK_LEFT){
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
			}*/
			/*if(keycode == KeyEvent.VK_RIGHT){
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
			}*/
			/*if(player.length > 1){
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
			}*/
		}
		
		public void keyReleased(KeyEvent e) {
			
		}
		
		public void keyTyped(KeyEvent e) {
			
		}
	}
}
