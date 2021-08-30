package umaze.grid;

import umaze.*;
import umaze.grid.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Cell {
	public int col;
	public int row;
	public int x;
	public int y;
	public static int size = Settings.cell_size; // the size of each cell in px
	
	public Color color;
	private Random rand = new Random();
	
	public boolean current = false;
	public boolean visited = false;
	
	// top wall, right wall, bottom wall, left wall
	public boolean[] walls = {true, true, true, true};
	
	// the neighboring cells
	public Cell top, right, bottom, left;
	private Cell[] neighbors; // the cells next to this cell
	private Stack<Cell> open_neighbors = new Stack<>(); // the available neighbors
	
	public Node node;
	
	/**
	 * Constructs an empty Cell object
	 */
	public Cell(){		

	}
	
	/**
	 * Constructs a Cell object at the specified column and row
	 * @param c The column of the cell
	 * @param r The row of the cell
	 */
	public Cell(int c, int r){	
		col = c;
		row = r;
		x = (col * size) + Settings.maze_pos[0];
		y = (row * size) + Settings.maze_pos[1];
		neighbors = new Cell[4];
		color = Settings.theme[0];
	}
	
	/**
	 * Sets the cell to visited status
	 */
	public void visit(){
		visited = true;
		color = Color.pink;
	}
	
	/**
	 * Tracks if the cell's neighbors have been visited
	 */
	public void setNeighbors(){				
		// check the edge cases
		if (row != 0){ top = uMaze.grid[row - 1][col]; } else { top = null; }
		if (col != Settings.grid_w - 1){ right = uMaze.grid[row][col + 1]; } else { right = null; }
		if (row != Settings.grid_h - 1){ bottom = uMaze.grid[row +1][col]; } else { bottom = null; }
		if (col != 0){ left = uMaze.grid[row][col - 1]; } else { left = null; }
		// set the neighbors
		this.neighbors[0] = top;
		this.neighbors[1] = right;
		this.neighbors[2] = bottom;
		this.neighbors[3] = left;
	}
	
	/**
	 * Updates the cell's stack of available neighbors, 
	 * also updates the cell color
	 */
	public void update(){
		if(current && Settings.create_maze && Settings.debug){
			color = Color.red;
		} else if (Settings.debug && this.visited && Settings.create_maze){
			color = Color.gray;
		} else {
			color = Settings.theme[0];
		}
		
		this.setNeighbors();
		open_neighbors = new Stack<Cell>();
		
		if(neighbors[0] != null && neighbors[0].visited != true){
			open_neighbors.push(neighbors[0]);
		}
		if(neighbors[1] != null && neighbors[1].visited != true){
			open_neighbors.push(neighbors[1]);
		}
		if(neighbors[2] != null && neighbors[2].visited != true){
			open_neighbors.push(neighbors[2]);
		}
		if(neighbors[3] != null && neighbors[3].visited != true){
			open_neighbors.push(neighbors[3]);
		}
	}	
	
	/**
	 * Gets a random available neighbor of the cell
	 * @return c A random open neighbor or 
	 * null if the cell has no open neighbors
	 */
	public Cell getRandNeighbor(){
		if(open_neighbors.size() > 0){
			Cell c = open_neighbors.get(rand.nextInt(open_neighbors.size()));
			return c;
		} else {
			return null;
		}
	}
}

