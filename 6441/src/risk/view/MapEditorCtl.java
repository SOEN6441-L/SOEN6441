package risk.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.control.ListView;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import risk.model.Continent;
import risk.model.Country;
import javafx.scene.Scene;
import javafx.stage.Stage;
import risk.model.DoDrawing;


public class MapEditorCtl {


    @FXML
    private ListView<String> countryList;

    @FXML
    private ListView<String> continentList;

    @FXML
    ObservableList<String> counteries = FXCollections.observableArrayList();

    @FXML
    ObservableList<String> continents = FXCollections.observableArrayList();

    @FXML
    private void initialize(){


        Pane root = new Pane();

        Canvas canvas = new Canvas(300, 300);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        //doDrawing(gc);

        DoDrawing draw = new DoDrawing();
        draw.doDrawing(gc);

        root.getChildren().add(canvas);

        Scene scene = new Scene(root, 1000 , 1000, Color.WHITESMOKE);
        Stage stage = new Stage();
        stage.setTitle("Stroke and fill");
        stage.setScene(scene);
        stage.show();



        countryList.setItems(countrylistFillData());
        continentList.setItems(continentListFillData());

    }



    private ObservableList<String> countrylistFillData(){
        System.out.println("11");

        for (int i = 0; i < 21; i ++){
            counteries.add(new Country("Country"+i, "China").getCountryName());
        }

        return counteries;
    }
    private ObservableList<String> continentListFillData(){
        continents.add(new Continent("Canada").getContinentName());
        continents.add(new Continent("China").getContinentName());
        continents.add(new Continent("America").getContinentName());

        return continents;

    }

    @FXML
    public void save() {


        Country country = new Country("Country21","China");



        if(!counteries.contains(country.getCountryName())){
            countryList.getItems().add(country.getCountryName());

            if(continents.contains(country.getBelongContient())){
                System.out.println("22");
            }else{
                continentList.getItems().add(country.getBelongContient());
            }
        }


        countryList.refresh();
        continentList.refresh();

    }



}
