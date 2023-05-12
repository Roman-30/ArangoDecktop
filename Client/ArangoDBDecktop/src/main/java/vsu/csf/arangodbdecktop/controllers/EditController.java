package vsu.csf.arangodbdecktop.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vsu.csf.arangodbdecktop.ClientApplication;
import vsu.csf.arangodbdecktop.model.DataConnection;
import vsu.csf.arangodbdecktop.model.QueryPatterns;
import vsu.csf.arangodbdecktop.service.FileService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditController {

    private DataConnection old;

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
    private TextField userField;

    public void setConnection(DataConnection data) {
        this.old = data;
    }

    private void reloadWindow(String window) {
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
        stage.setResizable(false);
        stage.show();
    }

    public void setValue(DataConnection data) {
        if (data != null) {
            this.hostField.setText(data.getHost());
            this.nameField.setText(data.getDbName());
            this.portField.setText(String.valueOf(data.getPort()));
            this.passField.setText(data.getPassword());
            this.userField.setText(data.getUserName());
        }
    }

    @FXML
    void initialize() {

        saveButton.setOnAction(e -> {
            try {
                String dbName = this.nameField.getText();
                String port = this.portField.getText();
                String host = this.hostField.getText();
                String user = this.userField.getText();
                String password = this.passField.getText();

                DataConnection data = new DataConnection(dbName, host, Integer.parseInt(port), user, password);

                List<DataConnection> connections = FileService.readConnection(QueryPatterns.ALL_DATA_BASE_PASS);
                connections.remove(old);
                connections.add(data);

                FileService.writeConnection(connections, QueryPatterns.ALL_DATA_BASE_PASS);

                saveButton.getScene().getWindow().hide();
                reloadWindow("ConnectWindow.fxml");

            } catch (Exception ex) {
                ex.fillInStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Data error!");
                alert.showAndWait();
            }
        });

        canselButton.setOnAction(e -> {
            canselButton.getScene().getWindow().hide();
            reloadWindow("ConnectWindow.fxml");
        });
    }
}

