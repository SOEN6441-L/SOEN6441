package risk.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by liarthur on 2017/10/5.
 */
public class Continent {

    private final StringProperty continentName;
    private final StringProperty continentArmyNumber;
    private final StringProperty countryInside;

    public Continent(){
        this(null);
    }

    public Continent(String continentName) {
        this.continentName = new SimpleStringProperty(continentName);
        this.continentArmyNumber = new SimpleStringProperty("2");
        this.countryInside = new SimpleStringProperty("country1");
    }

    public String getContinentName() {
        return continentName.get();
    }

    public StringProperty continentNameProperty() {
        return continentName;
    }

    public void setContinentName(String continentName) {
        this.continentName.set(continentName);
    }

    public String getContinentArmyNumber() {
        return continentArmyNumber.get();
    }

    public StringProperty continentArmyNumberProperty() {
        return continentArmyNumber;
    }

    public void setContinentArmyNumber(String continentArmyNumber) {
        this.continentArmyNumber.set(continentArmyNumber);
    }

    public String getCountryInside() {
        return countryInside.get();
    }

    public StringProperty countryInsideProperty() {
        return countryInside;
    }

    public void setCountryInside(String countryInside) {
        this.countryInside.set(countryInside);
    }
}
