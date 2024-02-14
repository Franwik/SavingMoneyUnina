package smu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Stage primaryStage;

    @Override
    public void start(@SuppressWarnings("exports") Stage stage) throws IOException {
        primaryStage = stage;
        setResolution(750, 550);
        primaryStage.setMinWidth(750);
        primaryStage.setMinHeight(550);
        setScene("Login");
        //primaryStage.getIcons().add(new Image(getClass().getResourceAsStream( "icon.png" )));
        primaryStage.setTitle("Saving Money Unina");
        //Resizable(false);
        Center();
        primaryStage.show();
    }

    static void Resizable(boolean check) {
        primaryStage.setResizable(check);
    }

    static void Center() {
        primaryStage.centerOnScreen();
    }

    static void setScene(String fxml) throws IOException {
        primaryStage.setScene(new Scene(loadFXML(fxml)));
    }

    static void setResolution(double width, double height) throws IOException {
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}