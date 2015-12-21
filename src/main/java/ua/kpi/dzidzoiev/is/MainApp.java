package ua.kpi.dzidzoiev.is;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        String fxmlFile = "/fxml/main_window.fxml";
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        stage.setTitle("Cryptography labs");
        stage.setScene(new Scene(root));
        stage.show();

//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/sym_tab.fxml"));
//        Parent root = (Parent)loader.load();
//        SymmetricTabController controller = (SymmetricTabController)loader.getController();
//        controller.setStage(stage);
    }
}