package ru.inkrot.kit.part3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.inkrot.kit.part3.model.DataBase;

public class Main extends Application {

    public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        try {
            DataBase.connect();
            Parent root = FXMLLoader.load(getClass().getResource("view/main.fxml"));
            stage.setTitle("Салон продажи сотовых телефонов");
            stage.setScene(new Scene(root, 900, 600));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
