package umaze.render;

import umaze.*;
import umaze.grid.*;
import umaze.menu.Menu;
import umaze.render.Frame;

import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Display extends JPanel{
	/**
	 * Constructs the display panel
	 */
	public Display(){
		this.setLayout(new GridLayout());
		this.setPreferredSize(new Dimension(Settings.screen_res[0], Settings.screen_res[1]));
		this.setBackground(Settings.theme[1]);

		for(JButton button : Frame.menu.buttons){
			this.add(button);
		}
		this.add(Frame.menu.menu_b);
	}

	public void draw_buttons(){
		if(!Settings.toggle_menu){
			Menu.menu_b.setBounds(Settings.screen_res[0] - 55, 10, 45, 45);
		} else {
			Menu.menu_b.setBounds(Settings.screen_res[0] - 158, 10, 120, 35);
		}

		for(int i = 0; i < Menu.buttons.length; i++){
			if(Settings.toggle_menu){
				Menu.buttons[i].setBounds(Settings.screen_res[0] - 189, (i*90) + 50, 180, 80);
			} else {
				// moves Menu.buttons offscreen
				Menu.buttons[i].setBounds(Settings.screen_res[0] + 15, (i*90) + 50, 180, 80);
			}
		}
	}

	/**
	 * Paints to the screen
	 *
	 * @param g is the graphics object
	 */
	public void paintComponent (Graphics g){
		Cell[][] grid = uMaze.grid;
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;

		draw_buttons();

		// update the Menu.buttons and draw the menu
		if(Settings.toggle_menu){
			g2D.setColor(Settings.theme[4]);
			g2D.fillRect(Settings.screen_res[0] - Settings.menu_w, 0, Settings.menu_w, Settings.screen_res[1]);
		}
		
		if(!Settings.blank_maze){
			if(uMaze.grid != null){
				if(uMaze.grid.length * uMaze.grid[0].length == Settings.grid_w * Settings.grid_h){
					// draw cells
					for(int row = 0; row < Settings.grid_h; row++){
						for(int col = 0; col < Settings.grid_w; col++){
							Cell cell = grid[row][col];

							// Fills a rectangle based on the attributes of each cell
							g2D.setColor(cell.color);

							if(cell.current && Settings.debug){  // colors current cell while generating
								g2D.fillRect(cell.x + (int)(Settings.cell_size / 4), cell.y + (int)(Settings.cell_size / 4),
								cell.size - (int)(Settings.cell_size / 2), cell.size - (int)(Settings.cell_size / 2));
							} else {
								g2D.fillRect(cell.x - (int)(Settings.cell_size / 4), cell.y - (int)(Settings.cell_size / 4),
								cell.size + (int)(Settings.cell_size / 2), cell.size + (int)(Settings.cell_size / 2));
							}

						}
					}

					// draw nodes
					if(Settings.solve_maze || !Settings.create_maze){
						if (uMaze.nodes != null){
							for(int row = 0; row < Settings.grid_h; row++){
								for(int col = 0; col < Settings.grid_w; col++){
									if(uMaze.grid[row][col].node != null){
										Node node = grid[row][col].node;
										int offset = 10;
										int text_offset = 20;

										// center coordinates for text
										int c_x = node.x + node.size/2 - 4;
										int c_y = node.y + node.size/2 + 5;

										// only draw nodes in debug mode
										if(Settings.debug){
											// draw node
											g2D.setColor(node.color);
											g2D.fillOval(node.x + offset/2, node.y + offset/2, node.size - offset, node.size - offset);
											// draw weights
											g2D.setColor(Color.black);
											g2D.drawString(String.valueOf(node.topWeight), c_x, c_y - text_offset);
											g2D.drawString(String.valueOf(node.rightWeight), c_x + text_offset, c_y);
											g2D.drawString(String.valueOf(node.bottomWeight), c_x, c_y + text_offset);
											g2D.drawString(String.valueOf(node.leftWeight), c_x - text_offset, c_y);
										}
										//} else {
											//// draw start and end nodes
											//g2D.setColor(node.color);
											//if(row == .s_node.row && col == .s_node.col){
												//g2D.fillOval(node.x + offset/2, node.y + offset/2, node.size - offset, node.size - offset);
											//}
											//if(row == .e_node.row && col == .e_node.col){
												//g2D.fillOval(node.x + offset/2, node.y + offset/2, node.size - offset, node.size - offset);
											//}
										//}
									}
								}
							}
						}
					}

					// draw walls
					for(int row = 0; row < Settings.grid_h; row++){
						for(int col = 0; col < Settings.grid_w; col++){
							Cell cell = grid[row][col];
							if(cell.visited && !Settings.blank_maze){
								// draw walls individually so they can be removed later
								g2D.setColor(Settings.theme[2]);
								g2D.setStroke(new BasicStroke(Settings.cell_size / 2)); // hacky way to make walls squares
								// top
								if(cell.walls[0]){ g2D.drawLine(cell.x, cell.y, cell.x + cell.size, cell.y); }
								// right
								if(col == Settings.exit_pos[0] && row == Settings.exit_pos[1]){
								} else {
									if(cell.walls[1]){ g2D.drawLine(cell.x + cell.size, cell.y, cell.x + cell.size, cell.y + cell.size); }
								}
								// bottom
								if(cell.walls[2]){ g2D.drawLine(cell.x, cell.y + cell.size, cell.x + cell.size, cell.y + cell.size); }
								// left
								if(col == Settings.enter_pos[0] && row == Settings.enter_pos[1]){
								} else {
									if(cell.walls[3]){ g2D.drawLine(cell.x, cell.y, cell.x, cell.y + cell.size); }
								}
							}
						}
					}

					// draw shortest path
					if(Settings.solved){
						g2D.setColor(Settings.theme[3]);
						g2D.setStroke(new BasicStroke((int)(Settings.cell_size / 10)));
						for(Node key : uMaze.solution){
							int keyIndex = uMaze.solution.indexOf(key);
							if(keyIndex < uMaze.solution.size() - 1){
								g2D.drawLine(key.x + key.size / 2, key.y + key.size / 2,
								uMaze.solution.get(keyIndex + 1).x + key.size / 2, uMaze.solution.get(keyIndex + 1).y + key.size / 2);
							}
						}
						Settings.solved = true;
						Settings.solve_maze = true;
					}
				}
			}
		}
	}
}
