package tests;

import java.util.HashMap;

import maze.Map;

/**
 * @author Kevin Gladhart
 * 
 * With dynamic map generation we need to verify it meets the requirements for a map, as defined by the games rules. 
 * This class will check that the necessary parameters for a successful map are there and will return a true or false
 * statement of success or failure.
 * 
 * There will also be a list that logs the test results for study.
 * 
 * 
 * @args - Map map 
 *
 */

public class MapAuthenticator {
	
	private Map map;
	private HashMap<String, String> allLogs;
	
	public MapAuthenticator(Map m){
		map = m;
	}

}
