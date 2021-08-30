package umaze.menu;

import umaze.*;
import umaze.grid.*;
import umaze.render.*;

import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Menu implements ActionListener, KeyListener{
	public static JButton start_b = new JButton("Generate");
	public static JButton solve_b = new JButton("Solve"); 
	public static JButton reset_b = new JButton("Reset");
	public static JButton exit_b = new JButton("Exit");
	public static JButton menu_b = new JButton();
	public static JButton[] buttons = {start_b, solve_b, reset_b, exit_b};
	Display display = umaze.render.Frame.display;
	
	public Menu(){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | 
			InstantiationException | 
			IllegalAccessException | 
			UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		for(JButton button : buttons){
			//button.setBackground(Settings.theme[0]);
			//button.setOpaque(true);
			//button.setBorderPainted(true);
			//button.setBorder(BorderFactory.createLineBorder(Settings.theme[2]));
			button.setFocusable(false);
			button.addActionListener(this);
		}
		menu_b.setFocusable(false);
		menu_b.addActionListener(this);
	}
	
	private void start(){
		Settings.create_maze = true;
		Settings.solve_maze = false;
		uMaze.reset();
		Settings.blank_maze = false;
		display.repaint();
	}
	
	private void solve(){
		Settings.blank_maze = false;
		uMaze.initNodes();
		Settings.create_maze = false;
		Settings.solve_maze = true;
		uMaze.update();
		uMaze.dijkstra();
	}
	
	private void reset(){
		Settings.blank_maze = true;
		Settings.create_maze = false;
		Settings.solve_maze = false;
		Settings.solved = false;
		uMaze.reset();
		uMaze.update();
	}
	
	private void exit(){
		Settings.quit = true;
	}
	
	private void menu(){
		Settings.toggle_menu = !(Settings.toggle_menu);
		if(Settings.toggle_menu){
			uMaze.moveMaze(Settings.maze_pos[0], Settings.maze_pos[1]);
		} else {
			uMaze.moveMaze(Settings.maze_pos[0] + (Settings.menu_w / 2), Settings.maze_pos[1]);
		}		
		display.repaint();
	}
	
	/**
	 * Checks for actions on the frame
	 * @param e The ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e){
		// start
		if(e.getSource() == buttons[0]){
			if(!Settings.solve_maze && !Settings.solved){
				start();
			}
		}
		// solve
		if(e.getSource() == buttons[1]){
			if(!Settings.create_maze && !Settings.solved){
				solve();
			}
		}
		
		// reset
		if(e.getSource() == buttons[2]){
			reset();
		}
		
		// exit
		if(e.getSource() == buttons[3]){
			exit();
		}
		
		if(e.getSource() == menu_b){
			menu();
		}
	}
	
	/* To use KeyListener, I need to override all default key input methods, 
	 * even if I dont use them
	 */
	
	/**
	 * Checks if the any key is released
	 * Overrides the default KeyListener method
	 * @param e The key release
	 */
	@Override
	public void keyReleased(KeyEvent e){

	}
	
	/**
	 * Checks if the any key is pressed
	 * Overrides the default KeyListener method
	 * @param e The key press
	 */
	@Override
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode() == KeyEvent.VK_ENTER && e.getKeyCode() == KeyEvent.VK_SPACE){
			if(!Settings.solve_maze && !Settings.solved){
				start();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_S){
			solve();
		}
		if(e.getKeyCode() == KeyEvent.VK_R){
			reset();
		}
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_Q){
			exit();
		}
		// screen shot
		if(e.getKeyCode() == KeyEvent.VK_C){
			try {
				Robot robot = new Robot();
				// gets the application's screen location
				Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
				int x = (int)(screen_size.getWidth()/2) - (Settings.screen_res[0]/2);
				int y = (int)(screen_size.getHeight()/2) - (Settings.screen_res[1]/2);
				int w = Settings.screen_res[0];
				int h = Settings.screen_res[1];
				Rectangle rectangle = new Rectangle(x, y, w, h);
				BufferedImage bufferedImage = robot.createScreenCapture(rectangle);
				File file = new File("screen-capture.png");
				boolean status = ImageIO.write(bufferedImage, "png", file);
				System.out.println("Screen Captured ? " + status + " File Path:- " + file.getAbsolutePath());
 
			} catch (AWTException | IOException ex) {
				System.err.println(ex);
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_EQUALS){
			Settings.tick += 25;
			if(Settings.debug){
				System.out.println("update speed = " +Settings .tick + " milliseconds");
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_M){
			menu();
		}
		if(e.getKeyCode() == KeyEvent.VK_MINUS){
			if(Settings.tick > 25){
				Settings.tick -= 25;
				if(Settings.debug){
					System.out.println("update speed = " + Settings.tick + " milliseconds");
				}
			}
		}
	}
	
	/**
	 * Checks if the any key is typed
	 * Overrides the default KeyListener method
	 * @param e The key typed event
	 */
	@Override
	public void keyTyped(KeyEvent e){
		
	}
}

