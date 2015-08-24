package maze;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.io.*;
import java.net.*;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Random;

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
	private int mapSize = 16, level = 0, deadPlayers = 0, numPlayers, playerWithShark;
	private Random rand = new Random();
	private boolean fogEnabled, pause = false;
	private Barrel barrel;
	private String sharkColorRestore;
	private boolean server = false, solo = false;
	private ArrayList<User> users;
	private int clientID;
	private boolean finished = false;
	
	public static boolean run = true, first = true, refresh = false;
	public static Object monitor = new Object();
	
	public static ServerSocket serverSocket;
	public static Socket oSocket, iSocket;
	public static ObjectOutputStream out;
	public static ObjectInputStream in;
	
	
	public Board(Maze maze) {
		Object[] selectMenuOptions={"Host Game", "Join Game"};
		int n = JOptionPane.showOptionDialog(null, "Select an option", "Network Options", JOptionPane.DEFAULT_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, selectMenuOptions, selectMenuOptions[0]);
		
		if (n == 0)
		{
			//pause until user inputs desired settings
			launchSettings();
			
			synchronized (monitor) {
				try {
					monitor.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (numPlayers > 1)
			{
				server = true;
				users = new ArrayList<User>(numPlayers - 1);
				for (int i = 0; i < users.size(); i++)
				{
					try {
						serverSocket = new ServerSocket(7777);
						oSocket = serverSocket.accept();
						out =  new ObjectOutputStream(oSocket.getOutputStream());
						in = new ObjectInputStream(oSocket.getInputStream());
						users.add(new User(out, in));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				//resume and build with host settings
				this.maze = maze;
				map = new Map();
				mapSize = rand.nextInt(14) + 16;
				map.setSize(mapSize);
				maze.frame.setSize(Constants.WIDTH_REQUIRED_SPACING+(32*map.getMapSize()), Constants.HEIGHT_REQUIRED_SPACING+(32*map.getMapSize()));
				f = new Fog(mapSize);
				f.setFogMapSize(mapSize);
				f.setFishSight(Settings.getSettings().getSight());
				fogEnabled = Settings.getSettings().getFogEnabled();
				numPlayers = Settings.getSettings().getNumberPlayers();
				playerList = new ArrayList<Player>(numPlayers);
				for(int i = 0; i < numPlayers; i++){
					Player player = new Player();
					player.setNumber(i);
					player.setColor(Settings.getSettings().getPlayerColors().get(i));
					player.setPrevColor();
					player.setImages();
					playerList.add(player);
				}
				
				for (int i = 0; i < users.size(); i++)
				{
					try {
						users.get(i).out.writeObject(f);
						users.get(i).out.writeBoolean(fogEnabled);
						users.get(i).out.writeInt(numPlayers);
						users.get(i).out.writeObject(playerList);
						users.get(i).out.writeInt(i);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			else
			{
				this.maze = maze;
				map = new Map();
				mapSize = rand.nextInt(14) + 16;
				map.setSize(mapSize);
				maze.frame.setSize(Constants.WIDTH_REQUIRED_SPACING+(32*map.getMapSize()), Constants.HEIGHT_REQUIRED_SPACING+(32*map.getMapSize()));
				f = new Fog(mapSize);
				f.setFogMapSize(mapSize);
				f.setFishSight(Settings.getSettings().getSight());
				fogEnabled = Settings.getSettings().getFogEnabled();
				numPlayers = Settings.getSettings().getNumberPlayers();
				playerList = new ArrayList<Player>(numPlayers);
				for(int i = 0; i < numPlayers; i++){
					Player player = new Player();
					player.setNumber(i);
					player.setColor(Settings.getSettings().getPlayerColors().get(i));
					player.setPrevColor();
					player.setImages();
					playerList.add(player);
				}
				solo = true;
			}
			
		}
		else if (n == 1)
		{
			//attempt to get game settings
			try {
				iSocket = new Socket("localhost", 7777);
				out= new ObjectOutputStream(iSocket.getOutputStream());
				in =  new ObjectInputStream(iSocket.getInputStream());
				this.f = (Fog) in.readObject();
				this.fogEnabled = in.readBoolean();
				this.numPlayers = in.readInt();
				this.playerList = (ArrayList<Player>) in.readObject();
				int i = in.readInt();
				Player temp = playerList.get(0);
				playerList.set(0, playerList.get(i+1));
				playerList.set(i, temp);
				this.clientID = i;
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		barrel = new Barrel(map);
		setFocusable(true);
		keyBinding();
		startLevel();
	}
	
	public void newGame(){
		for(Player player: playerList){
			if(player.isDead()){
				player.undead();
			}else if(player.isGhostMode()){
				player.setGhostMode(false);
			}
		}
		startLevel();
	}
	
	public void startLevel(){
		
		barrel.requestStop();
		for (Fisherman f: fishermen)
		{
			f.requestStop();
		}
		
		if (server)
		{
			level += 1;
			mapSize = rand.nextInt(14) + 16;
			map.setSize(mapSize);
			map.newMap(mapSize);
			maze.frame.setSize(Constants.WIDTH_REQUIRED_SPACING+(32*map.getMapSize()), Constants.HEIGHT_REQUIRED_SPACING+(32*map.getMapSize()));
			maze.frame.setVisible(true);
			
			f = new Fog(mapSize);
			f.setFogMapSize(mapSize);
			f.setFishSight(Settings.getSettings().getSight());
			fogEnabled = Settings.getSettings().getFogEnabled();
			
			
			fishermen = new ArrayList<Fisherman>(level);
			for(int i = 0; i < level; i++){
				Fisherman fisherman = new Fisherman(map, playerList);
				fisherman.randomStart();
				fisherman.start();
				fishermen.add(fisherman);
			}
			for(Player player: playerList){
				player.setMapFog(map, f);
				player.setStepsTaken(0);
				player.setTimesCaught(0);
				if(player.isDead() && player.getDeathOnLevel() < level){
					player.ghostModeEnabled();
				}
				if(!player.isGhostMode()){
					player.moveToStart(map.getStartX(), map.getStartY());
					f.createFog(player.getTileX(), player.getTileY());
				}
				else{
					player.randomStartGhost();
					f.createFog(player.getTileX(), player.getTileY());
				}
			}
			
			if(sharkColorRestore != null){
				playerList.get(playerWithShark).setColor(sharkColorRestore);
				playerList.get(playerWithShark).setImages();
			}
			
			barrel = new Barrel(map);
			barrel.randomStart();
			barrel.start();
		
			//send data to network clients
			for (int i = 0; i < users.size(); i++)
			{
				try {
					users.get(i).out.writeObject(maze);
					users.get(i).out.writeObject(map);
					users.get(i).out.writeObject(f);
					users.get(i).out.writeObject(playerList);
					users.get(i).out.writeObject(fishermen);
					users.get(i).out.writeObject(barrel);
					users.get(i).out.writeInt(i);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		else 
		{
			if (!solo)
			{
				//pause client side calculations
				run = false;
				//get all the host data to setup the game
				try {
					this.maze = (Maze) in.readObject();
					this.map = (Map) in.readObject();
					this.f = (Fog) in.readObject();
					this.playerList = (ArrayList<Player>) in.readObject();
					this.fishermen = (ArrayList<Fisherman>) in.readObject();
					this.barrel = (Barrel) in.readObject();
					int i = in.readInt();
					Player temp = playerList.get(0);
					playerList.set(0, playerList.get(i+1));
					playerList.set(i, temp);
					this.clientID = i;
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			else
			{
				level += 1;
				mapSize = rand.nextInt(14) + 16;
				map.setSize(mapSize);
				map.newMap(mapSize);
				maze.frame.setSize(Constants.WIDTH_REQUIRED_SPACING+(32*map.getMapSize()), Constants.HEIGHT_REQUIRED_SPACING+(32*map.getMapSize()));
				maze.frame.setVisible(true);
				
				fishermen = new ArrayList<Fisherman>(level);
				for(int i = 0; i < level; i++){
					Fisherman fisherman = new Fisherman(map, playerList);
					fisherman.randomStart();
					fisherman.start();
					fishermen.add(fisherman);
				}
				for(Player player: playerList){
					player.setMapFog(map, f);
					player.setStepsTaken(0);
					player.setTimesCaught(0);
					if(player.isDead() && player.getDeathOnLevel() < level){
						player.ghostModeEnabled();
					}
					if(!player.isGhostMode()){
						player.moveToStart(map.getStartX(), map.getStartY());
						f.createFog(player.getTileX(), player.getTileY());
					}
					else{
						player.randomStartGhost();
						f.createFog(player.getTileX(), player.getTileY());
					}
				}
				
				if(sharkColorRestore != null){
					playerList.get(playerWithShark).setColor(sharkColorRestore);
					playerList.get(playerWithShark).setImages();
				}
				
				barrel = new Barrel(map);
				barrel.randomStart();
				barrel.start();
			}
		}
		
		sTime = System.currentTimeMillis();
		timer = new Timer(25, this);
		timer.start();
	}
	
	public void restartLevel()
	{
		if (server)
		{
			f.setFishSight(Settings.getSettings().getSight());
			fogEnabled = Settings.getSettings().getFogEnabled();
			numPlayers = Settings.getSettings().getNumberPlayers();
			//TODO: find a way to remove a particular player if they want to drop out
			playerList = new ArrayList<Player>(numPlayers);
			for(int i = 0; i < numPlayers; i++){
				Player player = new Player();
				player.setNumber(i);
				player.setColor(Settings.getSettings().getPlayerColors().get(i));
				player.setImages();
				playerList.add(player);
			}
			level = 0;
			
			if (!solo)
			{
				//send data to network clients
				for (int i = 0; i < users.size(); i++)
				{
					try {
						users.get(i).out.writeObject(f);
						users.get(i).out.writeBoolean(fogEnabled);
						users.get(i).out.writeInt(numPlayers);
						users.get(i).out.writeObject(playerList);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		else
		{
			try {
				this.f = (Fog) in.readObject();
				this.fogEnabled = in.readBoolean();
				this.numPlayers = in.readInt();
				this.playerList = (ArrayList<Player>) in.readObject();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		startLevel();
	}
	
	public void actionPerformed(ActionEvent e) {
		if (server || solo)
		{
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
				if(map.getMap(player.getTileX(), player.getTileY()) == 'f' && !player.isGhostMode()){
					player.setFinished(true);
					isFinished();
					finished = true;
				}
				isPlayerCaught();
				
				if(player.isGhostMode()){
					player.isGhostNearPlayer(playerList, level);
				}
				
				barrel.isPlayerNear(player);
				if(barrel.getSharkTime()){
					sharkColorRestore = player.getColor();
					playerWithShark = player.getNumber();
					sharkColorRestore = player.getColor();
				    playerWithShark = player.getNumber();
				    player.setColor("grey");
				    player.setImages();
				    player.getTimer(10000);
				    barrel.resetsharkTime();
				    barrel.hide();
				    barrel.requestStop();
				}
			}
			
			if (!solo)
			{
				//send data to network clients
				for (int i = 0; i < users.size(); i++)
				{
					try {
						users.get(i).out.writeObject(playerList);
						users.get(i).out.writeObject(fishermen);
						users.get(i).out.writeObject(barrel);
						users.get(i).out.writeBoolean(finished);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		else
		{
			try {
				this.playerList = (ArrayList<Player>) in.readObject();
				this.fishermen = (ArrayList<Fisherman>) in.readObject();
				this.barrel = (Barrel) in.readObject();
				this.finished = in.readBoolean();
				if (finished)
				{
					isFinished();
				}
			} catch (IOException | ClassNotFoundException e1) {
				e1.printStackTrace();
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
			
			f.draw(g);
			for (Fisherman fisherman : fishermen) {
				if(fog[fisherman.getTileX()][fisherman.getTileY()] == 0){
					g.drawImage(fisherman.getImage(), fisherman.getX(), fisherman.getY(), null);
				}
			}
			
			if(fog[barrel.getTileX()][barrel.getTileY()] == 0 && (!barrel.isStopRequested())){
				g.drawImage(barrel.getImage(), barrel.getX(), barrel.getY(), null);}
		} else {
			for (Fisherman fisherman : fishermen) {
				g.drawImage(fisherman.getImage(), fisherman.getX(), fisherman.getY(), null);
	 		}
			g.drawImage(barrel.getImage(), barrel.getX(), barrel.getY(), null);
		}
		
		
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
			if(player.isGhostMode()){
				g.drawImage(player.draw(), player.getX(), player.getY(), null);
			}
			if(player.getColor().equals(Constants.GREY)){
				AttributedString attrString = new AttributedString("SHARK TIME! " + player.getTimer());
				attrString.addAttribute(TextAttribute.FONT, new Font("Arial", Font.BOLD & Font.ITALIC, 18));
				attrString.addAttribute(TextAttribute.FOREGROUND, Color.YELLOW, 0 , 14);
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
					player.decreaseHealth(5);
					player.setHitRecently(true);
					player.getTimer(2000);
					
				}else if(!(player.getHealth() == 0)){
					player.died();
					player.setDeathOnLevel(level);
					deadPlayers += 1;
					if(deadPlayers >= numPlayers){
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
		if (server || solo)
		{
			for(Player player : playerList){
				player.setFinished(false);
				player.setColor(player.getPrevColor());
				if(player.getShark())player.stopSharkTimer();
			}
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
                			if (!server)
                				break;
                			pause = true;
                			break;
                	}	
            	}
	        	if(!playerList.get(0).isDead()){
	            	if(keyID == KeyEvent.KEY_PRESSED){
	            		if (server || solo)
	            		{
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
	            		else
	            		{
	            			switch(keyCode){
	                		case KeyEvent.VK_UP:
	                			try {
									out.writeObject("up");
								} catch (IOException e) {
									e.printStackTrace();
								}
	                			break;
	                		case KeyEvent.VK_LEFT:
	                			try {
									out.writeObject("left");
								} catch (IOException e) {
									e.printStackTrace();
								}
	                			break;
	                		case KeyEvent.VK_DOWN:
	                			try {
									out.writeObject("down");
								} catch (IOException e) {
									e.printStackTrace();
								}
	                			break;
	                		case KeyEvent.VK_RIGHT:
	                			try {
									out.writeObject("right");
								} catch (IOException e) {
									e.printStackTrace();
								}
	                			break;
	            			}
	            		}
	            	}
	        	}
	        	/*
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
	        	}*/
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
