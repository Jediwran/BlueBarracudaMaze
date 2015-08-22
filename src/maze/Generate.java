package maze;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;


public class Generate {	
	
	public static final char WALL = 'w';
	public static final char EDGE = 'e';
	public static final char SPACE = 'g';
	public static final char START = 's';
	public static final char END = 'f';
	public static final char BORDER = 'b';
	
	private static char[][] maze;
	
	private static Stack<int[]> previous;
	private static ArrayList<int[]> ends;
	
	private static int sizeX;
	private static int sizeY;
	private static Random randy;
	private static boolean straighter = true;
	private static int numOfHoles;
	
	
	public static char[][] newMaze(int size, int holes, boolean...bs) {
		sizeX = size;
		sizeY = size;
		numOfHoles = holes;
		if (bs.length > 0) {
			straighter = bs[0];
		}

		start();
		
		return maze;
	}
	
	public static char[][] newMaze(int x, int y, int holes, boolean...bs) {
		sizeX = x;
		sizeY = y;
		numOfHoles = holes;
		if (bs.length > 0) {
			straighter = bs[0];
		}
		
		start();
		
		return maze;
	}
	
	public static void start() {
		maze = new char[sizeY][sizeX];
		previous = new Stack<int[]>();
		ends = new ArrayList<>();
		randy = new Random();
		
		for (int i = 0; i < sizeY; i++) {
			for (int j = 0; j < sizeX; j++) {
				maze[i][j] = WALL;
				maze[2][j] = EDGE;
				maze[sizeY-1][j] = EDGE;
				maze[0][j] = BORDER;
				maze[1][j] = BORDER;
			}
			
			maze[i][0] = EDGE;
			maze[i][sizeX-1] = EDGE;
//			maze[i][0] = BORDER;
//			maze[i][sizeX-1] = BORDER;
		}
		int[] start = new int[2];
		
		start[0] = randy.nextInt(sizeY-4) + 3;
		start[1] = randy.nextInt(sizeX-2) + 1;
		
		
		if (straighter) {
			generateStaighter(start);
		} else {
			generate(start);
		}
		finishUp(start);
	}
	
	private static void generateStaighter(int[] pos) {		
		boolean flag = true;
		ArrayList<Integer> directions = new ArrayList<>();
		directions.add(0);
		directions.add(1);
		directions.add(2);
		directions.add(3);
		
		maze[pos[0]][pos[1]] = SPACE;
		previous.push(pos);
		
		while (!previous.empty()) {
			flag = true;
			ArrayList<Integer> dirs = (ArrayList<Integer>) directions.clone();
			while(!dirs.isEmpty()) {
				int dir = dirs.remove(randy.nextInt(dirs.size()));
				int counter = 4;
				while (counter > 0) {
					if (isGood(pos, dir)) {
						pos = updatePos(pos, dir);
						maze[pos[0]][pos[1]] = SPACE;
						previous.push(pos);
						flag = false;
						//break;
					}
					counter--;
				}
			}
			if (flag) {
				ends.add(pos);
				pos = previous.pop();
			}
			
			
			
			//print(maze);
			
		}
	}
	
	private static void generate(int[] pos) {
		
		boolean flag = true;
		ArrayList<Integer> directions = new ArrayList<>();
		directions.add(0);
		directions.add(1);
		directions.add(2);
		directions.add(3);
		
		maze[pos[0]][pos[1]] = SPACE;
		previous.push(pos);
		
		while (!previous.empty()) {
			flag = true;
			ArrayList<Integer> dirs = (ArrayList<Integer>) directions.clone();
			while(!dirs.isEmpty()) {
				int dir = dirs.remove(randy.nextInt(dirs.size()));
				if (isGood(pos, dir)) {
					pos = updatePos(pos, dir);
					maze[pos[0]][pos[1]] = SPACE;
					previous.push(pos);
					flag = false;
					break;
				}
			}
			if (flag) {
				ends.add(pos);
				pos = previous.pop();
			}
			
			
			
			//print(maze);
		}
	}
	

	
	/**
	 *direction is based on 0:up, 1:right, 2:down, 3:left
	 */
	private static int[] updatePos(int[] pos, int dir) {
		int[] newPos = pos.clone();
		
		switch (dir) {
		case 0:
			newPos[1] = newPos[1] - 1;
			break;
		case 1:
			newPos[0] = newPos[0] + 1;
			break;
		case 2:
			newPos[1] = newPos[1] + 1;
			break;
		case 3:
			newPos[0] = newPos[0] - 1;
			break;
		}
		return newPos;
	}
	
	private static boolean isGood(int[] pos, int dir) {
		switch (dir) {
		case 0:
			//System.out.println(maze[pos[0]][pos[1] - 1]);
			if (maze[pos[0]][pos[1] - 1] == WALL) {
				//System.out.println(maze[pos[0]][pos[1] - 2] + "   " + maze[pos[0] + 1][pos[1] - 1] + "   " + maze[pos[0] - 1][pos[1] - 1]);
				if (maze[pos[0]][pos[1] - 2] != SPACE && maze[pos[0] + 1][pos[1] - 1] != SPACE && maze[pos[0] - 1][pos[1] - 1] != SPACE) {
					return true;
				}
			}
			break;
		case 1:
			if (maze[pos[0] + 1][pos[1]] == WALL) {
				if (maze[pos[0] + 2][pos[1]] != SPACE && maze[pos[0] + 1][pos[1] + 1] != SPACE && maze[pos[0] + 1][pos[1] - 1] != SPACE) {
					return true;
				}
			}
			break;
		case 2:
			if (maze[pos[0]][pos[1] + 1] == WALL) {
				if (maze[pos[0]][pos[1] + 2] != SPACE && maze[pos[0] + 1][pos[1] + 1] != SPACE && maze[pos[0] - 1][pos[1] + 1] != SPACE) {
					return true;
				}
			}
			break;
		case 3:
			if (maze[pos[0] - 1][pos[1]] == WALL) {
				if (maze[pos[0] - 2][pos[1]] != SPACE && maze[pos[0] - 1][pos[1] + 1] != SPACE && maze[pos[0] - 1][pos[1] - 1] != SPACE) {
					return true;
				}
			}
			break;
		}
		
		return false;
	}
	
	
	private static void addRandomHoles() {
		boolean flag;
		for (int holes = 0; holes < numOfHoles; holes++) {
			flag = true;
			while (flag) {
			
				int x = randy.nextInt(sizeX - 2) + 1;
				int y = randy.nextInt(sizeY - 2) + 1;

				if (maze[y][x] == WALL) {
					maze[y][x] = SPACE;
					flag = false;
				}
			}
		}
	}
	
	private static void finishUp(int[] start) {
		addRandomHoles();
		maze[start[0]][start[1]] = START;
		int[] end = ends.remove(randy.nextInt(ends.size()));
		
		while (Math.sqrt(Math.pow((end[1] - start[1]),2) + Math.pow((end[0] - start[1]),2)) < Math.sqrt((sizeX*sizeX) + (sizeY*sizeY))/3) {
			end = ends.remove(randy.nextInt(ends.size()));
		}
		
		maze[end[0]][end[1]] = END;
		
		
		for (int i = 0; i < sizeY; i++) {
			for (int j = 0; j < sizeX; j++) {
				if (maze[i][j] == EDGE) {
					maze[i][j] = WALL;
				}
			}
		}
	}
	
	public static void print(char[][] mze) {
		for (char[] x : mze) {
			for (char y : x) {
				System.out.print(y);
			}
			System.out.println();
		}
		
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
