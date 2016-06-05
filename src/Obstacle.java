
public class Obstacle  extends MovingImage{
	public static final int BAR_WIDTH = 50;
	public static final int BAR_HEIGHT = 10;
	
	/**
	 * Creates an Obstacle that can collide with the ball
	 * @param fileName
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public Obstacle(String fileName, int x, int y){
		super(fileName, x, y, BAR_WIDTH, BAR_HEIGHT);
	}
	
	/**
	 * Moves the obstacle by a given amount
	 * @param speed speed it travels upwards
	 */
	public void act(double width, double speed){
		moveByAmount(0, -1*speed);
	}

}
