
public interface PowerUp{
	
	/**
	 * moves up
	 * @param speed by how much it moves up
	 */
	void act(double speed);
	
	/**
	 * Returns a string
	 * @return String representation of type of powerUp
	 */
	String toString();
	
	boolean getUsed();
	
	void setUsed();
}
