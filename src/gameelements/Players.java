package gameelements;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by liarthur on 03/11/2017.
 */
public class Players extends Observable{

    private ArrayList<Player> players;

    public Players( ) { }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
        setChanged();
        notifyObservers(this);
    }
}
