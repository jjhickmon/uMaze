package umaze;

import java.awt.Color;

public class Settings {
	public static boolean debug = false;  // shows the debugging view
	public static boolean quick_generate = true;  // generates the maze a fast as possible
	public static boolean toggle_menu = false;
	public static boolean blank_maze = true;
	public static boolean create_maze = false;
	public static boolean solve_maze = false;
	public static boolean solved = false;
	public static boolean quit = false;
	
	public static int[] maze_pos = {100, 95};  // pos of the top left corner of the maze
	public static int[] start_pos = {7, 6}; // pos to start creating maze from in the grid
	public static int[] enter_pos = {0, 0}; // pos to draw entrance
	public static int[] exit_pos = {14, 12}; // pos to draw exit (multiple of two
	public static int tick = 200; // time between cell movements in milliseconds
	public static int fps = 60;
	public static int targetTime = 1000000000 / fps; // the target time in nanoseconds for one frame, or 1/60th of a second
	public static int cell_size = 20;
	public static int grid_w = 15; // The number of cells in each row
	public static int grid_h = 13; // The number of rows
	public static int[] s_node_pos = {0, 0}; // the start node location in the grid
	public static int[] e_node_pos = {grid_w - 1, grid_h - 1};  // the end node location in the grid
	public static int menu_w = 200;
	public static int[] grid_res = {grid_w * cell_size, grid_h * cell_size};
	public static int[] screen_res = {(grid_w * cell_size) + menu_w + (2*maze_pos[0]), 
									  (grid_h * cell_size) + (2*maze_pos[1])};
	
	// 0:cell color, 1:bg color, 2:wall color, 3:solution color, 4:menu color
	public static Color[] blue_theme = 
	{new Color(170, 211, 226), new Color(170, 211, 226), Color.white, Color.red, Color.gray};
	public static Color[] dark_theme = 
	{Color.white, Color.white, Color.black, Color.black, Color.gray};
	public static Color[] light_theme = 
	{Color.white, Color.black, Color.black, Color.gray};
	public static Color[] theme = blue_theme;  // light, dark, blue
}

