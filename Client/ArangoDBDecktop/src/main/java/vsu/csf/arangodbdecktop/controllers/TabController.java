package vsu.csf.arangodbdecktop.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import vsu.csf.arangodbdecktop.ClientApplication;
import vsu.csf.arangodbdecktop.model.DataConnection;
import vsu.csf.arangodbdecktop.model.Quarry;
import vsu.csf.arangodbdecktop.model.ResultModel;
import vsu.csf.arangodbdecktop.service.HttpService;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

public class TabController {
    private DataConnection connection = new DataConnection(
            "tes1","localhost",8529,"root","12345"
    );
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button deleteButton;

    @FXML
    private Button doingButton;

    @FXML
    private Button findButton;

    @FXML
    private TextArea inputText;

    @FXML
    private Button insertButton;

    @FXML
    private TextArea outputText;

    @FXML
    private TableView<ResultModel> resTable;


    public AnchorPane getPane() {
        return this.anchorPane;
    }

    private void showResult(HttpService service, String queryRequest) {
        Map<String, Map<String, Object>> data = service.doingQuarry(
                new Quarry(this.connection, queryRequest));


        this.resTable.getColumns().clear();
        this.resTable.getItems().clear();

        if (data.size() != 0) {
            TableColumn<ResultModel, Object> argv1 = new TableColumn<>();
            TableColumn<ResultModel, Object> argv2 = new TableColumn<>();
            argv1.setCellValueFactory(new PropertyValueFactory<>("argv1"));
            argv2.setCellValueFactory(new PropertyValueFactory<>("argv2"));
            this.resTable.getColumns().add(argv1);
            this.resTable.getColumns().add(argv2);

            for (Map.Entry<String, Map<String, Object>> entry : data.entrySet()) {
                for (Map.Entry<String, Object> item : entry.getValue().entrySet()) {
                    this.resTable.getItems().add(new ResultModel(item.getKey(), item.getValue()));
                }
            }
        }

        outputText.clear();
    }

    private void createDataConnection() {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("ArangoDBConstructor.fxml"));
        try {
            fxmlLoader.load();
        } catch (IOException es) {
            throw new RuntimeException(es);
        }
        MainController d = fxmlLoader.getController();
        System.out.println("HIIIIIIIIIIIIIII");
        this.connection = new DataConnection(d.getConnection());
    }
    @FXML
    void initialize() {


        insertButton.setOnAction(e -> {
            inputText.clear();
            inputText.setText("INSERT { \\\"id\\\": @name, \\\"value\\\": @age } INTO @mycollection");
        });

        deleteButton.setOnAction(e -> {
            inputText.clear();
            inputText.setText("FOR my IN @mycollection REMOVE my IN @mycollection");
        });

        findButton.setOnAction(e -> {
            inputText.clear();
            inputText.setText("FOR my IN @mycollection RETURN my");
        });

        doingButton.setOnAction(e -> {
            if (connection == null) {
                createDataConnection();
            }
            outputText.clear();
            String line = inputText.getText().trim();

            HttpService service = new HttpService();
            showResult(service, line);
            outputText.setText("Completed successfully!");

        });
    }
}
