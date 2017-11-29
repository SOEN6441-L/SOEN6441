package basicclasses;

import javax.swing.JPopupMenu;

/**
 * Class to extend JPopupMenu, add one new variable and relative methods
 * @see JPopupMenu
 */
public class MyPopupMenu extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	private String owner;
	
	/**
	 * Constructor of class.
	 */
	public MyPopupMenu(){
		super();
	}
	
	/**
	 * Method to get the owner of this popup menu.
	 * @return the owner's name
	 */
	public String getOwner() {
		return owner;
	}
	
	/**
	 * Method to set the owner of this popup menu
	 * @param owner's name
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
}
