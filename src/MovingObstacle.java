
public class MovingObstacle extends Obstacle{
	
	/**
	 * Creates a new MovingObstacle
	 * @param fileName
	 * @param x
	 * @param y
	 */
	public MovingObstacle(String fileName, int x, int y){
		super(fileName, x, y);
	}
	
	/**
	 * Moves the obstacle by a given amount
	 * @param width width of the game panel.
	 * @param speed speed it travels upwards
	 */
	public void act (double width, double speed){
		moveByAmount(.5, -1*speed);
		if (getX() >= width - BAR_WIDTH){
			moveToLocation(0, getY());
		}
	}

}
