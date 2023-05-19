package vsu.csf.arangodbdecktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import vsu.csf.arangodbdecktop.model.DataConnection;
import vsu.csf.arangodbdecktop.service.FileService;
import vsu.csf.arangodbdecktop.service.HttpService;

import java.net.URL;
import java.util.ResourceBundle;

public class CollectionController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addButton;

    @FXML
    private TextField nameField;
    @FXML
    void initialize() {
        addButton.setOnAction(e -> {
            HttpService service = new HttpService();

            String name = nameField.getText();

            if (nameField != null) {

                DataConnection data = FileService.readConnection(FileService.CURRENT_DATA_BASE_PASS).get(0);
                data.setCollection(name);

                int code = service.createCollection(data);

                Alert alert;
                if (code < 300) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Message");
                    alert.setHeaderText("Create is successful!");
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Message");
                    alert.setHeaderText("Collection is exist!");
                }
                alert.showAndWait();
            }
        });
    }
}
