package gamemodels;

import java.util.Observable;
/**
 * 
 * user-defined observable array of nodes
 *
 */
public class ObservableNodes extends Observable{

	private NodeRecord[][] localNodes;
	/**
	 * Initialize  x dimension of array
	 * @param x X dimension
	 */	
	public void initDimensionX(int x){
		if (x>0)
			localNodes = new NodeRecord[x][];
	}
	/**
	 * Initialize  y dimension of array
	 * @param indexX x dimension
	 * @param y      y dimension
	 */	
	public void initDimensionY(int indexX, int y){
		if (y>0)
			localNodes[indexX] = new NodeRecord[y];
	}
	/**
	 * Initialize values
	 * @param x      x dimension
	 * @param y      y dimension
	 * @param name   name of nodes
	 * @param number number of nodes
	 */	
	public void initValue(int x, int y, String name, int number){
		localNodes[x][y] = new NodeRecord(name,number);
	}

	/**
	 * Increase value
	 * @param x x dimension
	 * @param y y dimension
	 */	
	public void increaseValue(int x, int y){
		localNodes[x][y].setNumber(localNodes[x][y].getNumber()+1);
		setChanged();
		notifyObservers("Step 4 - Player"+(x+1)+" place one army on "+localNodes[x][y].getName());
	}
	/**
	 * Get node
	 * @return localNodes local node array
	 */
	public NodeRecord[][] getNodes(){
		return localNodes;
	}
}
