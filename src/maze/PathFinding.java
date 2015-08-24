package maze;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

public class PathFinding implements Serializable{
	
	private static final long serialVersionUID = 2234053880326244184L;
	private Map map;
	private int[] start;
	private int[] end;
	private char moveable;
	private boolean isAlive;
	private boolean[][] checkedMap;
	
	public PathFinding(Map map, char moveableSpace, boolean isAlive) {
		this.map = map;
		this.moveable = moveableSpace;
		this.isAlive = isAlive;
		checkedMap = new boolean[map.getMapSize()][map.getMapSize()];
	}
	
	
	public Stack<int[]> findPath( int[] start, int[] getToo) {
		this.start = start;
		this.end = getToo;
		checkedMap = new boolean[map.getMapSize()][map.getMapSize()];
		
		if (start[0] == end[1] && start[1] == end[1]) {
			return null;
		}
		

		Stack<int[]> path = new Stack<>();
		path.push(new int[] {start[0], start[1], 0});
		Stack<int[]> possibleMoves = new Stack<>();
		possibleMoves = getPossibleNextPositions(path, new int[] {path.peek()[0], path.peek()[1]});
		
		Stack<Stack<int[]>> previousPossibleMoves = new Stack<>();
		Stack<Stack<int[]>> finishedPaths = new Stack<>();
		
		
		boolean flag = true;
		while (flag && isAlive) {
			
			while (!possibleMoves.isEmpty() && isAlive) {
				
				
				path.push(possibleMoves.pop());
				previousPossibleMoves.push(possibleMoves);
				possibleMoves = getPossibleNextPositions(path, new int[] {path.peek()[0], path.peek()[1]});
				
				//checkedMap[path.peek()[0]][path.peek()[1]] = true;

				// TODO remove print
//				printMapPath(path, true);
				

				if (path.peek()[0] == end[0] && path.peek()[1] == end[1]) {
					
					// TODO remove print
					//System.out.println("Path: (" + path.peek()[0] + ", " + path.peek()[1] + ")\nEnd: (" +
					//		end[0] + ", " + end[1] + ")");
					
//					finishedPaths.push(path);
//					path.pop();
					
					return path;
				}
				
			}
			if (!path.isEmpty()  && path != null) {
				checkedMap[path.peek()[0]][path.peek()[1]] = true;
				path.pop();
			}
			
			if (!isAlive) {
				break;
			}
			
			if (previousPossibleMoves.isEmpty()) {
				flag = false;
			} else {
				possibleMoves = previousPossibleMoves.pop();
			}
		}
		
		if (!isAlive) {
			return null;
		}
		
		return findOptimalPath(finishedPaths);
	}
	
	
	private Stack<int[]> findOptimalPath(Stack<Stack<int[]>> paths) {
		if (!isAlive) {
			return null;
		}
		
		if (paths.isEmpty()) {
			// TODO
//			System.out.println("paths is empty");
			return null;
		}
		
		Stack<int[]> path = paths.pop();
		int cost = calculateCost(path);
		for (Stack<int[]> p : paths) {
			int tempCost = calculateCost(p);
			if (tempCost < cost && !p.isEmpty()) {
				path = p;
				cost = tempCost;
			}
		}
		
		// TODO
//		System.out.println("Printing final path");
//		printMapPath(path, false);
		
		return path;
	}
	
	public int calculateCost(Stack<int[]> path) {
		int cost = 0;
		for (int[] p : path) {
			if (!isAlive) {
				return 0;
			}
			cost = cost + p[2];
		}
		return cost;
	}
	
	
	private void printMapPath(Stack<int[]> path, boolean showChecked) {
		
		for (int i = 0; i < map.getMapSize(); i++) {
			for (int j = 0; j < map.getMapSize(); j++) {
				if (inPath(path, new int[] {j, i}) && j == end[0] && i == end[1]) {
					System.out.print('*');
				} else if (inPath(path, new int[] {j, i})) {
					System.out.print('P');
				} else if (j == end[0] && i == end[1]) {
					System.out.print('#');
				} else {
					char l = map.getMap(j, i);
					if (l == 'g') {
						System.out.print(' ');
					} else if (checkedMap[j][i] && showChecked) {
						System.out.print('C');
					} else {
						System.out.print(l);
					}
				}
			}
			System.out.println();
		}
		System.out.println();
		
		
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	private boolean inPath(Stack<int[]> path, int[] toCheck) {
		
		if (!isAlive) {
			return false;
		}
		
		for (int[] p : path) {
			if (p[0] == toCheck[0] && p[1] == toCheck[1]) {
				return true;
			}
		}
		return false;
	}
	
	
	private Stack<int[]> getPossibleNextPositions(Stack<int[]> path, int[] pos) {
		ArrayList<int[]> unorderedPossibleLocations = new ArrayList<>();
		Stack<int[]> possibleLocations = new Stack<>();
		
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (isAlive) {
					if (checkMap(path, new int[] {pos[0] + i, pos[1] + j})) {
						if (i != 0 && j != 0) {
							unorderedPossibleLocations.add(new int[] {pos[0]+i, pos[1]+j, 1});
						} else if (i == 0 && j == 0) {
							continue;
						} else {
							unorderedPossibleLocations.add(new int[] {pos[0]+i, pos[1]+j, 2});
						}
					}
				} else {
					return null;
				}
			}
		}
		
		
		for (int i = 0; i < unorderedPossibleLocations.size() - 1; i++) {
			double distance2End = Math.sqrt(Math.pow(unorderedPossibleLocations.get(i)[0] - end[0],2) + Math.pow(unorderedPossibleLocations.get(i)[1] - end[1],2));
			for (int j = i + 1; j < unorderedPossibleLocations.size(); j++) {
				if (isAlive) {
					double tempDist = Math.sqrt(Math.pow(unorderedPossibleLocations.get(j)[0] - end[0],2) + Math.pow(unorderedPossibleLocations.get(j)[1] - end[1],2));
					if (tempDist > distance2End) {
						int[] temp = unorderedPossibleLocations.get(i);
						unorderedPossibleLocations.set(i, unorderedPossibleLocations.get(j));
						unorderedPossibleLocations.set(j, temp);
					}
				}
			}
		}
		
		for (int[] p : unorderedPossibleLocations) {
			possibleLocations.push(p);
		}
		
		return possibleLocations;
	}
	
	
	private boolean checkMap(Stack<int[]> path, int[] pos2Check) {
		
		if (!isAlive) {
			return false;
		}
		
		if (pos2Check[0] <= map.getMapSize() - 1 && pos2Check[0] >= 0 &&
				pos2Check[1] <= map.getMapSize() - 1 && pos2Check[1] >= 0 &&
				map.getMap(pos2Check[0], pos2Check[1]) == moveable &&
				!inPath(path, pos2Check) &&
				!checkedMap[pos2Check[0]][pos2Check[1]]) {
			
			return true;
		}
		
		return false;
	}
	
	

	
	private void printStackPath(Stack<Stack<int[]>> path) {
		System.out.println("Stack Path");
		if (path.isEmpty()) {
			// TODO
			System.out.println("Stack Path is empty");
			return;
		}
		for (Stack<int[]> p : path) {
			for (int[] i : p) {
				System.out.println(i[0] + "\t" + i[1]);
			}
			System.out.println();
		}
	}
	private void printPath(Stack<int[]> path) {
		System.out.println("Path");
		if (path.isEmpty()) {
			System.out.println("Path is empty");
			return;
		}
		for (int[] i : path) {
			System.out.println(i[0] + "\t" + i[1]);
		}
		System.out.println();
	}
	
	
	public void kill() {
		isAlive = false;
	}
}
