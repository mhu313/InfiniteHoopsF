import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class OptionPanel extends JPanel implements ActionListener {
	
	private Main w;
	private BufferedImage image;
	
	/**
	 * Creates a new OptionPanel with a "Start" and "Instructions" button 
	 * @param w
	 */
	public OptionPanel(Main w) {
		this.w = w;
		try {
			image = ImageIO.read(new File("infinitehoops.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BasicBorders.ButtonBorder border = 
				new BasicBorders.ButtonBorder(Color.MAGENTA, Color.MAGENTA, Color.MAGENTA, Color.MAGENTA);
		setBackground(Color.BLACK);
		JButton button1 = new JButton("Start");
		JButton button2 = new JButton("Instructions");
		JButton button3 = new JButton("2 Players");
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		button1.setBackground(Color.WHITE);
		button1.setBorder(border);
		button2.setBackground(Color.WHITE);
		button2.setBorder(border);
		button3.setBackground(Color.WHITE);
		button3.setBorder(border);
		
		add(button1);
		add(button2);
		add(button3);
	}
	
	/**
	 * Draws the images
	 */
	public void paintComponent(Graphics g)
	  {
		super.paintComponent(g);
		g.drawImage(image, -10, 100, null);
	  }
	
	/**
	 * Switches panels depending on what button is pressed
	 */
	public void actionPerformed(ActionEvent e) {
		
		String comStr = e.getActionCommand();
		if (comStr.equals("Start")) {
			w.changePanel("4");
			w.setHasMade(false);
			/*w.changePanel("2");
			w.setHasMade(false);
			w.start();*/
		} else if (comStr.equals("Instructions")) {
			w.changePanel("3");
		} else if (comStr.equals("2 Players")){
			w.make2Player();
			w.changePanel("2");
			w.start();
		} 
	}
	
}
