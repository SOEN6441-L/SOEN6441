package gameviews;

import java.rmi.Remote;
import java.util.Observable;

public interface PhaseViewInterface extends Remote {
	public void update(Observable arg0, Object arg1);
}
