package risk;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import risk.view.MapEditorCtl;

import java.io.IOException;

public class Main extends Application {

    private  Stage  primaryStage;
    private  BorderPane mainLayout;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("6441");

        showMainView();
        showStartPage();


    }



    private  void showMainView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/MainView.fxml"));
        mainLayout = loader.load();
        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    private  void showStartPage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/StartPage.fxml"));
        BorderPane startPage = loader.load();
        mainLayout.setCenter(startPage);

    }
//
//    public  void mapStage() throws IOException {
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(Main.class.getResource("view/MapEditor.fxml"));
//        AnchorPane mapLayout = loader.load();
//
//        Stage subStage = new Stage();
//        subStage.initModality(Modality.WINDOW_MODAL);
//        subStage.initOwner(primaryStage);
//
//        Scene scene = new Scene(mapLayout);
//
//        subStage.setScene(scene);
//        subStage.showAndWait();
//
//    }


    public static void main(String[] args) {
        launch(args);
    }
}
