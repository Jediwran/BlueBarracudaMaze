package maze;

import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Player extends JPanel implements Runnable, ActionListener {

	private int x, y, tileX, tileY, number;
	private Image leftImage, downImage, rightImage, upImage;
	private String leftFile = "src/resources/fish_left_";
	private String downFile = "src/resources/fish_down_";
	private String rightFile = "src/resources/fish_right_";
	private String upFile = "src/resources/fish_up_";
	private int lives = 5;
	private int stepsTaken = 0;
	private int timesCaught = 0;
	private int direction = 3;
	private String color;
	private Map m;
	private Fog f;
	private boolean caught = false;
	private boolean isAtFinish = false;
		
	public Player(Map m, Fog f) {
		this.m = m;
		this.f = f;
		keyBindings();
	}
	
	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getTimesCaught() {
		return timesCaught;
	}

	public void setTimesCaught(int timesCaught) {
		this.timesCaught = timesCaught;
	}

	public int getPlayerStepsTaken() {
		return stepsTaken;
	}

	public void setPlayerStepsTaken(int playerStepsTaken) {
		this.stepsTaken = playerStepsTaken;
	}

	public int getPlayerLives() {
		return lives;
	}

	public void setPlayerLives(int playerLives) {
		this.lives = playerLives;
	}

	public void setPlayerImages(){
		ImageIcon img = new ImageIcon(leftFile + color + ".png");
		leftImage = img.getImage();
		img = new ImageIcon(downFile + color + ".png");
		downImage = img.getImage();
		img = new ImageIcon(rightFile + color + ".png");
		rightImage = img.getImage();
		img = new ImageIcon(upFile + color + ".png");
		upImage = img.getImage();
	}
	
	public void setPlayerStart(int tX, int tY) {
		tileX = tX;
		tileY = tY;
		
		//Normal 32x32 character
		x = tX * 32;
		y = tY * 32;
		
		//Character with black around him
		//x = tX * 32 - 783;
		//y = tY * 32 - 784;
	}
	
	public Image getPlayerLeft() {
		return leftImage;
	}
	
	public Image getPlayerDown(){
		return downImage;
	}
	
	public Image getPlayerRight(){
		return rightImage;
	}
	
	public Image getPlayerUp(){
		return upImage;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getTileX() {
		return tileX;
	}
	
	public int getTileY() {
		return tileY;
	}
	
	public void setNumber(int num) {
		number = num;
	}
	
	public int getNumber() {
		return number;
	}
	
	public void move(int dx, int dy, int tx, int ty) {
		x += dx;
		y += dy;
		
		tileX += tx;
		tileY += ty;
	}
	
	public Image drawPlayer(){
		Image playerImage = null;
		switch(direction){
			case 0:{
				playerImage = getPlayerUp();
				break;
			}
			case 1:{
				playerImage = getPlayerRight();
				break;
			}
			case 2:{
				playerImage = getPlayerDown();
				break;
			}
			case 3:{
				playerImage = getPlayerLeft();
				break;
			}
		}
		return playerImage;
	}
	
	public void movePlayerUp(){
		if(m.getMap(getTileX(), getTileY() - 1) != 'w') {
			f.reFog(getTileX(), getTileY(), "U");
			move(0, -32, 0, -1);
			setDirection(0);
			setPlayerStepsTaken(getPlayerStepsTaken() + 1);
			
			f.iAmHereFog(getTileX(), getTileY());
			//isFinish(p);
		}
	}
	
	public void movePlayerDown(){
		if(m.getMap(getTileX(), getTileY() + 1) != 'w') {
			f.reFog(getTileX(), getTileY(), "D");

			move(0, 32, 0, 1);
			setDirection(2);
			setPlayerStepsTaken(getPlayerStepsTaken() + 1);
			f.iAmHereFog(getTileX(), getTileY());
			//isFinish(p);
		}
	}
	
	public void movePlayerLeft(){
		if(m.getMap(tileX - 1, tileY) != 'w') {
			f.reFog(tileX, tileY, "L");
			move(-32, 0, -1, 0);
			setDirection(3);
			setPlayerStepsTaken(getPlayerStepsTaken() + 1);
			f.iAmHereFog(tileX, tileY);
			//isFinish(p);
		}
	}
	
	public void movePlayerRight(){
		if(m.getMap(getTileX() + 1, getTileY()) != 'w') {
			f.reFog(tileX, tileY, "R");
			move(32, 0, 1, 0);
			setDirection(1);
			setPlayerStepsTaken(getPlayerStepsTaken() + 1);
			f.iAmHereFog(getTileX(), getTileY());
			//isFinish(p);
		}
	}

	@Override
	public void run() {
		//JComponent component = null;
		while(true){
			/*KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
				
				@Override
	            public boolean dispatchKeyEvent(KeyEvent ke) {
	                synchronized (IsKeyPressed.class) {
	                    switch (ke.getID()) {
	                    case KeyEvent.KEY_PRESSED:
	                        if (ke.getKeyCode() == KeyEvent.VK_W) {
	                            wPressed = true;
	                        }
	                        break;

	                    case KeyEvent.KEY_RELEASED:
	                        if (ke.getKeyCode() == KeyEvent.VK_W) {
	                            wPressed = false;
	                        }
	                        break;
	                    }
	                    return false;
	                }
	            }
			});
			
			System.out.println(wPressed);*/
			//Action anAction = new Action();
			//anAction.setEnabled(true);
			//TestAction testAction = new TestAction("MoveUp", null, "Moving player up", new Integer(KeyEvent.VK_W));
			/*Action doSomething = new AbstractAction(){
				public void actionPerformed(ActionEvent e){
					System.out.println("Pressed W");
				}
			};
			getInputMap().put(KeyStroke.getKeyStroke("W"),"doSomething");
			getActionMap().put("doSomething", doSomething);*/
			/*KeyStroke test = (KeyStroke) getActionForKeyStroke(KeyStroke.getKeyStroke("ctrl CONTROL"));
			if(test != null){
				System.out.println(test);
			}*/
			//addKeyListener(new Al());
			//component.getInputMap().put(KeyStroke.getKeyStroke("S"), "pressed");
			//System.out.println(KeyStroke.getKeyStroke("W"));		
		}
	}
	
	/*public class IsKeyPressed {
		public boolean isWPressed() {
			synchronized(IsKeyPressed.class){
				return wPressed;
			}
		}
		public boolean isUpPressed() {
			synchronized(IsKeyPressed.class){
				return upPressed;
			}
		}
	}*/
	
	public void keyBindings() {
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			
			@Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                //synchronized (IsKeyPressed.class) {
                	int keyID = ke.getID();
                	int keyCode = ke.getKeyCode();
                	if(keyID == KeyEvent.KEY_PRESSED && number == 0){
	                	switch(keyCode){
	                		case KeyEvent.VK_UP:
	                			movePlayerUp();
	                			break;
	                		case KeyEvent.VK_LEFT:
	                			movePlayerLeft();
	                			break;
	                		case KeyEvent.VK_DOWN:
	                			movePlayerDown();
	                			break;
	                		case KeyEvent.VK_RIGHT:
	                			movePlayerRight();
	                			break;
	                	}
                	}else if(keyID == KeyEvent.KEY_PRESSED && number == 1){
        				switch(keyCode){
    					case KeyEvent.VK_W:
    							movePlayerUp();
    						break;
    					case KeyEvent.VK_A:
    							movePlayerLeft();
    						break;
    					case KeyEvent.VK_S:
    							movePlayerDown();
    						break;
    					case KeyEvent.VK_D:
    							movePlayerRight();
    						break;
    				}
    			}else if(keyID == KeyEvent.KEY_PRESSED && number == 2){
    				switch(keyCode) {
    					case KeyEvent.VK_I:
    							movePlayerUp();
    						break;
    					case KeyEvent.VK_J:
    							movePlayerLeft();
    						break;
    					case KeyEvent.VK_K:
    							movePlayerDown();
    						break;
    					case KeyEvent.VK_L:
    							movePlayerRight();
    						break;
    				}
    			}else if(keyID == KeyEvent.KEY_PRESSED && number == 3){
    				switch(keyCode){
    					case KeyEvent.VK_NUMPAD8:
    							movePlayerUp();
    						break;
    					case KeyEvent.VK_NUMPAD4:
    							movePlayerLeft();
    						break;
    					case KeyEvent.VK_NUMPAD2:
    							movePlayerDown();
    						break;
    					case KeyEvent.VK_NUMPAD6:
    							movePlayerRight();
    						break;
    				}
    			//}
                    /*switch (ke.getID()) {
                    case KeyEvent.KEY_PRESSED:
                        if (ke.getKeyCode() == KeyEvent.VK_W) {
                            wPressed = true;
                        }
                        break;

                    case KeyEvent.KEY_RELEASED:
                        if (ke.getKeyCode() == KeyEvent.VK_W) {
                            wPressed = false;
                        }
                        break;
                    }*/
                    //return false;
                }
				return false;
            }
		});
		
	}
	
	public boolean isCaught(Fisherman fisherMan){
		if((tileX == fisherMan.getFishermanTileX() + 1 || tileX == fisherMan.getFishermanTileX() - 1) && tileY == fisherMan.getFishermanTileY()){
			caught = true;
		}else if((tileY == fisherMan.getFishermanTileY() + 1 || tileY == fisherMan.getFishermanTileY() - 1 ) && tileX == fisherMan.getFishermanTileX()){
			caught = true;
		}else caught = false;
		
		return caught;
	}
	
	public void isAtFinish(){
		if(m.getMap(tileX, tileY) == 'f'){
			isAtFinish = true;
		}
	}

	public void setFinished(boolean value){
		isAtFinish = value;
	}
	
	public boolean getIsAtFinish(){
		return isAtFinish;
	}
	
	public class Al extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int keycode = e.getKeyCode();
			System.out.println("Key Pressed!");
			if(number == 0){
				switch(keycode){
					case KeyEvent.VK_UP:
						movePlayerUp();
						break;
					case KeyEvent.VK_LEFT:
						movePlayerLeft();
						break;
					case KeyEvent.VK_DOWN:
						movePlayerDown();
						break;
					case KeyEvent.VK_RIGHT:
						movePlayerRight();
						break;
				}
			}else if(number == 1){
				switch(keycode){
					case KeyEvent.VK_W:
							movePlayerUp();
						break;
					case KeyEvent.VK_A:
							movePlayerLeft();
						break;
					case KeyEvent.VK_S:
							movePlayerDown();
						break;
					case KeyEvent.VK_D:
							movePlayerRight();
						break;
				}
			}else if(number == 2){
				switch(keycode) {
					case KeyEvent.VK_I:
							movePlayerUp();
						break;
					case KeyEvent.VK_J:
							movePlayerLeft();
						break;
					case KeyEvent.VK_K:
							movePlayerDown();
						break;
					case KeyEvent.VK_L:
							movePlayerRight();
						break;
				}
			}else{
				switch(keycode){
					case KeyEvent.VK_NUMPAD8:
							movePlayerUp();
						break;
					case KeyEvent.VK_NUMPAD4:
							movePlayerLeft();
						break;
					case KeyEvent.VK_NUMPAD2:
							movePlayerDown();
						break;
					case KeyEvent.VK_NUMPAD6:
							movePlayerRight();
						break;
				}
			}
		}
		
		public void keyReleased(KeyEvent e) {
			
		}
		
		public void keyTyped(KeyEvent e) {
			
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
				
	}
}