import java.awt.Color;


public class Hoop extends MovingImage{
	public static final int HOOP_WIDTH = 50;
	public static final int HOOP_HEIGHT = 20;
	private int color;
	
	/**
	 * Creates a new Hoop with a given color and size
	 * @param fileName
	 * @param x
	 * @param y
	 * @param color
	 */
	public Hoop(String fileName, int x, int y, int color){
		super(fileName, x, y, HOOP_WIDTH, HOOP_HEIGHT);
		this.color = color;
	}
	
	/**
	 * Returns the color of the hoop
	 * @return color
	 */	
	 public int getColor(){
		return color;
	}
	
	 /**
	  * Moves the Hoop by a given amount
	  * @param speed
	  */
	public void act(double speed){
		moveByAmount(0, -1*speed);
	}
	

}
