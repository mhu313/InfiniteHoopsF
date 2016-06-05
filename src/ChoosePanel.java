import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicBorders;

public class ChoosePanel extends JPanel implements MouseListener, ActionListener{

	private Main w;
	private GamePanel g;
	private BufferedImage[] ball;
	
	/**
	 * Creates a new OptionPanel with a "Start" and "Instructions" button 
	 * @param w
	 * @param g
	 */
	public ChoosePanel(Main w, GamePanel g) {
		this.w = w;
		this.g = g;
		ball = new BufferedImage[4];
		
		try {
			for (int i = 1; i <= 4; i++){
				String name = "ball" + i + ".png";
				ball[i-1] = ImageIO.read(new File(name));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BasicBorders.ButtonBorder border = 
				new BasicBorders.ButtonBorder(Color.MAGENTA, Color.MAGENTA, Color.MAGENTA, Color.MAGENTA);
		setBackground(Color.BLACK);
		JButton button1 = new JButton("Back");
		JButton button2 = new JButton("Random");
		button1.addActionListener(this);
		button2.addActionListener(this);
		button1.setBackground(Color.WHITE);
		button2.setBackground(Color.WHITE);
		button1.setBorder(border);
		button2.setBorder(border);
		
		add(button1);
		add(button2);
	}
	
	/**
	 * Draws the objects 
	 */
	public void paintComponent(Graphics g)
	  {
		super.paintComponent(g);

		g.setColor(Color.magenta);
		g.setFont(new Font("Monospaced Plain", Font.PLAIN, 30));
		g.drawString("Please choose a color", 80, 180);
		
	    for (int i = 0; i < ball.length; i++)
	    {
	    	g.drawImage(ball[i], 90 + i*70, 275, 50, 50, null);
	    }
	  }
	
	/**
	 * Switches panels depending on what button is pressed
	 */
	public void actionPerformed(ActionEvent e) {
		
		String comStr = e.getActionCommand();
		if (comStr.equals("Back")) {
			w.changePanel("1");
		}
		if (comStr.equals("Random")) {
			w.changePanel("2");
			w.start();
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		int mx = arg0.getX();
		int my = arg0.getY();
		
		if (my > 275 && my < 325) {
			if (mx > 90 && mx < 140) {
				w.changePanel("2");
				w.setHasMade(false);
				w.start();
				g.setBallColor(0);
			}
			else if (mx > 160 && mx < 210) {
				w.changePanel("2");
				w.setHasMade(false);
				w.start();
				g.setBallColor(1);
			}
			else if (mx > 230 && mx < 280) {
				//g.setBallColor(2);
				w.changePanel("2");
				w.setHasMade(false);
				w.start();
				g.setBallColor(2);
			}
			else if (mx > 300 && mx < 350) {
				//g.setBallColor(3);
				w.changePanel("2");
				w.setHasMade(false);
				w.start();
				g.setBallColor(3);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
