package vsu.csf.arangodbdecktop.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import vsu.csf.arangodbdecktop.ClientApplication;
import vsu.csf.arangodbdecktop.model.DataConnection;
import vsu.csf.arangodbdecktop.model.Quarry;
import vsu.csf.arangodbdecktop.model.ResultModel;
import vsu.csf.arangodbdecktop.service.HttpService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController {


    private DataConnection connection;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private AnchorPane left;

    @FXML
    private Button deleteButton;

    @FXML
    private Button insertButton;

    @FXML
    private Button findButton;
    @FXML
    private TextArea inputText;

    @FXML
    private TextArea outputText;

    @FXML
    private Button connectButton;

    @FXML
    private TextArea inputBox;

    @FXML
    private TreeView<String> tree = new TreeView<>(new TreeItem<>());

    @FXML
    private Button doingButton;

    @FXML
    private TableView<ResultModel> resTable;

    @FXML
    private TreeView<String> collectionTree = new TreeView<>(new TreeItem<>());

    public void start(DataConnection connection) {
        HttpService service = new HttpService();
        Map<String, List<?>> data = service.getFileTree(connection);

        TreeItem<String> rootNode = new TreeItem<>("Arango db files");
        rootNode.setExpanded(true);
        populateTree(data, rootNode);

        this.connection = connection;

        this.tree.setRoot(rootNode);

    }

    public void start1(DataConnection connection) {
        HttpService service = new HttpService();
        Map<String, Map<String, Object>> data = service.getCollectionTree(connection);

        TreeItem<String> rootNode = new TreeItem<>(connection.getCollection() + " data");
        rootNode.setExpanded(true);
        populateTree1(data, rootNode);

        this.collectionTree.setRoot(rootNode);

    }

    private void populateTree1( Map<String, Map<String, Object>> data, TreeItem<String> parent) {
        for (Map.Entry<String, Map<String, Object>> entry : data.entrySet()) {
            TreeItem<String> item = new TreeItem<>(entry.getKey());
            parent.getChildren().add(item);
            for (Map.Entry<String, Object> entry1 : entry.getValue().entrySet()) {
                TreeItem<String> key = new TreeItem<>(entry1.getKey());
                item.getChildren().add(key);
                key.getChildren().add(new TreeItem<>(String.valueOf(entry1.getValue())));
            }
        }
    }

    private void populateTree(Map<String, List<?>> data, TreeItem<String> parent) {
        for (Map.Entry<String, List<?>> entry : data.entrySet()) {
            TreeItem<String> item = new TreeItem<>(entry.getKey());
            parent.getChildren().add(item);

            for (Object name : entry.getValue()) {
                item.getChildren().add(new TreeItem<>(String.valueOf(name)));
            }
        }
    }

    public void selectCollection(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            TreeItem<String> selectedItem = this.tree.getSelectionModel().getSelectedItem();
            String name = selectedItem == null ? null : selectedItem.getValue() ;
            this.connection.setCollection(name);
            start1(this.connection);
        }
    }

    public static String mapToString(Map<String, Map<String, Object>> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
            for (Map.Entry<String, Object> item : entry.getValue().entrySet()) {
                sb.append(item.getKey()).append(" ").append(item.getValue()).append("\n");
            }
        }
        return sb.toString();
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
            outputText.clear();
            String line = inputText.getText().trim();
            HttpService service = new HttpService();
            Map<String, Map<String, Object>> data = service.doingQuarry(new Quarry(this.connection, line));


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
            outputText.setText("Completed successfully!");

        });

        connectButton.setOnAction(e -> {
            connectButton.getScene().getWindow().hide();

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
