import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The class that contains the Main method and creates all the panels.
 * @author Michelle Hu and Jasmine Liu
 *
 */
public class Main extends JFrame{
	JPanel cardPanel;
	private GamePanel panel2;
	private boolean hasPlayed;
	private boolean hasMade2;
	
	/**
	 * Constructs a JFrame and adds panels.
	 * @param title title of the game
	 */
	public Main(String title) {
		super(title);
		setBounds(500, 100, 480, 800);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    cardPanel = new JPanel();
	    CardLayout cl = new CardLayout();
	    cardPanel.setLayout(cl);
	    
		OptionPanel panel1 = new OptionPanel(this);    
	    panel2 = new GamePanel(this, false);
	    InstructionsPanel panel3 = new InstructionsPanel(this);
	    ChoosePanel panel4 = new ChoosePanel(this, panel2);
	    
	    addKeyListener(panel2.getKeyHandler());
	    panel4.addMouseListener(panel4);
	
	    cardPanel.add(panel1, "1");
	    cardPanel.add(panel2, "2");
	    cardPanel.add(panel3, "3");
	    cardPanel.add(panel4, "4");
	    
	    add(cardPanel);
	    changePanel("1");
	    
	    hasPlayed = false;
	    hasMade2 = false;
	    
	    setVisible(true);
	}

	/**
	 * Makes a Main object and runs it.
	 * @param args
	 */
	public static void main(String[] args)
	{
		Main w = new Main("INFINITE HOOPS");
	}
  
	/**
	 * Changes between the panels
	 * @param name panels name that you want to change to
	 */
	public void changePanel(String name) {
		((CardLayout)cardPanel.getLayout()).show(cardPanel,name);
		requestFocus();
	}
	
	/**
	 * Sets whether or not the game has been already played once.
	 * @param b
	 */
	public void setHasPlayed(boolean b){
		hasPlayed = b;
	}
	
	/**
	 * Resets GamePanel if user has already played once and starts the thread.
	 */
	public void start() {
		if (hasPlayed && !hasMade2){
			panel2.reset(false);
		}
		panel2.setStart(true);
		panel2.startThread();
	}
	
	/**
	 * Makes the game playable for 2 players
	 */
	public void make2Player(){
		panel2.reset(true);
		hasMade2 = true;
	}
	
	/**
	 * Sets whether or not the game has been made for 2
	 * @param b boolean whether the game has been made 2 player or not
	 */
	public void setHasMade(boolean b){
		hasMade2 = b;
	}

	
	
}




