package vsu.csf.arangodbdecktop.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import vsu.csf.arangodbdecktop.ClientApplication;

import java.io.IOException;

public class WindowUtils {
    public static void loadWindow(String window, boolean wait) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ClientApplication.class.getResource(window));

        try {
            loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        if (wait) {
            stage.showAndWait();
        } else {
            stage.show();
        }
    }

}
