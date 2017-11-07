package basicclasses;

import javax.swing.JPopupMenu;

/**
 * Class to extend JPopupMenu, add one new varible and relative methods
 * @see JPopupMenu
 */
public class MyPopupMenu extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	private String owner;
	
	/**
	 * Construct of class.
	 */
	public MyPopupMenu(){
		super();
	}
	
	/**
	 * Method to get the owner of this popupmenu.
	 * @return the owner's name
	 */
	public String getOwner() {
		return owner;
	}
	
	/**
	 * Method to set the owner of this popupmenu
	 * @param owner's name
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
}
