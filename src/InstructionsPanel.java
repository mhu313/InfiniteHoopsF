import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicBorders;

public class InstructionsPanel extends JPanel implements ActionListener {
	
	Main w;
	public static final int DRAWING_WIDTH = 500;
	public static final int DRAWING_HEIGHT = 900;
	private BufferedImage[] ball, hoop;
	private BufferedImage life, change, slow, shrink;
	
	/**
	 * Creates an InstructionsPanel with a black background and a "Back" button
	 * @param w main object
	 */
	public InstructionsPanel(Main w) {
		setBackground(Color.BLACK);
		this.w = w;
		JButton button = new JButton("Back");
		button.setBackground(Color.WHITE);
		button.setBorder(new BasicBorders.ButtonBorder(Color.MAGENTA, Color.MAGENTA, Color.MAGENTA, Color.MAGENTA));
		button.addActionListener(this);
		ball = new BufferedImage[4];
		hoop = new BufferedImage[4];
		
		try {
			for (int i = 1; i <= 4; i++){
				String name = "ball" + i + ".png";
				ball[i-1] = ImageIO.read(new File(name));
			}
			for (int i = 1; i <= 4; i++) {
				String name = "hoop" + i + ".png";
				hoop[i-1] = ImageIO.read(new File(name));
			}
			life = ImageIO.read(new File("+1 life.png"));
			change = ImageIO.read(new File("Changecolor.png"));
			shrink = ImageIO.read(new File("Shrink.png"));
			slow = ImageIO.read(new File("SlowDown.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		add(button);
	}
	
	/**
	 * Switches back to the OptionPanel if the "Back" button is pressed
	 */
	public void actionPerformed(ActionEvent e) {
		
		String comStr = e.getActionCommand();
		if (comStr.equals("Back"))
			w.changePanel("1");
	}
	
	/**
	 * Draws the Instruction panel
	 */
	public void paintComponent(Graphics g)
	  {
	    super.paintComponent(g);  // Call JPanel's paintComponent method to paint the background

		Graphics2D g2 = (Graphics2D)g;

	    int width = getWidth();
	    int height = getHeight();
	    
	    double ratioX = (double)width/DRAWING_WIDTH;
	    double ratioY = (double)height/DRAWING_HEIGHT;
	    
	    AffineTransform at = g2.getTransform();
	    g2.scale(ratioX, ratioY);

	    /*
	     * 	+ "The ball will be falling down the screen with colored hoops"
	    		+ "flying towards it. Guide the ball to fall through the hoops"
	    		+ "that are the same color as the ball. Missing a hoop will result"
	    		+ "in a loss of a life. You have three lives total. Multiple "
	    		+ "powerups will be along the way. As time passes on, the ball will"
	    		+ "fall faster and faster. Try to get as far as you can!"
	     */
	    
	    g.setFont(new Font("Monospaced Plain", Font.PLAIN, 15));
	    g.setColor(Color.magenta);
	    g.drawString("Use the arrow keys to move the ball. Hoops will be flying", 50, 75);
	    g.drawString("towards it. Guide the ball to fall through the hoops that", 50, 95);
	    g.drawString("are the same color as the ball. Missing a hoop will result", 50, 115);
	    g.drawString("in a loss of a life. You have three lives total. Multiple ", 50, 135);
	    g.drawString("powerups will be along the way. As time passes on, the ball", 50, 155);
	    g.drawString("will fall faster and faster. Try to get as far as you can!", 50, 175);
	    
	    g.drawString("You can lose a life in the following ways:", 50, 210);
	    g.drawString("1. Missing your hoop.", 50, 230);
	    g.drawString("2. Hitting the edge.", 50, 250);
	    g.drawString("3. Hitting an obstacle.", 50, 270);
	    
	    for (int i = 0; i < ball.length; i++)
	    {
	    	g.drawImage(ball[i], 60 + i*30, 300, 25, 25, null);
	    }
	    g.drawString("You'll be the Ball", 300, 320);
	    
	    for (int i = 0; i < hoop.length; i++)
	    {
	    	g.drawImage(hoop[i], 50 + i*45, 370, 40, 20, null);
	    }
	    g.drawString("Pass through the hoop of ", 300, 390);
	    g.drawString("your color", 300, 410);
	    
	    g.drawImage(life, 100, 460, 30, 30, null);
	    g.drawString("Add life", 300, 480);
	    
	    g.drawImage(shrink, 100, 530, 30, 30, null);
	    g.drawString("Shrink", 300, 550);
	    
	    g.drawImage(change, 100, 600, 30, 30, null);
	    g.drawString("Change Color", 300, 620);
	    
	    g.drawImage(slow, 100, 660, 30, 30, null);
	    g.drawString("Slow Down", 300, 680);
	    
	    
	  }
}
