
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;

import javax.swing.*;

/*
 * Represents a moving image.
 *
 * by: Shelby
 * on: 5/3/13
 */
 
public class MovingImage extends Rectangle2D.Double {
	
	// FIELDS
	private Image image;
	
	// CONSTRUCTORS
	public MovingImage(String filename, int x, int y, int w, int h) {
		this((new ImageIcon(filename)).getImage(),x,y,w,h);
	}
	
	public MovingImage(Image img, int x, int y, int w, int h) {
		super(x,y,w,h);
		image = img;
	}
	
	public MovingImage(int x, int y, int w, int h) {
		super(x,y,w,h);
		image = null;
	}
	
	// METHODS	
	public void setImage(Image img) {
		image = img;
	}
	
	public void setImage(String filename) {
		image = (new ImageIcon(filename)).getImage();
	}
	
	public void moveToLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void moveByAmount(double x, double y) {
		this.x += x;
		this.y += y;
	}
	
	//public void changeSize(double w, double h) {
	//	this.setRect(getX(), getY(), w, h);
	//}
	
	public void applyWindowLimits(int windowWidth, int windowHeight) {
		x = Math.min(x,windowWidth-width);
		y = Math.min(y,windowHeight-height);
		x = Math.max(0,x);
		y = Math.max(0,y);
	}
	
	
	public void draw(Graphics g, ImageObserver io) {
		g.drawImage(image,(int)x,(int)y,(int)width,(int)height,io);
	}

	//public double getX(){
	//	return x;
	//}
	
	//public double getY(){
	//	return y;
	//}
	
	
}










