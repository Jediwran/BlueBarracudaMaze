package maze;

import java.io.Serializable;

import javax.swing.*;

public class Maze implements Serializable {

	JFrame frame = new JFrame();
	
	
	public static void main(String[] args) {
		new Maze();
	}
	
	public Maze() {
		frame.setTitle(Constants.TITLE_BAR_NAME);
		frame.add(new Board(this));
		//frame.setSize(Constants.WIDTH_REQUIRED_SPACING+(32*16), Constants.HEIGHT_REQUIRED_SPACING+(32*16));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}