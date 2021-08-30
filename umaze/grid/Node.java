package umaze.grid;

import umaze.*;
import umaze.grid.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Node implements Comparator<Node>{
	public int col;
	public int row;
	public int x;
	public int y;
	public static int size = Cell.size;
	public Color color = Color.magenta;
	
	public boolean isStart = false;
	public boolean isEnd = false;
	
	// neighbors
	public Node top, right, bottom, left;
	// the distance to each neighbor
	public int topWeight, rightWeight, bottomWeight, leftWeight;
	public int cost; // the cost to travel to this node from the prev node
	public ArrayList<Node> neighbors = new ArrayList<>();
	
	/**
	 * Constructs an empty Node object
	 */
	public Node(){
		
	}
	
	/**
	 * Constructs a Node object at the specified column and row
	 * @param c The column of the node
	 * @param r The row of the node
	 * @param s True if the node is the start
	 * @param e True if the node is the end
	 */
	public Node(int c, int r, boolean s, boolean e){
		col = c;
		row = r;
		x = (col * size) + Settings.maze_pos[0];
		y = (row * size) + Settings.maze_pos[1];
		
		this.isStart = s;
		this.isEnd = e;
		this.update();
		uMaze.nodes.add(this);
		uMaze.grid[row][col].node = this;
	}
	
	/**
	 * Sets the node's neighbors and weights
	 */
	public void setNeighbors(){
		// top
		if(row == 0){ 
			this.topWeight = -1; 
		} else {
			for(int r = row - 1; r >= 0; r--){
				if(!uMaze.grid[row][col].walls[0]){
					if(uMaze.grid[r][col].node != null){
						this.top = uMaze.grid[r][col].node;
						this.top.bottom = this;
						this.topWeight = this.top.bottomWeight = this.row - this.top.row - 1;
						break;
					}
				} else {
					this.topWeight = -1;
					break;
				}
			}
		}
		
		//right
		if(col == Settings.grid_w - 1){
			this.rightWeight = -1;
		} else {
			for(int c = col + 1; c < Settings.grid_w; c++){
				if(!uMaze.grid[row][col].walls[1]){
					if(uMaze.grid[row][c].node != null){
						this.right = uMaze.grid[row][c].node;
						this.right.left = this;
						this.rightWeight = this.right.leftWeight = this.right.col - this.col - 1;
						break;
					}
				} else {
					this.rightWeight = -1;
					break;
				}
			}
		}
		
		// bottom
		if(row == Settings.grid_h - 1){
			this.bottomWeight = -1;
		} else {
			for(int r = row + 1; r < Settings.grid_h; r++){
				if(!uMaze.grid[row][col].walls[2]){
					if(uMaze.grid[r][col].node != null){
						this.bottom = uMaze.grid[r][col].node;
						this.bottom.top = this;
						this.bottomWeight = this.bottom.topWeight = this.bottom.row - this.row - 1;
						break;
					}
				} else {
					this.bottomWeight = -1;
					break;
				}
			}
		}
		
		// left
		if(col == 0){
			this.leftWeight = -1;
		} else {
			for(int c = col - 1; c >= 0; c--){
				if(!uMaze.grid[row][col].walls[3]){
					if(uMaze.grid[row][c].node != null){
						this.left = uMaze.grid[row][c].node;
						this.left.right = this;
						this.leftWeight = this.left.rightWeight = this.col - this.left.col - 1;
						break;
					}
				} else {
					this.leftWeight = -1;
					break;
				}
			}
		}
		
		if(top != null){ neighbors.add(top); }
		if(right != null){ neighbors.add(right); }
		if(bottom != null){ neighbors.add(bottom); }
		if(left != null){ neighbors.add(left); }
	}
	
	/**
	 * Gets the cost from this node to it's neighboring node
	 * @param neighbor The neighbor to get the cost to
	 * @return The weight to the specified neighbor
	 */
	public int getCostTo(Node neighbor){
		if(neighbor == top){ return topWeight; }
		if(neighbor == right){ return rightWeight; }
		if(neighbor == bottom){ return bottomWeight; }
		if(neighbor == left){ return leftWeight; }
		return 0;
	}
	
	/**
	 * Compares node costs against one another to find shortest travel distance
	 * Overrides the default Comparator override method.
	 * @param n1 The first node to check cost
	 * @param n2 The second node to check cost
	 * @return -1 if n1 cost is less than n2 cost, 
	 * 1 if n1 cost is greater than n2 cost, or 
	 * 0 if n1 cost is equal to n2 cost
	 */
	@Override
	public int compare(Node n1, Node n2){
		if(n1.cost < n2.cost){
			return -1;
		}
		if (n1.cost > n2.cost){
			return 1;
		}
		return 0;
	}
	
	/**
	 * Updates the node
	 */
	public void update(){
		if(isStart){
			color = Color.red;
			uMaze.s_node = this;
		} else if (isEnd){
			color = Color.blue;
			uMaze.e_node = this;
		} else {
			color = Color.magenta;
		}
	}
}

