package vsu.csf.arangodbdecktop.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import vsu.csf.arangodbdecktop.HelloApplication;
import vsu.csf.arangodbdecktop.service.HttpService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea inputText;

    @FXML
    private TextArea outputText;

    @FXML
    private Button connectButton;

    @FXML
    private TextArea inputBox;

    @FXML
    private TreeView<?> tree;

    @FXML
    private Button doingButton;

    @FXML
    void initialize() {

        doingButton.setOnAction(e -> {
            DataConnection connection = new DataConnection(
                    "tes1", "localhost", 8529, "root","12345"
            );
            String line = inputText.getText();
            HttpService service = new HttpService();
            service.doingQuarry(connection, new Quarry(line));
            outputText.setText("OK");
        });

        connectButton.setOnAction(e -> {
            connectButton.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HelloApplication.class.getResource("ConnectWindow.fxml"));

            try {
                loader.load();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

        });
    }

}
