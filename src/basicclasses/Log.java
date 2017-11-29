package basicclasses;

import java.io.Serializable;
import java.util.Observable;
/**
 * Class for define a observable log string
 * @see Observable
 * @see Serializable
 */
public class Log extends Observable implements Serializable{
	private static final long serialVersionUID = 6L;
	private String logStr;

	/**
	 * Method to get log string
	 * @return the log string
	 */
	public String getLogStr() {
		return logStr;
	}

	/**
	 * Method to set log string
	 * @param logStr the new log string
	 */
	public void setLogStr(String logStr) {
		this.logStr = logStr;
		setChanged();
		notifyObservers(logStr);
	}	
	
	
}
