package tests;

import static org.junit.Assert.*;

import org.junit.Test;

public class testStorageRecord {
	
	@Test
	public void testObjectsAreNullWhenFirstCreated(){
		StorageRecord record = new StorageRecord("Test Record");
		
		assertEquals(null, record.getNumberData().get(0));
		
		
		
		
	}
	
	

}
