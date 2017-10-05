package risk.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by liarthur on 2017/10/5.
 */
public class Country {

    private final StringProperty countryName;
    private final StringProperty belongContient;
    private final StringProperty belongPlayer;
    private final StringProperty armyNumber;


    public Country() {
        this(null,null);
    }

    public Country(String countryName, String belongContient){
        this.countryName = new SimpleStringProperty(countryName);
        this.belongContient = new SimpleStringProperty(belongContient);


        this.belongPlayer = new SimpleStringProperty("Player1");
        this.armyNumber = new SimpleStringProperty("0");
    }

    public String getCountryName(){
        return countryName.get();
    }
    public void setCountryName(String countryName){
        this.countryName.set(countryName);
    }
    public StringProperty countryNameProperty(){
        return countryName;
    }


    public String getBelongContient() {
        return belongContient.get();
    }

    public StringProperty belongContientProperty() {
        return belongContient;
    }

    public void setBelongContient(String belongContient) {
        this.belongContient.set(belongContient);
    }


    public String getBelongPlayer() {
        return belongPlayer.get();
    }

    public StringProperty belongPlayerProperty() {
        return belongPlayer;
    }

    public void setBelongPlayer(String belongPlayer) {
        this.belongPlayer.set(belongPlayer);
    }

    public String getArmyNumber() {
        return armyNumber.get();
    }

    public StringProperty armyNumberProperty() {
        return armyNumber;
    }

    public void setArmyNumber(String armyNumber) {
        this.armyNumber.set(armyNumber);
    }
}
