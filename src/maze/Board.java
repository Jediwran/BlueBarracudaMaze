package maze;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.ArrayList;

import javax.swing.*;

public class Board extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private Timer timer;
	private long sTime, eTime;
	private Map map;
	private Maze maze;
	private ArrayList<Player> playerList = new ArrayList<Player>();
	private ArrayList<Fisherman> fishermen = new ArrayList<Fisherman>();
	private Fog f;
	private int mapSize = 16;
	private int level = 0;
	private int deadPlayers = 0;
	private boolean fogEnabled = Settings.getSettings().getFogEnabled();
	private int numPlayers = Settings.getSettings().getNumberPlayers();
	private Barrel barrel;
	private String colorRestore;
	private int playerNum;
	private boolean pause = false;
	public static boolean run = true, first = true, refresh = false;
	public static Object monitor = new Object();
	
	public Board(Maze maze) {
		//pause until user inputs desired settings
		launchSettings();
		
		synchronized (monitor) {
			try {
				monitor.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//resume and build with user settings
		this.maze = maze;
		map = new Map();
		map.setSize(mapSize);
		maze.frame.setSize(Constants.WIDTH_REQUIRED_SPACING+(32*map.getMapSize()), Constants.HEIGHT_REQUIRED_SPACING+(32*map.getMapSize()));
		f = new Fog();
		f.setFogMapSize(mapSize);
		f.setFishSight(Settings.getSettings().getSight());
		fogEnabled = Settings.getSettings().getFogEnabled();
		numPlayers = Settings.getSettings().getNumberPlayers();
		playerList = new ArrayList<Player>(numPlayers);
		for(int i = 0; i < numPlayers; i++){
			Player player = new Player(map,f);
			player.setNumber(i);
			player.setColor(Settings.getSettings().getPlayerColors().get(i));
			player.setPrevColor();
			player.setImages();
			playerList.add(player);
		}
		setFocusable(true);
		keyBinding();
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
	
	public void startLevel(){
		level += 1;
		map.newMap(mapSize);
		
		maze.frame.setSize(Constants.WIDTH_REQUIRED_SPACING+(32*map.getMapSize()), Constants.HEIGHT_REQUIRED_SPACING+(32*map.getMapSize()));
		maze.frame.setVisible(true);
		for(Fisherman fisher:fishermen)
		{
			fisher.requestStop();
		}
		
		fishermen = new ArrayList<Fisherman>(level);
		for(int i = 0; i < level; i++){
			Fisherman fisherman = new Fisherman(map);
			fisherman.randomStart();
			fisherman.start();
			fishermen.add(fisherman);
		}
		for(Player player: playerList){
			player.setStepsTaken(0);
			player.setTimesCaught(0);
			player.moveToStart(map.getStartX(), map.getStartY());
			f.createFog(player.getTileX(), player.getTileY());
		}
		
		if(colorRestore != null){
			playerList.get(playerNum).setColor(colorRestore);
			playerList.get(playerNum).setImages();
		}
		
		barrel = new Barrel(map);
		barrel.randomStart();
		barrel.start();
		
		sTime = System.currentTimeMillis();
		timer = new Timer(25, this);
		timer.start();
	}
	
	public void restartLevel()
	{
		f.setFishSight(Settings.getSettings().getSight());
		fogEnabled = Settings.getSettings().getFogEnabled();
		numPlayers = Settings.getSettings().getNumberPlayers();
		playerList = new ArrayList<Player>(numPlayers);
		for(int i = 0; i < numPlayers; i++){
			Player player = new Player(map, f);
			player.setNumber(i);
			player.setColor(Settings.getSettings().getPlayerColors().get(i));
			player.setImages();
			playerList.add(player);
		}
		level = 0;
		startLevel();
	}
	
	public void actionPerformed(ActionEvent e) {
		if (pause)
		{
			launchSettings();
			pause = false;
			return;
		}
		
		if (!run)
		{
			return;
		}
		
		if (refresh)
		{
			restartLevel();
			refresh = false;
		}
		repaint();
		
		for(Player player : playerList){
			for(Fisherman fisherman: fishermen){
				fisherman.isPlayerNear(player);
				if(fisherman.getCaughtPlayer() < 5 && player.getColor() != "grey" && !(fisherman.getDead())) playerList.get(playerList.indexOf(player)).setCaught(true);
				if(fisherman.getCaughtPlayer() < 5 && player.getColor() == "grey"){
					fisherman.requestStop();
					fisherman.setImage();
				}
			}
			if(map.getMap(player.getTileX(), player.getTileY()) == 'f'){
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
					player.getTimer(10000);
				}
				barrel.resetsharkTime();
				barrel.hide();
				barrel.requestStop();
			}
		}
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		for(int y = 0; y < map.getMapSize(); y++) {
			for(int x = 0; x < map.getMapSize(); x++) {
				if(map.getMap(x, y) == 'g'){
					g.drawImage(map.getGround(), x * 32, y * 32, null);
				}
				if(map.getMap(x, y) == 'b'){
					g.drawImage(map.getBlock(), x * 32, y * 32, null);
				}
				if(map.getMap(x, y) == 'w'){
					g.drawImage(map.getWall(), x * 32, y * 32, null);
				}
				if(map.getMap(x, y) == 'f'){
					g.drawImage(map.getFinish(), x * 32, y * 32, null);
				}
				if(map.getMap(x, y) == 's'){
					g.drawImage(map.getStart(), x * 32, y * 32, null);
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
 		for (Fisherman fisherman : fishermen) {
			if(fog[fisherman.getTileX()][fisherman.getTileY()] == 0){
				g.drawImage(fisherman.getImage(), fisherman.getX(), fisherman.getY(), null);
			}
		}
		
		if(fog[barrel.getTileX()][barrel.getTileY()] == 0 && (!barrel.isStopRequested())){
			g.drawImage(barrel.getImage(), barrel.getX(), barrel.getY(), null);}
		
		g.setColor(new Color(255,255,255));
		g.setFont(new Font(Constants.FONT_NAME, Font.BOLD, 16));
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
			if(player.getColor().equals(Constants.GREY)){
				//g.setColor(new Color(255,0,0));
				AttributedString attrString = new AttributedString("SHARK TIME! " + player.getTimer());
				attrString.addAttribute(TextAttribute.FONT, new Font("Arial", Font.BOLD & Font.ITALIC, 18));
				attrString.addAttribute(TextAttribute.FOREGROUND, Color.YELLOW, 0 , 14);
				//g.drawString("SHARK TIME! " + player.getTimer(), 180, 80);
				g.drawString(attrString.getIterator(), 180, 60);
			}
		}
	}
	
	public void isPlayerCaught(){
		for(Player player: playerList){
			if(player.getCaught() && !player.isCaughtRecent()){
				player.setCaught(false);
				if(player.getHealth() > 5){
					//JOptionPane.showMessageDialog(new JFrame(), "Player " + (player.getNumber() + 1) + " You have been caught! \nFisherman released you back into the water.");
					player.decreaseHealth();
					player.setCaughtRecent(true);
					player.getTimer(2000);
					
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
		for (Fisherman fisherman : fishermen) {
			fisherman.resetCaughtPlayer();
		}
	}
	
	public void isFinished(){
		for(Player player : playerList){
			player.setFinished(false);
			player.setColor(player.getPrevColor());
			if(player.getShark())player.stopSharkTimer();
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
	
	private void keyBinding() {
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
	        public boolean dispatchKeyEvent(KeyEvent ke) {
	        	int keyID = ke.getID();
	        	int keyCode = ke.getKeyCode();
	        	if(keyID == KeyEvent.KEY_PRESSED){
                	switch(keyCode){
                		case KeyEvent.VK_P:
                			pause = true;
                			break;
                	}	
            	}
	        	if(!playerList.get(0).isDead()){
	            	if(keyID == KeyEvent.KEY_PRESSED){
	                	switch(keyCode){
	                		case KeyEvent.VK_UP:
	                			playerList.get(0).moveUp();
	                			break;
	                		case KeyEvent.VK_LEFT:
	                			playerList.get(0).moveLeft();
	                			break;
	                		case KeyEvent.VK_DOWN:
	                			playerList.get(0).moveDown();
	                			break;
	                		case KeyEvent.VK_RIGHT:
	                			playerList.get(0).moveRight();
	                			break;
	                	}
	            	}
	        	}
	        	if(playerList.size() > 1 && !playerList.get(1).isDead()){
	            	if(keyID == KeyEvent.KEY_PRESSED){
	                	switch(keyCode){
	                		case KeyEvent.VK_W:
	                			playerList.get(1).moveUp();
	                			break;
	                		case KeyEvent.VK_A:
	                			playerList.get(1).moveLeft();
	                			break;
	                		case KeyEvent.VK_S:
	                			playerList.get(1).moveDown();
	                			break;
	                		case KeyEvent.VK_D:
	                			playerList.get(1).moveRight();
	                			break;
	                	}
	            	}
	        	}
	        	if(playerList.size() > 2 && !playerList.get(2).isDead()){
	            	if(keyID == KeyEvent.KEY_PRESSED){
	                	switch(keyCode){
	                		case KeyEvent.VK_I:
	                			playerList.get(2).moveUp();
	                			break;
	                		case KeyEvent.VK_J:
	                			playerList.get(2).moveLeft();
	                			break;
	                		case KeyEvent.VK_K:
	                			playerList.get(2).moveDown();
	                			break;
	                		case KeyEvent.VK_L:
	                			playerList.get(2).moveRight();
	                			break;
	                	}
	            	}
	        	}
	        	if(playerList.size() > 3 && !playerList.get(3).isDead()){
	            	if(keyID == KeyEvent.KEY_PRESSED){
	                	switch(keyCode){
	                		case KeyEvent.VK_NUMPAD8:
	                			playerList.get(3).moveUp();
	                			break;
	                		case KeyEvent.VK_NUMPAD4:
	                			playerList.get(3).moveLeft();
	                			break;
	                		case KeyEvent.VK_NUMPAD2:
	                			playerList.get(3).moveDown();
	                			break;
	                		case KeyEvent.VK_NUMPAD6:
	                			playerList.get(3).moveRight();
	                			break;
	                	}
	            	}
	        	}
	        	return false;
	        }
		});
	}
	
	private void launchSettings()
	{
		run = false;
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		SwingUtilities.invokeLater(new Runnable() {
		    @Override
		    public void run() {
		        new SettingsPage().setVisible(true);
		    }
		});
	}
}
