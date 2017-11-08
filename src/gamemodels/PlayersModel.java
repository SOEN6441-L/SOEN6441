package gamemodels;

import java.util.ArrayList;
import java.util.Observable;

public class PlayersModel extends Observable {
	private ArrayList <PlayerModel> players;

	public ArrayList <PlayerModel> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList <PlayerModel> players) {
		this.players = players;
		setChanged();
        notifyObservers(this);
	}

}
