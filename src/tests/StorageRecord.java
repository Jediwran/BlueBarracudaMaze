package tests;

import java.util.ArrayList;


/**
 * @author Kevin Gladhart
 * 
 * This class allows data to be saved about any test run on anything. This acts as a sort of generic way to use more than
 * just strings to store data for faster processing and even statistical measurements.
 * 
 * Each record has a unique name that cannot be changed, however you can import the data into a new record with a new name.
 * Each record also stores a string for a description about it if the name is not enough to know what it means. 
 * 
 * All text, number or objects associated with the record are aligned in three arraylists with the index representing a shared link
 * between the data. for example;
 * 		textData.get(1) -> "Outer Wall Test"
 * 		numberData.get(1) -> 100.00
 * 
 * This would mean that the outer wall test had a successful 100% rate. 
 *
 */
public class StorageRecord {
	
	private String name;
	private String description;
	private String errorLog;
	
	private ArrayList<String> textData;
	private ArrayList<Integer[]> numberData;
	private ArrayList<Object[]> objectData;
	
	private int currentRecordIndex = 0;
	// CONSTRUCTOR
	public StorageRecord(String nameOfRecordSaved){
		name = nameOfRecordSaved;
		
		textData = new ArrayList<String>();
		numberData = new ArrayList<Integer[]>();
		objectData = new ArrayList<Object[]>();
	}
	
	// =======================================

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	/**
	 * 
	 * @param testText - String data for whatever you want to store
	 * @param numberData - numbers associated with the text
	 * @param objectData - an object used for the test you want to store to access later.
	 * @return boolean success value
	 */
	public boolean addToRecord(String testText, Integer[] numberData, Object... objectData){
		
		if (recordAtIndexIsEmpty()) {
			
			addToTextData(testText);
			addToNumberData(numberData);
			
			// Check optional exists or not
			if (objectData[0] != null){
				addToObjectData(objectData);
			}
			
			currentRecordIndex++;
			return true;
		} else { 
			// THERE's SOMETHING THERE! LOOK FOR A FREE SPACE!
			int tempIndex = currentRecordIndex;
			
			while (! recordAtIndexIsEmpty() ){
				currentRecordIndex++;
			}
			// Log the error!!
			addToErrorLog("HAD TO MOVE DATA TO DIFFERENT INDEX! Semi-empty or filled values at index " + tempIndex + 
				" moved to index: " + currentRecordIndex);
			
			addToTextData(testText);
			addToNumberData(numberData);
			
			if (objectData[0] != null){
				addToObjectData(objectData);
			}
			
			
			return true;
			
			
		}
		
		
	
	}
	
	
	
	
	
	private boolean recordAtIndexIsEmpty() {
		return (textData.get(currentRecordIndex) != null && 
			this.numberData.get(currentRecordIndex) != null &&
			this.objectData.get(currentRecordIndex) != null);
	}
	
	// ADD TO METHODS ===============================================================================
	
	public void addToTextData(String text){
		textData.add(text);
	}
	
	
	public void addToNumberData(Integer[] num){
		numberData.add(num);
	}
	
	
	public void addToObjectData(Object[] o){
		objectData.add(o);
	}
	
	public void addToErrorLog(String errorMsg){
		errorLog += errorMsg + "\n";
	}
	
	// GETTERS ======================================================================================
	public String getName(){
		return name;
	}
	
	
	public String getDescription() {
		return description;
	}

	
	public ArrayList<String> getTextData() {
		return textData;
	}
	

	public ArrayList<Integer[]> getNumberData() {
		return numberData;
	}

	
	public ArrayList<Object[]> getObjectData() {
		return objectData;
	}





	

	
	

}
