
public class Shrink extends MovingImage implements PowerUp{

	private String type;
	private boolean used;
	
	/**
	 * Creates a new Shrink PowerUp
	 * @param fileName filaName of image that will represent it
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param type String of what type of power-up it is
	 */
	public Shrink(String fileName, int x, int y, String type)
	{
		super(fileName, x, y, 10, 10);
		this.type = type;
		used = false;
	}
	
	/**
	 * moves the powerup
	 */
	@Override
	public void act(double speed) {
		moveByAmount(0, -1*speed);
	}
	
	/**
	 * Changes the size of the ball
	 * @param b ball whose size is to be changed
	 */
	public void changeSize(Ball b)
	{
		b.changeSize(5);
	}
	
	/**
	 * Returns the type of PowerUp
	 */
	public String toString()
	{
		return type;
	}

	public boolean getUsed(){
		return used;
	}
	
	public void setUsed(){
		used = true;
	}
}
