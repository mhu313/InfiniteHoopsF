import java.util.ArrayList;

public class Ball extends MovingImage{
	private int prevW, prevH;
	private PowerUp power;
	private int numLives;
	private int score;
	private int color;
	private String filename;
	private boolean walled;
	
	/**
	 * Creates a new ball with 3 lives and a 0 score
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param width width of ball
	 * @param height height of ball
	 * @param color integer that represents the color of the ball
	 * @pre color is between 0-3 inclusive, no negative x and y coordinates, no negative 
	 * widths and heights
	 * @post new ball is created
	 */
	public Ball(int x, int y, int width, int height, int color){
		super(x, y, width, height);
		power = null;
		numLives = 3;
		score = 0;
		this.color = color;
		prevW = width;
		prevH = height;
		walled = false;
		setColor(color);
	}
	
	private void setColor(int color) {
		this.color = color;
		if (color == 0) {
			filename = "ball1.png";
		} else if (color == 1) {
			filename = "ball2.png";
		} else if (color == 2) {
			filename = "ball3.png";
		} else {
			filename = "ball4.png";
		}
		setImage(filename);
	}
	
	/**
	 * resets the powerup
	 */
	public void resetPowerUp(){
		power = null;
	}
	
	/**
	 * Returns the color as a string
	 * @return a string of the ball's color
	 */
	public String getColorString(){
		if (color == 0){
			return "purple";
		}else if (color ==1){
			return "yellow";
		}else if (color ==2){
			return "blue";
		}else{
			return "green";
		}
	}
	
	/**
	 * Registers to see if the ball collides with any obstacles
	 * @param obstacles ArrayList of Obstacles
	 * @post number of lives may decrease and ball may have moved or bounced on obstacle
	 */
	public void act(ArrayList<Obstacle> obstacles){
		for (Obstacle o : obstacles){
			if (o.intersects(this)){
				numLives--;
				act(o);
				break;
			}
		}
	}
	
	/**
	 * Registers to see if the ball collides with another ball
	 * @param b other ball
	 * @post ball may have moved or bounced on other ball
	 */
	public void actBall(Ball b){
		if (intersects(b)){
			act(b);
		}
	}
	
	private void act(MovingImage o){
		if (o.getX() < getCenterX()){
			for (int i = 5; i > 0; i--){
				moveByAmount(i*3.5, 0);
			}
		}else{
			for (int i = 5; i > 0; i--){
				moveByAmount(-i*3.5, 0);
			}
		}
		
	}
	
	/**
	 * return power-up
	 * @return PowerUp that ball has
	 */
	public PowerUp getPower(){
		return power;
	}
	
	/**
	 * Detects if you've obtain a powerup 
	 * @param powerups
	 * @return more specific powerup
	 */
	public PowerUp getPowerUp(ArrayList<PowerUp> powerups){
		for (PowerUp p : powerups){
			if (p instanceof SlowDown){
				SlowDown s = (SlowDown)p;
				if (intersects(s)){
					power = s;
					return power;
				}
			}
			else if (p instanceof Shrink){
				Shrink s = (Shrink)p;
				if (intersects(s)){
					power = s;
					return power;
				}
			}
			else if (p instanceof Life){
				Life s = (Life)p;
				if (intersects(s)){
					power = s;
					return power;
				}
			}
			else if (p instanceof SwitchColor){
				SwitchColor s = (SwitchColor)p;
				if (intersects(s)){
					power = s;
					return power;
				}
			}
		}
		return null;
	}
	
	/**
	 * Sets the size of the ball
	 * @param size radius of the ball
	 */
	public void changeSize(int size)
	{
		//changeSize(size, size);
		this.setRect(x, y, size,  size);
	}
	
	/**
	 * Changes color of ball.
	 * @param color integer that represents a color
	 * @pre color is 0-3 inclusive
	 */
	public void changeColor(int color)
	{
		setColor(color);
	}
	
	
	/**
	 * Returns the ball's score
	 * @return score
	 */
	public int getScore(){
		return score;
	}
	
	/**
	 * Returns the number of lives the ball has
	 * @return number of lives
	 */
	public int getNumLives(){
		return numLives;
	}
	
	/**
	 * Sets the number of lives
	 * @param m new number of lives
	 */
	public void setNumLives(int m){
		numLives = m;
	}
	
	/**
	 * Changes the number of lives
	 * @param change
	 */
	public void changeNumLives(int change){
		numLives +=change;
	}
	
	/**
	 * Changes the score
	 * @param amount
	 */
	public void changeScore(double amount){
		score+=amount;
	}
	
	
	/**
	 * Returns the color of the ball
	 * @return color
	 */
	public int getColor(){
		return color;
	}
	
	public boolean getWalled(){
		return walled;
	}
	
	/**
	 * Moves the ball be an amount and decreases life if you hit a side
	 */
	public void moveByAmount(double x, double y) {
		this.x += x;
		if (getX() < 0){
			numLives--;
			this.x = 0 + 20;
		}
		else if (getX() > GamePanel.DRAWING_WIDTH-getWidth()){
			numLives--;
			this.x = GamePanel.DRAWING_WIDTH-getWidth() - 20;
		}
		
		this.y += y;
		if (getY() < 0){
			numLives--;
			this.y = 0+20;
		}
		else if (getY() > GamePanel.DRAWING_HEIGHT-getHeight()){
			numLives--;
			this.y = GamePanel.DRAWING_HEIGHT-getHeight() - 20;
		}
		
		
		if (numLives == 0){
			walled = true;
		}
		
	}
	
}
