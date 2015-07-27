package maze;

import javax.swing.*;

public class Maze {

	JFrame frame = new JFrame();
	public static String title = "Maze Game";
	public static String version = "Alpha 0.1";
	public static int width = 480;
	public static int height = 467;
	
	public static void main(String[] args) {
		new Maze();
	}
	
	public Maze() {
		frame.setTitle(title + " - " + version);
		frame.add(new Board());
		frame.setSize(467, 480);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}