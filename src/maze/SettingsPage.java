package maze;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

public class SettingsPage extends JFrame
{
	private JLabel fogLabel = new JLabel("Fog Enabled: ");
    private JLabel sightLabel = new JLabel("Sight range: ");
    private JLabel numberOfPlayersLabel = new JLabel("Number of players: ");
    private JLabel colorPlayer1 = new JLabel("Player 1: ");
    private JLabel colorPlayer2 = new JLabel("Player 2: ");
    private JLabel colorPlayer3 = new JLabel("Player 3: ");
    private JLabel colorPlayer4 = new JLabel("Player 4: ");
    
    private JTextField textUsername = new JTextField(20);
    private JFormattedTextField sightField = null;
    private JButton buttonSave = new JButton("Save");
    ButtonGroup fog = new ButtonGroup();
    
    ButtonGroup playerNumberGroup = new ButtonGroup();
    ButtonGroup fish1 = new ButtonGroup();
    ButtonGroup fish2 = new ButtonGroup();
    ButtonGroup fish3 = new ButtonGroup();
    ButtonGroup fish4 = new ButtonGroup();
 
    String blueFish = Constants.FISH_LEFT_BLUE_IMAGE;
    String orangeFish = Constants.FISH_LEFT_ORANGE_IMAGE;
    String greenFish = Constants.FISH_LEFT_GREEN_IMAGE;
    String purpleFish = Constants.FISH_LEFT_PURPLE_IMAGE;
    JPanel mainPanel = new JPanel(new GridBagLayout());

    Settings settings = null;

	public SettingsPage() 
    {
    	
        super("Game Settings");
    	settings = Settings.getSettings();
    	settings.toString();
        // create a new panel with GridBagLayout manager
         
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);
        int y = 0;
        // add components to the panel
        constraints.gridx = 0;
        constraints.gridy = y;     
        mainPanel.add(fogLabel, constraints);
 
        constraints.gridx = 1;
        JPanel fogPanel = new JPanel();
        createFogGroup(fog, fogPanel,settings.getFogEnabled());
        mainPanel.add(fogPanel, constraints);
         
        constraints.gridx = 0;
        constraints.gridy = ++y;     
        mainPanel.add(sightLabel, constraints);
         
        constraints.gridx = 1;
        setupSightField(settings.getSight());
        mainPanel.add(sightField, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = ++y;     
        mainPanel.add(numberOfPlayersLabel, constraints);
 
        constraints.gridx = 1;
        JPanel playerNumberPanel = new JPanel();
        createPlayerNumberGroup(playerNumberGroup, playerNumberPanel,settings.getNumberPlayers());
        mainPanel.add(playerNumberPanel, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = ++y;     
        mainPanel.add(colorPlayer1, constraints);
	        
        constraints.gridx = 1;
        JPanel sidePanel = new JPanel(new GridBagLayout());
	    createPlayerColorGroup(fish1, sidePanel,settings.getPlayerColors().get(0));    

	    mainPanel.add(sidePanel,constraints);        
        
        constraints.gridx = 0;
        constraints.gridy = ++y;     
        mainPanel.add(colorPlayer2, constraints);
 
        constraints.gridx = 1;
	    JPanel sidePanel2 = new JPanel(new GridBagLayout());
        createPlayerColorGroup(fish2, sidePanel2,settings.getPlayerColors().get(1));
        mainPanel.add(sidePanel2,constraints);
                
        constraints.gridx = 0;
        constraints.gridy = ++y;     
        mainPanel.add(colorPlayer3, constraints);
 
        constraints.gridx = 1;
	    JPanel sidePanel3 = new JPanel(new GridBagLayout());
        createPlayerColorGroup(fish3, sidePanel3,settings.getPlayerColors().get(2));
        mainPanel.add(sidePanel3,constraints);
 
        constraints.gridx = 0;
        constraints.gridy = ++y;     
        mainPanel.add(colorPlayer4, constraints);
 
        constraints.gridx = 1;
	    JPanel sidePanel4 = new JPanel(new GridBagLayout());
        createPlayerColorGroup(fish4, sidePanel4,settings.getPlayerColors().get(3));
        mainPanel.add(sidePanel4,constraints);
        
        constraints.gridx = 0;
        constraints.gridy = ++y;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        
        buttonSave.addActionListener(new ActionListener(){
        	@Override
        	public void actionPerformed(ActionEvent event)
        	{
        		settings.setFogEnabled(((JRadioButton)getSelectedButton(fog)).getName().equalsIgnoreCase("YES") ?true:false);
        		settings.setSight((int)sightField.getValue());

        		if(((JRadioButton)getSelectedButton(playerNumberGroup)).getName().equalsIgnoreCase("ONE"))
        		{
        			settings.setNumberPlayers(1);
        		}
        		else if(((JRadioButton)getSelectedButton(playerNumberGroup)).getName().equalsIgnoreCase("TWO"))
        		{
        			settings.setNumberPlayers(2);
        		}
        		else if(((JRadioButton)getSelectedButton(playerNumberGroup)).getName().equalsIgnoreCase("THREE"))
        		{
        			settings.setNumberPlayers(3);
        		}
        		else if(((JRadioButton)getSelectedButton(playerNumberGroup)).getName().equalsIgnoreCase("FOUR"))
        		{
        			settings.setNumberPlayers(4);
        		}

        		List<String> colors = new ArrayList<>();        		
        		colors.add(((JRadioButton)getSelectedButton(fish1)).getName());
        		colors.add(((JRadioButton)getSelectedButton(fish2)).getName());
        		colors.add(((JRadioButton)getSelectedButton(fish3)).getName());
        		colors.add(((JRadioButton)getSelectedButton(fish4)).getName());
        		settings.setPlayerColors(colors);
        		settings.saveSettings();
        		
        		exit();
        	}
        });
        mainPanel.add(buttonSave, constraints);
        
        // set border for the panel
        mainPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Settings Panel"));
         
        // add the panel to this frame
        add(mainPanel);
         
        pack();
        setLocationRelativeTo(null);
    }
  
    public void exit()
    {
    	//this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    	this.setVisible(false);
    	this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
    public void setupSightField(int sightRange)
    {
    	MaskFormatter formatter = null;
	    try {
	        formatter = new MaskFormatter("#");
	    } catch (java.text.ParseException exc) {
	        System.err.println("formatter is bad: " + exc.getMessage());
	        System.exit(-1);
	    }    
	    sightField = new JFormattedTextField(formatter);
    	sightField.setValue((int)sightRange);
    	sightField.setColumns(3);
    
    }
    	 
    public void createFogGroup(ButtonGroup bg, JPanel panel,boolean fogEnabled)
    {
        JRadioButton yesButton = new JRadioButton("Yes");
        JRadioButton noButton = new JRadioButton("No");
        yesButton.setName("YES");
        noButton.setName("NO");
        if(fogEnabled)
        {
        	yesButton.setSelected(true);
        	noButton.setSelected(false);
        }
        else
        {
        	yesButton.setSelected(false);
        	noButton.setSelected(true);        	
        }
        bg.add(yesButton);
        bg.add(noButton);
        
		GridBagConstraints constriants = new GridBagConstraints();
		constriants.anchor = GridBagConstraints.WEST;
		constriants.insets = new Insets(10, 10, 10, 10);
		constriants.gridx = 0;
		constriants.gridy = 0;
		panel.add(yesButton, constriants);
		constriants.gridx = 1;
		
		panel.add(noButton, constriants);
    }
    public void createPlayerNumberGroup(ButtonGroup bg, JPanel panel,int number)
    {
        JRadioButton oneButton = new JRadioButton("1");
        JRadioButton twoButton = new JRadioButton("2");
        JRadioButton threeButton = new JRadioButton("3");
        JRadioButton fourButton = new JRadioButton("4");
        oneButton.setName("ONE");
        twoButton.setName("TWO");
        threeButton.setName("THREE");
        fourButton.setName("FOUR");
        if(number == 1)
        {
        	oneButton.setSelected(true);
        }
        else if(number  == 2)
        {
        	twoButton.setSelected(true);
        }
        else if(number == 3)
        {
        	threeButton.setSelected(true);
        }
        else if(number ==4)
        {
        	fourButton.setSelected(true);
        }
        else
        {
        	oneButton.setSelected(true);
        }
         
        bg.add(oneButton);
        bg.add(twoButton);
        bg.add(threeButton);
        bg.add(fourButton);
        
		GridBagConstraints constriants = new GridBagConstraints();
		constriants.anchor = GridBagConstraints.WEST;
		constriants.insets = new Insets(10, 10, 10, 10);
		constriants.gridx = 0;
		constriants.gridy = 0;
		panel.add(oneButton, constriants);
		constriants.gridx = 1;
		
		panel.add(twoButton, constriants);
		constriants.gridx = 2;
		
		panel.add(threeButton, constriants);
		constriants.gridx = 3;
		
		panel.add(fourButton, constriants);
        
    }
    public void createPlayerColorGroup(ButtonGroup bg, JPanel panel,String color)
    {
        JRadioButton blueButton = new JRadioButton(new ImageIcon(blueFish));
        JRadioButton orangeButton = new JRadioButton(new ImageIcon(orangeFish));
        JRadioButton greenButton = new JRadioButton(new ImageIcon(greenFish));
        JRadioButton purpleButton = new JRadioButton(new ImageIcon(purpleFish));
        blueButton.setBorderPainted(true);
        orangeButton.setBorderPainted(true);
        greenButton.setBorderPainted(true);
        purpleButton.setBorderPainted(true);
        blueButton.setName("BLUE");
        orangeButton.setName("ORANGE");
        greenButton.setName("GREEN");
        purpleButton.setName("PURPLE");
        bg.add(blueButton);
        bg.add(orangeButton);
        bg.add(greenButton);
        bg.add(purpleButton);
        
        if(color.equalsIgnoreCase(Constants.BLUE))
        {
        	blueButton.setSelected(true);
        }
        else if(color.equalsIgnoreCase(Constants.ORANGE))
        {
        	orangeButton.setSelected(true);
        }
        else if(color.equalsIgnoreCase(Constants.GREEN))
        {
        	greenButton.setSelected(true);
        }
        else if(color.equalsIgnoreCase(Constants.PURPLE))
	    {
	    	purpleButton.setSelected(true);
	    }
        else
        {
        	blueButton.setSelected(true);
        }

        
        
		GridBagConstraints constriants = new GridBagConstraints();
		constriants.anchor = GridBagConstraints.WEST;
		constriants.insets = new Insets(10, 10, 10, 10);
		constriants.gridx = 0;
		constriants.gridy = 0;
		panel.add(blueButton, constriants);
		constriants.gridx = 1;
		
		panel.add(orangeButton, constriants);
		constriants.gridx = 2;
		
		panel.add(greenButton, constriants);
		constriants.gridx = 3;
		
		panel.add(purpleButton, constriants);
        
    }
    public AbstractButton getSelectedButton(ButtonGroup bg)
    {
    	for(Enumeration<AbstractButton> button_list = bg.getElements();button_list.hasMoreElements();)
    	{
    		AbstractButton button = button_list.nextElement();
    		if(button.isSelected())
    		{
    			return button;
    		}
    	}
    	return null;
    }
}
