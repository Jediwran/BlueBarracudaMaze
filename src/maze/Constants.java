package maze;

public class Constants {
	//Game information
	public static final String GAME_TITLE = "Maze Game";
	public static final String GAME_VERSION = "Alpha 0.2";
	public static final String TITLE_BAR_NAME = GAME_TITLE + " - " + GAME_VERSION;
	
	//Default spacing due to window size
	public static final int WIDTH_REQUIRED_SPACING = 20;
	public static final int HEIGHT_REQUIRED_SPACING = 48;
	public static final String FONT_NAME = "default";
	
	//Settings file and location
	public static final String SETTINGS_FILE = "src/resources/setting.config";
	
	//Barrel image
	public static final String BARREL_IMAGE = "src/resources/images/barrel.png";
	
	//Fisherman image
	public static final String FISHERMAN_IMAGE = "src/resources/images/fisherman.png";
	
	//Dead fisherman image
	public static final String FISHERMAN_DEAD_IMAGE = "src/resources/images/skull.png";
	
	//Settings fish location
	public static final String SETTINGS_BLUE_FISH_IMAGE = "src/resources/images/player/fish_left_blue.png";
	public static final String SETTINGS_ORANGE_FISH_IMAGE = "src/resources/images/player/fish_left_orange.png";
	public static final String SETTINGS_GREEN_FISH_IMAGE = "src/resources/images/player/fish_left_green.png";
	public static final String SETTINGS_PURPLE_FISH_IMAGE = "src/resources/images/player/fish_left_purple.png";
	
	//Caught fish images
	public static final String FISH_CAUGHT_LEFT_IMAGE = "src/resources/images/player/fish_left_white.png";
	public static final String FISH_CAUGHT_DOWN_IMAGE = "src/resources/images/player/fish_down_white.png";
	public static final String FISH_CAUGHT_RIGHT_IMAGE = "src/resources/images/player/fish_right_white.png";
	public static final String FISH_CAUGHT_UP_IMAGE = "src/resources/images/player/fish_up_white.png";
	
	//Fish images location
	public static final String FISH_LEFT_IMAGE = "src/resources/images/player/fish_left_";
	public static final String FISH_DOWN_IMAGE = "src/resources/images/player/fish_down_";
	public static final String FISH_RIGHT_IMAGE = "src/resources/images/player/fish_right_";
	public static final String FISH_UP_IMAGE = "src/resources/images/player/fish_up_";
	
	//Ghost fish images location
	public static final String FISH_GHOST_LEFT_IMAGE = "src/resources/images/ghosts/fish_ghost_left_";
	public static final String FISH_GHOST_DOWN_IMAGE = "src/resources/images/ghosts/fish_ghost_down_";
	public static final String FISH_GHOST_RIGHT_IMAGE = "src/resources/images/ghosts/fish_ghost_right_";
	public static final String FISH_GHOST_UP_IMAGE = "src/resources/images/ghosts/fish_ghost_up_";
	
	//Dead fish image
	public static final String FISH_DEAD_IMAGE = "src/resources/images/player/deadfish.png";
	
	//Map images
	public static final String WATER_IMAGE = "src/resources/images/world/water.png";
	public static final String WALL_IMAGE = "src/resources/images/world/wall.png";
	public static final String START_IMAGE = "src/resources/images/world/start.png";
	public static final String FINISH_IMAGE = "src/resources/images/world/finish.png";
	public static final String BLOCK_IMAGE = "src/resources/images/world/block.png";
	public static final String GRASS_IMAGE = "src/resources/images/world/grass.png";
	
	//Fog Images
	public static final String FOG_IMAGE = "src/resources/images/fog.png";
	public static final String FOG_OPAQUE_IMAGE = "src/resources/images/fog-opaque.png";
	
	//Map file locations
	public static final String MAPS_LOCATION = "src/resources/maps/";
	
	//Colors for player
	public static final String BLUE = "blue";
	public static final String ORANGE = "orange";
	public static final String GREEN = "green";
	public static final String PURPLE = "purple";
	public static final String GREY = "grey";
}