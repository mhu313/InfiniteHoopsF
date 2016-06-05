
public class SwitchColor extends MovingImage implements PowerUp{
	
	private String type;
	private boolean used;
	
	/**
	 * Creates a new SlowDown PowerUp
	 * @param fileName filaName of image that will represent it
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param type String of what type of power-up it is
	 */
	public SwitchColor(String fileName, int x, int y, String type)
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
	 * Returns the PowerUp type
	 */
	public String toString()
	{
		return type;
	}

	public boolean getUsed() {
		// TODO Auto-generated method stub
		return used;
	}

	@Override
	public void setUsed() {
		// TODO Auto-generated method stub
		used = true;
	}


}
