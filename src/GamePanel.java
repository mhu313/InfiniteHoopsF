import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.JPanel;

/**
 * The GamePanel class is the class that mainly handles the user portion for the game.
 * This class makes the panel and the game screen, it draws the objects and it controls
 * the scoring and whether or not the ball dies or lives.
 * @author Michelle Hu and Jasmine Liu
 *
 */
public class GamePanel extends JPanel implements Runnable{
	
	public static final int DRAWING_WIDTH = 500;
	public static final int DRAWING_HEIGHT = 900;
	
	
	private Ball b, b2;
	private ArrayList<Hoop>hoops;
	private ArrayList<Obstacle>bars;
	private ArrayList<PowerUp>power;
	private boolean start, powerOn, multi;
	private long startTime, lastGen, lastMove, powerStart, songPosition;
	private double ratio, genRate, moveRate, prevRatio;
	private ArrayList<String> hoopCol;
	private int hoopIndex, ballLives;
	private Clip song;
	private MovingImage image1, image2;
	private Main w;
	
	/**
	 * Creates a GamePanel object with set dimensions with a black background. Spawns balls and
	 * instantiates variables. A ratio is set to 30 to move the bars/power-ups/hoops by that
	 * amount each time.
	 * @param w Main object
	 * @param multi boolean for whether or not there are multiple players
	 */
	public GamePanel(Main w, boolean multi){
		super();
		initialize(multi);
		this.w = w;
	}
	
	private void initialize(boolean multi) {
		setBackground(Color.BLACK);
		bars = new ArrayList<Obstacle>();
		hoops = new ArrayList<Hoop>();
		power = new ArrayList<PowerUp>();
		this.multi = multi;
		b = spawnBall();
		if (multi) {
			b2 = spawnBall();
			while (b2.getColor() == b.getColor()) {
				b2 = spawnBall();
			}
		} else {
			b2 = null;
		}
		start = false;
		ratio = .75; //20
		hoopCol = new ArrayList<String>();
		genRate = 7000; //3000
		moveRate = 200; //100
		songPosition = 0;
		lastGen = 0;
		lastMove = 0;
		prevRatio = ratio;
		startTime = System.currentTimeMillis();
		image1 = new MovingImage("blacksky for apcs.jpg", 0, 0, 500, 850);
		image2 = new MovingImage("blacksky for apcs.jpg", 0, 850, 500, 850);
	}
	
	/**
	 * Resets the game.
	 * @param multi boolean for whether or not there are multiple players
	 */
	public void reset(boolean multi){
		initialize(multi);
		repaint();
	}
	
	/**
	 * It does through all the hoops and removes those that have left the screen. It finds
	 * and returns the hoop that the ball needs to pass through.
	 * @return returns the hoops that the ball has to pass through in order to do scoring.
	 */
	public Hoop filterHoops(Ball ball){
		for (int i = 0; i < hoops.size(); i++){
			Hoop hoop = hoops.get(i);
			if (hoop.getY() < 0){
				hoops.remove(i);
			}
			
			// Do nothing if colors are different
			if (hoop.getColor() != ball.getColor()) {
				continue;
			}
			
			if (ball.intersects(hoop)) {
				ball.changeScore(5);
				//System.out.println("hoop passed");
				hoops.remove(i);
			} else{
				if (ball.getY() > hoop.getY()) {
					ball.changeNumLives(-1);
					//System.out.println("hoop missed");
					hoops.remove(i);
				}
			}
		}
		return null;
	}
	
	/**
	 * Removes obstacles from the game that have moved off the screen.
	 */
	public void removeObstacles(){
		for (int i = 0; i < bars.size(); i++){
			Obstacle o = bars.get(i);
			if (o.getY() < 0){
				bars.remove(i);
			}
			if (o.getY() > 0){
				break;
			}
		}
	}
	
	/**
	 * Checks if the hoop that is at the ball's level is passed. A life is deducted if it
	 * doesn't.
	 * @param h the hoop at the same y as the ball with the same color - a hoop that the 
	 * ball must go through.
	 * @param ball the ball that is checked with the hoop
	 */
	public void hoopChecker(Hoop h, Ball ball){
		if (!(ball.intersects(h))){
			ball.changeNumLives(-1);
			System.out.println("hoop missed");
		}
		else{
			ball.changeScore(5);
			System.out.println("hoop passed");
		}
		hoops.remove(hoopIndex);
	}
	
	/**
	 * Uses the powerup when they get one. Lives may increase and color may change (won't revert).
	 * Things may get slowed down or the ball may shrink temporarily.
	 * @param p powerUp that is to be used
	 */
	public void usePowerUp(PowerUp p, Ball ball){
		if (p.toString().equals("Life")&& !p.getUsed()){
			ball.changeNumLives(1);
			p.setUsed();
			/*b.changeNumLives(1);
			if (b2 != null) {
				b2.changeNumLives(1);
			}*/
		}
		else if (p.toString().equals("SwitchColor")&& !p.getUsed()){
			int color = (int)(Math.random()*4);
			ball.changeColor(color);
			p.setUsed();
			/*int color = (int)(Math.random()*4);
			b.changeColor(color);
			if (b2 != null) {
				int color2 = (int)(Math.random()*4);
				while (color2 == color) {
					color2 = (int)(Math.random()*4);
				}
				b2.changeColor(color2);
			}*/
		}
		else{
			if (p.toString().equals("Shrink")&& !p.getUsed()){
				ball.changeSize(15);
				/*b.changeSize(15);
				if (b2 != null) {
					b2.changeSize(15);
				}*/
			}
			else if (p.toString().equals("SlowDown")&& !p.getUsed()){
				prevRatio = ratio;
				ratio = 1;
			}
			ball.resetPowerUp();
			/*b.resetPowerUp();
			if (b2 != null) {
				b2.resetPowerUp();
			}*/
			powerStart = System.currentTimeMillis();
			powerOn = true;
		}
	}
	
	/**
	 * Moves up all hoops/power-ups/obstacles up by a certain amount(ratio).
	 * @param ratio amount that the hoops/power-ups/obstacles move up by
	 */
	public void moveEverything(double ratio){
		for (Hoop h: hoops){
			h.act(ratio);	  	
		}		  	
		for (Obstacle o: bars){	
			o.act(DRAWING_WIDTH, ratio);
		}
		for (PowerUp p : power){
			p.act(ratio);
		}
	}
	
	/**
	 * Randomly spawns a ball that is one of 4 colors. At (250,50) and with dimensions 25x25
	 * @return Ball with the appropriate color
	 */
	public Ball spawnBall(){
		int color = (int)(Math.random()*4);
		return spawnBall(color);
	}
	
	/**
	 * Spawns a ball of a specific color
	 * @param color integer that represents a color
	 * @return a new ball that is made in that specific color
	 */
	public Ball spawnBall(int color) {
		return new Ball(250, 50, 25, 25, color);
	}
	
	/**
	 * Generates power-ups at specific coordinates
	 * @param x x coordinate of power-up
	 * @param y y coordinate of power-up
	 */
	public void addPowerUp(int x, int y){
		double add = Math.random();
		if (add < 0.2){
			int which = (int)(Math.random()*4);
			if (which == 0){
				power.add(new Life("+1 life.png", x, y, "Life"));
			}
			else if (which == 1){
				power.add(new Shrink("Shrink.png", x, y, "Shrink"));
			}
			else if (which == 2){
				power.add(new SlowDown("SlowDown.png", x, y, "SlowDown"));
			}
			else if (which == 3){
				power.add(new SwitchColor("Changecolor.png", x, y, "SwitchColor"));
			}
		}
	}
	
	/**
	 * Adds obstacles. It has a 50/50 chance of being a moving obstacle.
	 * @param x-coord
	 * @param y-coord
	 */
	public void addObstacle(double x, double y){
		int moving = (int)(Math.random()*2);
		if (moving == 0){
			bars.add(new Obstacle("bar.png", (int)x, (int)y));
		}
		else{
			bars.add(new MovingObstacle("bar.png", (int)x, (int)y));
		}
	}
	
	/**
	 * Changes the rate at which new objects are generated.
	 * @return the new rate that the objects will be generated
	 */
	public double changeGenRate(){
		//return genRate - 2;
		if (!powerOn){
			return genRate - 0.3;
		}
		return genRate;
	}
	
	
	/**
	 * Adds 4 hoops, each of a different color.
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param opt how many hoop colors
	 * @pre opt is between 1 and 4
	 */
	public void addHoop(double x, double y, int opt){
		if (hoopCol.size() == 0){
			hoopCol.add("hoop1.png");
			hoopCol.add("hoop2.png");
			hoopCol.add("hoop3.png");
			hoopCol.add("hoop4.png");
		}
		int color = (int)(Math.random()*opt);
		String str = hoopCol.get(color);
		if (str.equals("hoop1.png")){
			hoops.add(new Hoop(str, (int)x, (int)y, 0));
		}
		else if (str.equals("hoop2.png")){
			hoops.add(new Hoop(str, (int)x, (int)y, 1));
		}
		else if (str.equals("hoop3.png")){
			hoops.add(new Hoop(str, (int)x, (int)y, 2));
		}
		else{
			hoops.add(new Hoop(str, (int)x, (int)y, 3));
		}
		hoopCol.remove(color);
	}
	
	/**
	 * changes ratio (how fast the objects move up on screen) by a factor of 1.001.
	 * @return
	 */
	public double changeRatio(){
		double newR = 1.0001*ratio;
		if (ratio <= 20 ){
			return newR;
		}
		else{
			return ratio;
		}
		//return 1.001*ratio;
	}
	
	/**
	 * Returns whether or not the game has started.
	 * @return boolean that depicts whether or not the game has starts
	 */
	public boolean getStart(){
		return start;
	}
	
	/**
	 * Changes the ball's color to one that is indicated.
	 * @param c integer of what color the ball will be
	 * @pre c is between 0 and 3 inclusive.
	 */
	public void setBallColor(int c) {
		b.changeColor(c);
		/*
		int currScore = b.getScore();
		int nL = b.getNumLives();
		b = spawnBall(c);
		b.changeScore(currScore);
		b.setNumLives(nL);
		*/
	}
	
	/**
	 * Sets the start of the game.
	 * @param b boolean that will determine if the game started or not
	 */
	public void setStart(boolean b){
		start = b;
	}
	
	/**
	 * Starts thread and background audio.
	 */
	public void startThread(){
		new Thread(this).start();
		startTime = System.currentTimeMillis();
		try{
			AudioInputStream audioInputStream =
	                AudioSystem.getAudioInputStream( new File("background.wav"));
	            song = AudioSystem.getClip();
	            song.open(audioInputStream);
	            song.setMicrosecondPosition(songPosition);
	            song.loop(Clip.LOOP_CONTINUOUSLY);
	            song.start();
		}catch(IOException ex){
			ex.printStackTrace();
		}catch(UnsupportedAudioFileException ex){
			ex.printStackTrace();
		}catch(LineUnavailableException ex){
			ex.printStackTrace();
		}
		startTime = System.currentTimeMillis();
	}
	
	
	/**
	 * Generates hoops/obstacles/power-ups at the bottom of the screen.
	 */
	public void generateStuff(){
		double space = 100;
		int firstX = (int)(Math.random()*(DRAWING_WIDTH/3-Hoop.HOOP_WIDTH));
		int hoopY = DRAWING_HEIGHT - Hoop.HOOP_HEIGHT;
		int firstY = DRAWING_HEIGHT - Obstacle.BAR_HEIGHT + 25;
		for (int i = 4; i > 0; i--){
			int newX = (int)(firstX+(4-i)*space);
			int newHoopY = (int)(hoopY+space*(4-i));
			addHoop(newX, newHoopY, i);
			int numObstacles = (int)Math.random()*5+1;
			for (; numObstacles > 0; numObstacles--){
				int newY = (int)(firstY+(numObstacles-i)*50);
				int randX = (int)(Math.random()*(DRAWING_WIDTH - Obstacle.BAR_WIDTH));
				//int randY = (int)(Math.random()*(DRAWING_HEIGHT - (DRAWING_HEIGHT - Hoop.HOOP_HEIGHT) + 1)+ DRAWING_HEIGHT - Hoop.HOOP_HEIGHT);
				addObstacle(randX, newY);
			}
		}
		int randX = (int)(Math.random()*(DRAWING_WIDTH - Obstacle.BAR_WIDTH));
		addPowerUp(randX, DRAWING_HEIGHT-10);
		//repaint();
	}
	
	/**
	 * Runs the program as long as there are still lives left.
	 */
	public void run(){
		while(b.getNumLives() > 0){
			try{
				if (multi && b2.getNumLives() <=0){
					ballLives = b.getNumLives();
					b.setNumLives(0);
				}
				//if (System.currentTimeMillis() - lastMove >= moveRate){
					ratio = changeRatio();
					System.out.println(ratio);
					moveEverything(ratio);
					//System.out.println("moved");
					Hoop pass = filterHoops(b);
					if (pass != null){
						hoopChecker(pass, b);
						//System.out.println("checking hoops");
					}
					if (b2 != null){
						Hoop pass2 = filterHoops(b2);
						if (pass2 != null){
							hoopChecker(pass2, b2);
						}
					}
					b.act(bars);
					if (b2 != null){
						b2.act(bars);
					}
					PowerUp p = null;
					p = b.getPowerUp(power);
					if (p != null){
						usePowerUp(p, b);
					}
					if (b2 != null){
						PowerUp p2 = null;
						p2 = b2.getPowerUp(power);
						if (p2 != null){
							usePowerUp(p2, b2);
						}	
					}
					if (powerOn){
						long currentTime = System.currentTimeMillis();
						if (currentTime - powerStart >= 5000){
							b.changeSize(25);
							ratio = prevRatio;
							if (b2 != null){
								b2.changeSize(25);
								ratio = prevRatio;
							}
							powerOn = false;
						}
					}
					removeObstacles();
					lastMove = System.currentTimeMillis();
				//}
				genRate = changeGenRate();
				System.out.println(genRate);
				if (System.currentTimeMillis() - lastGen >= genRate){
					generateStuff();
					lastGen = System.currentTimeMillis();
				}
				repaint();
				Thread.sleep(17);
			}catch(InterruptedException e){
				
			}
		}
	}
	
	/**
	 * Draws all the components.
	 */
	public synchronized void paintComponent(Graphics g)
	  {
		//System.out.println("paintComponent");
	
	    super.paintComponent(g);  // Call JPanel's paintComponent method to paint the background

		Graphics2D g2 = (Graphics2D)g;

	    int width = getWidth();
	    int height = getHeight();
	    
	    double ratioX = (double)width/DRAWING_WIDTH;
	    double ratioY = (double)height/DRAWING_HEIGHT;
	    
	    AffineTransform at = g2.getTransform();
	    g2.scale(ratioX, ratioY);
	    
	    image1.draw(g, this);
	    image2.draw(g, this);
	    
	    if (b.getNumLives() > 0)
	    {
	    	image1.moveByAmount(0, -1);
	    	image2.moveByAmount(0, -1);
	    }
	    if (image1.getY() <= -850)
	    	image1 = new MovingImage("blacksky for apcs.jpg", 0, 850, 500, 850);
	    if (image2.getY() <= -850)
	    	image2 = new MovingImage("blacksky for apcs.jpg", 0, 850, 500, 850);
	    
	    
	    g.setColor(Color.magenta);
	    
	    try{
	    	for (Hoop h: hoops){
		  		h.draw(g2, this);
		  	}
		    
		    for (Obstacle o: bars){
		    		o.draw(g2, this);
		    }
		    
		    for (PowerUp p: power){
		    	if (p instanceof Shrink){
		    		Shrink s = (Shrink)p;
		    		s.draw(g2,this);
		    	}
		    	if (p instanceof SlowDown){
		    		SlowDown s = (SlowDown)p;
		    		s.draw(g2,this);
		    	}
		    	if (p instanceof Life){
		    		Life s = (Life)p;
		    		s.draw(g2,this);
		    	}
		    	if (p instanceof SwitchColor){
		    		SwitchColor s = (SwitchColor)p;
		    		s.draw(g2,this);
		    	}
		    }
	    }catch(ConcurrentModificationException ex){
	    	System.out.println("cme");
	    }
	    
	    if ((b.getNumLives() <= 0) ||
	    	(multi && b2.getNumLives() <= 0))
	    {
	    	g.setColor(Color.BLACK);
	    	g.drawRect(0, 300, 500, 100);
	    	g.fillRect(0, 300, 500, 100);
	    	g.setColor(Color.magenta);
	    	g.drawString("GAME OVER", 220, 330);
	    	if (multi){
	    		if (ballLives> b2.getNumLives()){
	    			g.drawString("Winner: " + b.getColorString(), 210, 350);
	    		}
	    		else if (ballLives==b2.getNumLives()){
	    			g.drawString("No Winner", 215, 350);
	    		}
	    		else{
	    			g.drawString("Winner: " + b2.getColorString(), 210, 350);
	    		}
	    	}
	    	else{
	    		g.drawString("Final Score: " + b.getScore(), 210, 350);
	    	}
	    	g.drawString("Press the Spacebar to go back to the homescreen", 130, 370);
	    	g.drawLine(0, 300, 500, 300);
	    	g.drawLine(0, 400, 500, 400);
	    	if (song != null){
	    		song.stop();
	    	}
	    	start = false;
	    }
	    
	    int c = b.getColor();
	    if (c == 0)
	    	g.setColor(Color.MAGENTA);
	    else if (c == 1)
	    	g.setColor(Color.YELLOW);
	    else if (c == 2)
	    	g.setColor(Color.CYAN);
	    else
	    	g.setColor(Color.GREEN);
	    g.setFont(new Font("Monospaced Plain", Font.PLAIN, 20));
	    g.drawString("Score:" + b.getScore(), 10, 20);
	    g.drawString("Lives: " + b.getNumLives(), 420, 20);

	    if (multi){
		    c = b2.getColor();
		    if (c == 0)
		    	g.setColor(Color.MAGENTA);
		    else if (c == 1)
		    	g.setColor(Color.YELLOW);
		    else if (c == 2)
		    	g.setColor(Color.CYAN);
		    else
		    	g.setColor(Color.GREEN);
	    	g.drawString("Score:" + b2.getScore(), 10, 45);
	    	g.drawString("Lives: " + b2.getNumLives(), 420, 45);
	    }
	    //g.setColor(new Color(205,102,29));
	    /*for (Shape s : bars) {
	    	g2.fill(s);
	    }*/
	    b.draw(g2,this);
	    
	    
	    if (b2 != null){
	    	b2.draw(g2, this);
	    }
	    
	    
	    g2.setTransform(at);

		// TODO Add any custom drawings here
	  }

	
	public class KeyHandler implements KeyListener{
		@Override
		
		/**
		 * Determines what to do one a key is pressed.
		 */
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				if (start){
					b.moveByAmount(-20, 0);
					if (b.getWalled()){
		  				repaint();
		  			}
					if (multi){
		  				if (b.intersects(b2)){
							b2.actBall(b);
						}
		  			}
				}
		  	} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
		  		if (start){
		  			b.moveByAmount(20, 0);
		  			if (b.getWalled()){
		  				repaint();
		  			}
		  			if (multi){
		  				if (b.intersects(b2)){
							b2.actBall(b);
						}
		  			}
		  		}
		  	} else if (e.getKeyCode() == KeyEvent.VK_UP) {
		  		if (start){
		  			if (b.getWalled()){
		  				repaint();
		  			}
		  			b.moveByAmount(0, -20);
		  		}
		  	} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
		  		if (start){
		  			if (b.getWalled()){
		  				repaint();
		  			}
		  			b.moveByAmount(0, 20);
		  		}
		  	} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				if (!start){
					w.setHasPlayed(true);
					w.changePanel("1");
				}
		  	} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				if (start){
					if (song != null){
						song.stop();
					}
				}
		  	}
		  	if (b2 != null){
		  		if (e.getKeyCode() == KeyEvent.VK_A) {
					if (start){
						b2.moveByAmount(-20, 0);
						if (b2.getWalled()){
			  				repaint();
			  			}
						if (b2.intersects(b)){
							b.actBall(b2);
						}
					}
			  	} if (e.getKeyCode() == KeyEvent.VK_D) {
			  		if (start){
			  			b2.moveByAmount(20, 0);
			  			if (b2.getWalled()){
			  				repaint();
			  			}
			  			if (b2.intersects(b)){
							b.actBall(b2);
						}
			  		}
			  	}if (e.getKeyCode() == KeyEvent.VK_W) {
			  		if (start){
			  			b2.moveByAmount(0, -20);
			  		}
			  	}if (e.getKeyCode() == KeyEvent.VK_S) {
			  		if (start){
			  			b2.moveByAmount(0, 20);
			  		}
			  	}
		  	}
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		
	}


	public KeyListener getKeyHandler() {
		// TODO Auto-generated method stub
		return new KeyHandler();
	}
	
	
}
