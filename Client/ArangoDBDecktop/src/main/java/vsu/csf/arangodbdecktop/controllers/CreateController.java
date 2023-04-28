package vsu.csf.arangodbdecktop.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vsu.csf.arangodbdecktop.ClientApplication;
import vsu.csf.arangodbdecktop.model.DataConnection;
import vsu.csf.arangodbdecktop.service.FileService;
import vsu.csf.arangodbdecktop.service.HttpService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CreateController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button canselButton;

    @FXML
    private TextField hostField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField passField;

    @FXML
    private TextField portField;

    @FXML
    private Button saveButton;

    @FXML
    private Button testButton;

    @FXML
    private Label testLabel;

    @FXML
    private TextField userField;
    @FXML
    void initialize() {

        saveButton.setOnAction(e -> {
            String dbName = this.nameField.getText();
            String port = this.portField.getText();
            String host = this.hostField.getText();
            String user = this.userField.getText();
            String password = this.passField.getText();

            DataConnection data = new DataConnection(dbName, host, Integer.parseInt(port), user, password); // создание экземпляра класса DataConnectionDto с нужными значениями

            List<DataConnection> old = FileService.readConnection();
            old.add(data);

            FileService.writeConnection(old);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message");
            alert.setHeaderText("Save is successful!");
            alert.showAndWait();
        });

        testButton.setOnAction(e ->{
            String dbName = this.nameField.getText();
            String port = this.portField.getText();
            String host = this.hostField.getText();
            String user = this.userField.getText();
            String password = this.passField.getText();

            DataConnection data = new DataConnection(dbName, host, Integer.parseInt(port), user, password); // создание экземпляра класса DataConnectionDto с нужными значениями

            HttpService service = new HttpService();

            int statusCode = service.createDb(data);
            System.out.println(statusCode);
            Alert alert;
            if (statusCode >= 200 && statusCode < 300) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Message");
                alert.setHeaderText("Create is successful!");
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message");
                alert.setHeaderText("Create error!");
            }
            alert.showAndWait();
        });


        canselButton.setOnAction(e -> {
            canselButton.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientApplication.class.getResource("ConnectWindow.fxml"));

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
