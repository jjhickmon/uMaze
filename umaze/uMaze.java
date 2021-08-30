package umaze;

import umaze.grid.*;
import umaze.render.*;
import umaze.Settings;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.time.*;

public class uMaze extends JPanel {
	public static umaze.render.Frame frame;
	private static Instant start_time, end_time, solve_start_t, solve_end_t;	
		
	private static long timer = System.currentTimeMillis();
	private static int frames = 0;
	
	public static Cell[][] grid = new Cell[Settings.grid_h][Settings.grid_w];
		
	public static Stack<Cell> visited_c = new Stack<>(); // visited cells
	
	public static Set<Node> nodes = new LinkedHashSet<>();
	public static HashSet<Node> visited_n = new HashSet<>(); // visited nodes
	
	public static Node s_node; // start node
	public static Node e_node; // end node
	public static Map<Node,Node> prevNodes = new HashMap<>();
	public static ArrayList<Node> solution = new ArrayList<>();

	/**
	 * The main method, initiates the program
	 * @param args  The passed arguments
	 */
	public static void main(String[] args) {
		if(Settings.debug){
			System.out.println("grid width = " + Settings.grid_w);
			System.out.println("grid height = " + Settings.grid_h);
			System.out.println("update speed = " + Settings.tick + " milliseconds");
		}
		
		frame = new umaze.render.Frame();
		reset();
		update();
		while(!Settings.quit){
			run();
		}
		System.exit(0);
	}
	
	/**
	 * Runs the program by calling the update method after a
	 * specified interval of time
	 */
	private static void run(){
		Instant current_update = Instant.now();
		Instant last_update = Instant.now();
		double dt;
		
		long start_time = System.nanoTime();
		while(!Settings.quit && (Settings.create_maze || Settings.solve_maze)){
			// update the screen at a constant time
			current_update = Instant.now();
			dt = Duration.between(last_update, current_update).toMillis();
			
			if(dt > Settings.tick){
				update();
				last_update = Instant.now();
			}
			frame.repaint();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				frame.setTitle("uMaze" + " | " + frames + " fps");
				frames = 0;
			}
			
			// set framerate
			long totalTime = System.nanoTime() - start_time;
			if(totalTime < Settings.targetTime){
				try {
					Thread.sleep((Settings.targetTime - totalTime) / 1000000);
					start_time = System.nanoTime();
				} catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		}
		frames++;
		if(System.currentTimeMillis() - timer > 1000){
			timer += 1000;
			frame.setTitle("uMaze" + " | " + frames + " fps");
			frames = 0;
		}
		
		// set framerate
		long totalTime = System.nanoTime() - start_time;
		if(totalTime < Settings.targetTime){
			try {
				Thread.sleep((Settings.targetTime - totalTime) / 1000000);
				start_time = System.nanoTime();
			} catch (InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Resets the grid to its default state
	 */
	public static void reset(){
		start_time = Instant.now();
		
		nodes.clear();
		nodes = new LinkedHashSet<>();
		visited_n = new HashSet<>();
		prevNodes = new HashMap<>();
		solution = new ArrayList<>();
		
		visited_c = new Stack<>();
		visited_n.clear();
		
		initCells();
		frame.repaint();
	}
	
	/**
	 * Updates the status of every cell in the grid
	 */
	public static void update(){
		if(Settings.create_maze){
			Cell current = new Cell();
			// update each individual cell
			for(int row = 0; row < Settings.grid_h; row++){
				for(int col = 0; col < Settings.grid_w; col++){
					grid[row][col].update();
					if(grid[row][col].current == true){
						current = grid[row][col];
					}
				}
			}
					
			// move the current cell and remove the wall in between
			Cell next = current.getRandNeighbor();	
			if(next != null){
				current.current = false;
				current.visited = true;
				
				// stores the visited cell
				visited_c.push(current);
				
				next.visited = true;
				next.current = true;
				
				if(next == current.top){
					current.walls[0] = false;
					next.walls[2] = false;
				} else if(next == current.right){
					current.walls[1] = false;
					next.walls[3] = false;
				} if(next == current.bottom){
					current.walls[2] = false;
					next.walls[0] = false;
				} else if(next == current.left){
					current.walls[3] = false;
					next.walls[1] = false;
				}		
			} else {
				if(visited_c.size() > 0){
					// backtrack by setting the current cell to the last visited cell
					current.current = false;
					current.visited = true;
					current = visited_c.pop();
					current.current = true;
					current.visited = true;
				} else {
					end_time = Instant.now();
					long dt = Duration.between(start_time, end_time).toMillis();
					if(Settings.debug){
						System.out.println("done");
						System.out.println("generation time: " + (float)(dt / 1000.0) + " seconds");
					}
					Settings.create_maze = false;
				}
			}
			for(Cell[] cells : grid){
				for(Cell cell : cells){
					cell.update();
				}
			}
			
			if(Settings.quick_generate){
				update();
			}
		}
		else if (Settings.solve_maze){
			for(Cell[] cells : grid){
				for(Cell cell : cells){
					cell.update();
				}
			}
			
			for(Node node : nodes){
				node.update();
			}
		} else {
			for(Cell[] cells : grid){
				for(Cell cell : cells){
					cell.update();
				}
			}
		}
	}	
	
	
	public static void moveMaze(int new_x, int new_y){
		int d_x = new_x - grid[0][0].x;
		int d_y = new_y - grid[0][0].y;
		for(Cell[] cells : grid){
			for(Cell cell : cells){
				cell.x += d_x;
				cell.y += d_y;
				if(cell.node != null){
					cell.node.x += d_x;
					cell.node.y += d_y;
				}
			}
		}
	}
	
	/**
	 * Creates the grid of cells and sets their initial states
	 */
	private static void initCells(){
		for(int row = 0; row < Settings.grid_h; row++){
			for(int col = 0; col < Settings.grid_w; col++){
				// Creates a cell
				Cell cell = new Cell(col, row);
				grid[row][col] = cell;
				
				if (col == Settings.start_pos[0] && row == Settings.start_pos[1]){
					cell.visited = true;
					cell.current = true;
					cell.update();
				}
			}
		}
		
		// Sets the neighbors
		for(int row = 0; row < Settings.grid_h; row++){
			for(int col = 0; col < Settings.grid_w; col++){
				grid[row][col].setNeighbors();
			}
		}
	}
	
	/**
	 * Creates a grid of nodes based on the given maze
	 */
	public static void initNodes(){
		solve_start_t = Instant.now();
		solve_end_t = Instant.now();
		
		// Create the nodes
		for(int row = 0; row < Settings.grid_h; row++){
			for(int col = 0; col < Settings.grid_w; col++){
				int[] pos = {col, row};
				boolean start = false;
				boolean end = false;
				if(Arrays.equals(pos, Settings.s_node_pos)){
					start = true;
				}
				if(Arrays.equals(pos, Settings.e_node_pos)){
					end = true;
				}
				
				/* Rule 1: if two or more adjacent walls are active,
				 * create a node
				 * 
				 * Rule 2: if less than two walls are active,
				 * create a node */
				 
				int active_walls = 0; // the number of walls a cell has
				boolean prev_wall = false; // stores the state of the previous wall
				boolean[] walls = grid[row][col].walls;

				/* fencepost loop because the left and top walls
				 * still need to be compared at the end of the for loop */
				for(int i = 0; i < walls.length; i++){
					if(walls[i]){ active_walls++; }
					if(walls[i] && prev_wall){
						Node node = new Node(col, row, start, end);
						prev_wall = walls[i];
						break;
					} else {
						prev_wall = walls[i];
					}
					
					if(i == walls.length - 1){
						if(walls[0] && prev_wall){
							Node node = new Node(col, row, start, end);
							prev_wall = walls[i];
							break;
						}
					}
				}
				
				if(active_walls < 2){
					Node node = new Node(col, row, start, end);
				}
			}
		}
		
		// set neighbors and weights
		for(int i = 0; i < Settings.grid_h; i++){
			for(int j = 0; j < Settings.grid_w; j++){
				if(grid[i][j].node != null){
					grid[i][j].node.setNeighbors();
				}
			}
		}
	}
	
	/**
	 * Uses dijkstra's algorithm to solve the maze
	 */
	public static void dijkstra(){
		// the sortest cost it takes to get to each node from a neighbor
		Map<Node, Integer> totalCosts = new HashMap<>();
		/* uses the comparator in Node to put the least costly node 
		 * at the head of the queue */
		PriorityQueue<Node> pq = new PriorityQueue<Node>(nodes.size(), new Node());
		
		Set<Node> visited = new HashSet<>();  // all visited nodes
		
		s_node.cost = 0;
		totalCosts.put(s_node, s_node.cost);
		prevNodes.put(s_node, s_node);
		pq.add(s_node);
		
		if(s_node.neighbors.size() == 0){
			if(Settings.debug){ System.out.println("no solution"); }
			return;
		}
		
		for(Node node : nodes){
			if(node != s_node){
				totalCosts.put(node, Integer.MAX_VALUE);
			}
		}
		
		while (!pq.isEmpty()){
			Node smallest = pq.poll();
			visited.add(smallest);
			for(Node neighbor : smallest.neighbors){
				if(!visited.contains(neighbor)){
					if(!pq.contains(neighbor)){
						neighbor.cost = smallest.getCostTo(neighbor);
						pq.add(neighbor);
						totalCosts.replace(neighbor, neighbor.cost + smallest.cost);
						prevNodes.put(neighbor, smallest);
					} else {
						int newPath = totalCosts.get(smallest) + smallest.getCostTo(neighbor);
						if(newPath < totalCosts.get(neighbor)){
							totalCosts.replace(neighbor, newPath);
							prevNodes.replace(neighbor, smallest);
						}
					}
				}
			}
		}
		createSolution(e_node, prevNodes);
	}
	
	/**
	 * Creates a list of every node that composes the shortest path
	 * @param key  The key of the map that I want to add to the solution
	 * @param prevNodes  The map of nodes and their prev nodes on the shortest path
	 */
	private static void createSolution(Node key, Map<Node,Node> prevNodes){
		solution.add(key);
		Node prevNode = prevNodes.get(key);
		if(prevNode != s_node){
			createSolution(prevNode, prevNodes);
		} else {
			solution.add(prevNode);
			Settings.solved = true;
			solve_end_t = Instant.now();
			long dt = Duration.between(solve_start_t, solve_end_t).toMillis();
			if(Settings.debug){ System.out.println("solution time: " + (float)(dt / 1000.0) + " seconds"); }
		}
	}
}

