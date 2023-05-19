package vsu.csf.arangodbdecktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import vsu.csf.arangodbdecktop.model.DataConnection;
import vsu.csf.arangodbdecktop.service.FileService;
import vsu.csf.arangodbdecktop.service.HttpService;
import vsu.csf.arangodbdecktop.util.WindowUtils;

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

    private DataConnection createDataConnection() {
        String dbName = this.nameField.getText();
        String port = this.portField.getText();
        String host = this.hostField.getText();
        String user = this.userField.getText();
        String password = this.passField.getText();

        try {
            return new DataConnection(dbName, host, Integer.parseInt(port), user, password);
        } catch (Exception e) {
            return null;
        }
    }
    @FXML
    void initialize() {
        testButton.setOnAction(e ->{
            DataConnection newConnection = createDataConnection();

            HttpService service = new HttpService();

            int statusCode = service.createDb(newConnection);

            Alert alert;
            if (statusCode >= 200 && statusCode < 300) {
                List<DataConnection> old = FileService.readConnection(FileService.ALL_DATA_BASE_PASS);
                old.add(newConnection);
                FileService.writeConnection(old, FileService.ALL_DATA_BASE_PASS);

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Create is successful!");
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Create error!");
            }
            alert.setTitle("Message");
            alert.showAndWait();
        });


        canselButton.setOnAction(e -> {
            canselButton.getScene().getWindow().hide();
            WindowUtils.loadWindow("ConnectWindow.fxml", false);
        });


    }
}
