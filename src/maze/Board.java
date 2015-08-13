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
	private int deadPlayers = 0;
	private boolean fogEnabled = Settings.getSettings().getFogEnabled();
	private Random r = new Random();
	private int numPlayers = Settings.getSettings().getNumberPlayers();
	private Barrel barrel;
	private String colorRestore;
	private int playerNum;
	
	public Board(Maze maze) {
		this.maze = maze;
		m = new Map();
		m.setSize(mapSize);
		maze.frame.setSize(Maze.width+(32*m.getMapSize()), Maze.height+(32*m.getMapSize()));
		f = new Fog();
		f.setFogMapSize(mapSize);
		//selectPlayerNumber();
		playerList = new Player[numPlayers];
		
		
		
		for(int i = 0; i < numPlayers; i++){
			playerList[i] = new Player(m,f);
			playerList[i].setNumber(i);

			//selectPlayerColor(playerList[i]);
			playerList[i].setColor(Settings.getSettings().getPlayerColors().get(i));
			
			playerList[i].setImages();
			new Thread(playerList[i]).start();
		}
		setFocusable(true);
		startLevel();
	}
	
	public void newGame(){
		for(Player player: playerList){
			if(player.isDead()){
				player.undead();
			}
		}
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
			fisherMen[i].randomStart();
			fisherMen[i].start();
		}
		for(Player p: playerList){
			p.setStepsTaken(0);
			p.setTimesCaught(0);
			p.moveToStart(m.getStartX(), m.getStartY());
			f.createFog(p.getTileX(), p.getTileY());
		}
		
		if(colorRestore != null){
			playerList[playerNum].setColor(colorRestore);
			playerList[playerNum].setImages();
		}
		
		barrel = new Barrel(m);
		barrel.randomStart();
		barrel.start();
		
		sTime = System.currentTimeMillis();
		timer = new Timer(25, this);
		timer.start();
	}
	
	public void actionPerformed(ActionEvent e) {
		repaint();
		for(Player player : playerList){
			for(Fisherman fisherman: fisherMen){
				fisherman.isPlayerNear(player);
				if(fisherman.getCaughtPlayer() < 5 && player.getColor() != "grey" && !(fisherman.getDead())) playerList[fisherman.getCaughtPlayer()].setCaught(true);
				if(fisherman.getCaughtPlayer() < 5 && player.getColor() == "grey"){
					fisherman.requestStop();
					fisherman.setImage();
					fisherman.drawFisherman();
				}
			}
			if(m.getMap(player.getTileX(), player.getTileY()) == 'f'){
				player.setFinished(true);
				isFinished();
			}
			isPlayerCaught();
			
			barrel.isPlayerNear(player);
			if(barrel.getSharkTime()){
				colorRestore = player.getColor();
				playerNum = player.getNumber();
				
				if(!player.isDead()){
					player.setColor("grey");
					player.setImages();
					barrel.resetsharkTime();
					barrel.hide();
					barrel.requestStop();
					}
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
			if(fog[fisherman.getTileX()][fisherman.getTileY()] == 0){
				g.drawImage(fisherman.getImage(), fisherman.getX(), fisherman.getY(), null);
			}
		}
		
		if(fog[barrel.getTileX()][barrel.getTileY()] == 0 && (!barrel.isStopRequested())){
			g.drawImage(barrel.getImage(), barrel.getX(), barrel.getY(), null);}
		
		g.setColor(new Color(255,255,255));
		g.setFont(new Font("default", Font.BOLD, 16));
		g.drawString("Level: " + level, 0, 24);
		int i = 0;
		int j = 0;
		for(Player player : playerList){
			if(i == 450){
				g.drawString("P" + (player.getNumber()+1) + "-Steps: " + player.getStepsTaken() + "   Health: " + player.getHealth() + " / " + player.getMaxHealth(), 80 + j, 44);
				j += 225;
			}else{
				g.drawString("P" + (player.getNumber()+1) + "-Steps: " + player.getStepsTaken() + "   Health: " + player.getHealth() + " / " + player.getMaxHealth(), 80 + i, 24);
				i += 225;
			}
			if(!player.isDead()){
				g.drawImage(player.draw(), player.getX(), player.getY(), null);
				f.iAmHereFog(player.getTileX(), player.getTileY());
			}
			if(player.isDead() && level == player.getDeathOnLevel()){
				g.drawImage(player.draw(), player.getX(), player.getY(), null);
			}
		}
	}
	
	public void isPlayerCaught(){
		for(Player player: playerList){
			if(player.getCaught() && !player.isCaughtRecent()){
				player.setCaught(false);
				if(player.getHealth() > 5){
					JOptionPane.showMessageDialog(new JFrame(), "Player " + (player.getNumber() + 1) + " You have been caught! \nFisherman released you back into the water.");
					player.decreaseHealth();
					player.setCaughtRecent(true);
					player.getTimer(2000);
					//player.moveToStart(m.getStartX(), m.getStartY());
					
				}else if(!(player.getHealth() == 0)){
					player.died();
					player.setDeathOnLevel(level);
					deadPlayers += 1;
					if(deadPlayers == numPlayers){
						Object[] selectMenuOptions={"New Game", "Exit"};
						int n = JOptionPane.showOptionDialog(null, "Select an option", "Game Over", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, selectMenuOptions, selectMenuOptions[0]);
						switch (n) {
						case 0:
							level = 0;
							for(Player newPlayer: playerList){
								newPlayer.setHealth(50);
							}
							newGame();
							break;
						case 1:
							System.exit(0);
							break;
						}
					}
				}
			}
		}
		for(Fisherman fisherman: fisherMen){
			fisherman.resetCaughtPlayer();
		}
	}
	
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
	    	stats += "\n-----------------------\nPlayer " + (finalPlayer.getNumber()+1) + "\nSteps: " + finalPlayer.getStepsTaken() + "\nFinal Health: " + finalPlayer.getHealth();
	    }
		JOptionPane.showMessageDialog(new JFrame(), "You have won! \nLevel: " + level + "\nYour Time: " + time + stats);
		startLevel();
	}	
}
