package umaze.render;

import umaze.render.Display;
import umaze.menu.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Frame extends JFrame{
	public static Display display = new Display();
	public static Menu menu = new Menu();
	/**
	 * Constructs the frame
	 */
	public Frame(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("uMaze");
		this.setResizable(false);
		this.add(display);
		this.addKeyListener(menu);
		this.pack();
		this.setLocationRelativeTo(null);  // centers frame
		this.setVisible(true);
	}
}

