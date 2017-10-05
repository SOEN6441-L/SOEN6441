package risk.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by liarthur on 2017/10/4.
 */
public class Countryfill {
    public ObservableList<String> countrylistFill(){
        System.out.println("11");
        ObservableList<String> counteries = FXCollections.observableArrayList();
        for (int i = 0; i < 21; i ++){
            counteries.add("Country" + i);
        }
        //continentList.setItems(counteries);
        return counteries;
    }
}
