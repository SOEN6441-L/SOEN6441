package risk.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import risk.Main;

import java.io.IOException;

/**
 * Created by liarthur on 2017/10/4.
 */
public class StartPageCtl {

    @FXML
    private Main main;

    @FXML
    private void showMap() throws IOException {
//        System.out.println("1111111111");
//        main.mapStage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/MapEditor.fxml"));
        AnchorPane mapLayout = loader.load();
        Scene scene = new Scene(mapLayout);

        Stage stage = new Stage();
        stage.setTitle("map");
        stage.setScene(scene);
        stage.show();



    }


}
