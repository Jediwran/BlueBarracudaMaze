package maze;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;

public class Board extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private Timer timer;
	private long sTime, eTime;
	private Map m;
	private Maze maze;
	private Player[] playerList;
	private Fisherman[] fisherMen = new Fisherman[0];
	private Fog f;
	private int mapSize = 16;
	private int level = 0;
	private boolean fogEnabled = false;
	private Random r = new Random();
	private int numPlayers = 1;
	
	public Board(Maze maze) {
		this.maze = maze;
		m = new Map();
		m.setSize(mapSize);
		maze.frame.setSize(Maze.width+(32*m.getMapSize()), Maze.height+(32*m.getMapSize()));
		f = new Fog();
		f.setFogMapSize(mapSize);
		selectPlayerNumber();
		playerList = new Player[numPlayers];
		for(int i = 0; i < numPlayers; i++){
			playerList[i] = new Player(m,f);
			playerList[i].setNumber(i);

			selectPlayerColor(playerList[i]);
			playerList[i].setPlayerImages();
			new Thread(playerList[i]).start();

		}
		//addKeyListener(new Al());
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
		m.newMap(mapSize);
		
		maze.frame.setSize(Maze.width+(32*m.getMapSize()), Maze.height+(32*m.getMapSize()));
		maze.frame.setVisible(true);
		for(Fisherman fisher:fisherMen)
		{
			fisher.requestStop();
		}
		
		fisherMen = new Fisherman[level];
		for(int i = 0; i < level; i++){
			fisherMen[i] = new Fisherman(m);
			randomStartFisherman(fisherMen[i]);
			fisherMen[i].start();
		}
		for(Player p: playerList){
			p.setPlayerStepsTaken(0);
			p.setTimesCaught(0);
			p.setPlayerStart(m.getStartX(), m.getStartY());
			f.createFog(p.getTileX(), p.getTileY());
		}
		sTime = System.currentTimeMillis();
		timer = new Timer(25, this);
		timer.start();
	}
	
	public void actionPerformed(ActionEvent e) {
		repaint();
		for(Player p : playerList){
			if(m.getMap(p.getTileX(), p.getTileY()) == 'f'){
				p.setFinished(true);
				isFinished();
			}
		}
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
		for (Fisherman fisherman : fisherMen) {
			if(fog[fisherman.getFishermanTileX()][fisherman.getFishermanTileY()] == 0){
				g.drawImage(fisherman.getFisherman(), fisherman.getFishermanX(), fisherman.getFishermanY(), null);
			}
		}
		g.setColor(new Color(255,255,255));
		g.setFont(new Font("default", Font.BOLD, 16));
		g.drawString("Level: " + level, 16, 24);
		int i = 10;
		int j = 10;
		for(Player p : playerList){
			if(i == 370){
				g.drawString("P" + (p.getNumber()+1) + "-Steps: " + p.getPlayerStepsTaken() + "   Lives: " + p.getPlayerLives(), 80 + j, 44);
				j += 180;
			}else{
				g.drawString("P" + (p.getNumber()+1) + "-Steps: " + p.getPlayerStepsTaken() + "   Lives: " + p.getPlayerLives(), 80 + i, 24);
				i += 180;
			}
			g.drawImage(p.drawPlayer(), p.getX(), p.getY(), null);
			f.iAmHereFog(p.getTileX(), p.getTileY());
		}
	}
	
	public void randomStartFisherman(Fisherman f) {
		Random rand = new Random();
		int randX;
		int randY;
		//generate random number to assign placement of fisherman
		//continues to generate a random number if position falls
		//within 3 squares of the starting position
		do{
			
			do{
			randX = rand.nextInt(13);
			}while(Math.abs(m.getStartX()-randX) < 3);
			
			do{
			randY = rand.nextInt(13);
			}while(Math.abs(m.getStartY()-randY) < 3);
			
		//test that the location generated falls on a Wall "w" space	
		}while(m.getMap(randX, randY) != 'w');
			f.setStartLocation(randX, randY);
	}
	
	public void fishermanCaughtFish(Player p) {
		JOptionPane.showMessageDialog(new JFrame(), "Player " + (p.getNumber()+1) + " You have been caught! \nFisherman released you back into the water.");
		//caught = false;
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
					for(Player newPlayer: playerList){
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
			for(Player p : playerList){
				p.isCaught(fm);
				if(fm.getFishermanTileX()+1 == p.getTileX() && fm.getFishermanTileY() == p.getTileY()){
					//caught = true;
				}else if(fm.getFishermanTileX()-1 == p.getTileX() && fm.getFishermanTileY() == p.getTileY()){
					//caught = true;
				}else if(fm.getFishermanTileX() == p.getTileX() && fm.getFishermanTileY() == p.getTileY() + 1){
					//caught = true;
				}else if(fm.getFishermanTileX() == p.getTileX() && fm.getFishermanTileY() == p.getTileY() - 1){
					//caught = true;
				}
			}
		}
		return true;
	}
	
	public void moveFishToStart(int caughtPlayer){
		f.reFog(playerList[caughtPlayer].getTileX(), playerList[caughtPlayer].getTileY(), "C");
		playerList[caughtPlayer].setPlayerStart(m.getStartX(), m.getStartY());
		f.iAmHereFog(m.getStartX(), m.getStartY());
	}
	
	public void isFishermanNear(Player p) {
		//moveFisherman();
		if(isFishCaught()){
			fishermanCaughtFish(p);
		}
	}
	
	/*public void createNewFisherman(){
		Fisherman[] newFisherman = new Fisherman[level + 1];
		System.arraycopy(fisherMen, 0, newFisherman, 0, level);
		newFisherman[level + 1] = new Fisherman(m);
		fisherMen = new Fisherman[level + 1];
		System.arraycopy(newFisherman, 0, fisherMen, 0, level + 1);
	}*/
	
	public void isFinished(){
		for(Player p : playerList){
			p.setFinished(false);
		}
		
		timer.stop();
		eTime = System.currentTimeMillis();
		long seconds = (eTime - sTime) / 1000;
        long minutes = seconds / 60;
	    long secondsRemaining = seconds % 60;
	    String time = minutes + "m : " + secondsRemaining + "s";
	    String stats = "";
	    for(Player finalPlayer: playerList){
	    	stats += "\n-----------------------\nPlayer " + (finalPlayer.getNumber()+1) + "\nSteps: " + finalPlayer.getPlayerStepsTaken() + "\nLives: " + finalPlayer.getPlayerLives();
	    }
		JOptionPane.showMessageDialog(new JFrame(), "You have won! \nLevel: " + level + "\nYour Time: " + time + stats);
		startLevel();
	}	
}
