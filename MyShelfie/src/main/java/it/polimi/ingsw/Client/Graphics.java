package main.java.it.polimi.ingsw.Client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Graphics extends Application{
    public static void main(String args[]){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(new Label(""),300, 250));
        stage.show();
    }
}
