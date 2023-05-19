package vsu.csf.arangodbdecktop.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import vsu.csf.arangodbdecktop.ClientApplication;
import vsu.csf.arangodbdecktop.model.DataConnection;
import vsu.csf.arangodbdecktop.model.Quarry;
import vsu.csf.arangodbdecktop.model.ResultModel;
import vsu.csf.arangodbdecktop.service.FileService;
import vsu.csf.arangodbdecktop.service.HttpService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class TabController {
    private DataConnection connection = FileService.
            readConnection(FileService.CURRENT_DATA_BASE_PASS).get(0);
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
        List<?> data = service.doingQuarry(
                new Quarry(this.connection, queryRequest));

        if (data.get(0) instanceof Map<?, ?> map) {
            System.out.println(map);

            this.resTable.getColumns().clear();
            this.resTable.getItems().clear();

            TableColumn<ResultModel, Object> argv1 = new TableColumn<>();
            TableColumn<ResultModel, Object> argv2 = new TableColumn<>();
            argv1.setCellValueFactory(new PropertyValueFactory<>("argv1"));
            argv1.setText("Key");
            argv2.setCellValueFactory(new PropertyValueFactory<>("argv2"));
            argv2.setText("Value");
            this.resTable.getColumns().add(argv1);
            this.resTable.getColumns().add(argv2);

            for (Object datum : data) {
                if (datum instanceof Map<?, ?> map1) {
                    for (Map.Entry<?, ?> item : map1.entrySet()) {
                        this.resTable.getItems().add(new ResultModel(item.getKey(), item.getValue()));
                    }
                    this.resTable.getItems().add(new ResultModel("---", "---"));
                }
            }


        } else {
            this.resTable.getColumns().clear();
            this.resTable.getItems().clear();
            TableColumn<ResultModel, Object> argv2 = new TableColumn<>();
            argv2.setCellValueFactory(new PropertyValueFactory<>("argv2"));
            argv2.setText("Value");
            this.resTable.getColumns().add(argv2);

            for (Object datum : data) {
                this.resTable.getItems().add(new ResultModel(null, datum));
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
