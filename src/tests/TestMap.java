package tests;

import static org.junit.Assert.*;
import maze.Map;

import org.junit.Test;

public class TestMap {
	
	// Difficulty loading map from test area. Added custom file open to test this. 
	@Test
	public void testGetMap(){
		Map m = new Map();
		m.setSize(14);
		//m.setMapName(1);
		//m.openCustomFile("src/resources/maps/Map1.txt");
		//m.readFile();
		//m.closeFile();
		
		assertTrue(m.getMapName().contains(".txt"));

		//System.out.println(m.printMap());
	}
	
	@Test
	public void testMapCanLoadRegardlessOfWhereItsInvoked() {
		Map m = new Map();
		m.setSize(14);
		//m.setMapName(1);
		m.setupMap();
		
		assertEquals(14, m.getMapSize());
		assertEquals("w", m.getMap(0, 0));
		assertEquals("w", m.getMap(13, 0));
		assertEquals("w", m.getMap(0, 13));
		assertEquals("w", m.getMap(13, 13));
	}

}
