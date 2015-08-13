package maze;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Settings
{
	private static Settings setting = null;
	private Boolean fogEnabled;
	private int sight;
	private int numPlayers;
	private List<String> playerColors;
	private int id;
	private static int id_counter = 0;
	private Settings()
	{
		id = ++id_counter;
		
		
		
		
	}
	public void saveSettings()
	{
		Gson gson = new GsonBuilder().create();

		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(Constants.SETTINGS_FILE));
			bw.write(gson.toJson(this));
			bw.close();
		}
		catch (IOException e1)
		{
			System.out.println("ERROR: Saving settings to gson");
			e1.printStackTrace();
		}
	}
	public static Settings getSettings()
	{
		
		if(setting != null)
		{
			return setting;
		}
		BufferedReader input = null;
		try
		{			

			 input = new BufferedReader(new FileReader(new File(Constants.SETTINGS_FILE)));
			 String setting_json = "";
			 while(input.ready())
			 {
				 setting_json = input.readLine();
			 }
			 Gson gson = new GsonBuilder().create();
			 setting = gson.fromJson(setting_json, Settings.class);
			
			 if(setting.fogEnabled == null || setting.sight < 0 || setting.sight > 20 || setting.numPlayers < 1 || setting.numPlayers > 4 || setting.playerColors.size() != 4)
			 {
				 //System.out.println("Invalid data in settings So using default values");
				 throw new Exception();
			 }
			 try
				{
					input.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			 
		} 
	 	catch (Exception e) 
		{
			//initiliaze values and save
	 		setting = new Settings();
	 		setting.fogEnabled = true;
	 		setting.sight = 2;
	 		setting.numPlayers = 1;
	 		setting.playerColors = new ArrayList<>();
	 		setting.playerColors.add(Constants.BLUE);
	 		setting.playerColors.add(Constants.ORANGE);
	 		setting.playerColors.add(Constants.GREEN);
	 		setting.playerColors.add(Constants.PURPLE);
	 		setting.saveSettings();
			
		}
		finally
		{
			
		}
		
		return setting;
	}
	public boolean getFogEnabled()
	{
		return fogEnabled;
	}
	public void setFogEnabled(boolean fogEnabled)
	{
		this.fogEnabled = fogEnabled;
	}
	public int getSight()
	{
		return sight;
	}
	public void setSight(int sight)
	{
		if(sight < 0 || sight > 20)
		{
			this.sight = 2;
		}
		else
		{
			this.sight = sight;
		}
	}
	public int getNumberPlayers()
	{
		return numPlayers;
	}
	public void setNumberPlayers(int count)
	{
		if(count < 1 || count > 20)
		{
			numPlayers = 1;
		}
		else if(count > 4)
		{
			numPlayers = 4;
		}
		else
		{
			numPlayers = count;
		}
	}
	public List<String> getPlayerColors()
	{
		return playerColors;
	}
	public void setPlayerColors(List<String> colors)
	{
		if(colors.size() != 4)
		{
			return;
		}
		for(String color:colors)
		{
			if(color.compareTo(Constants.BLUE) == 0 || color.compareTo(Constants.ORANGE) == 0 ||color.compareTo(Constants.GREEN) == 0 ||color.compareTo(Constants.PURPLE) == 0)
			{
				continue;
			}
			else
			{
				//System.out.println("Invalid color: "+ color);
				return;
			}
		}
		playerColors = colors;
		//System.out.println("Colors were assigned to playercolors");
	}
	@Override
	public String toString()
	{
		String output = "" + id + "/" + id_counter + "\n";
		output += "FogEnabled: " + fogEnabled + "\n";
		output += "sight: " + sight + "\n";
		output += "numPlayers: " + numPlayers + "\n";
		for(int i = 0; i < 4; i++)
		{
			output += "player color " + (i + 1) + ": " + playerColors.get(i) + "\n";
		}
		return output;
	}
}
