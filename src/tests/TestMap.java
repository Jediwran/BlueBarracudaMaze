package tests;

import static org.junit.Assert.*;
import maze.Map;

import org.junit.Test;

public class TestMap {
	
	@Test
	public void testGetMap(){
		Map m = new Map();
		
		assertEquals("Map2.txt", m.getMapName());

		System.out.println(m.printMap());
	}

}
