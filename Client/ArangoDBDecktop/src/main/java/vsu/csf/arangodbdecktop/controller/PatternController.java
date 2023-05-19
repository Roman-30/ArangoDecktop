package vsu.csf.arangodbdecktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import vsu.csf.arangodbdecktop.model.DataConnection;
import vsu.csf.arangodbdecktop.model.QueryPattern;
import vsu.csf.arangodbdecktop.service.FileService;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class PatternController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addPatternButton;

    @FXML
    private TextField patternNameField;

    @FXML
    private TextArea queryField;

    @FXML
    void initialize() {
        addPatternButton.setOnAction(e -> {
            String name = patternNameField.getText();
            String query = queryField.getText();

            DataConnection data = FileService.readConnection(FileService.CURRENT_DATA_BASE_PASS).get(0);
            Alert alert;

            if (!Objects.equals(name, "") && !Objects.equals(query, "")) {
                FileService.writePattern(data.getDbName(), new QueryPattern(name, query));

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
    }
}

