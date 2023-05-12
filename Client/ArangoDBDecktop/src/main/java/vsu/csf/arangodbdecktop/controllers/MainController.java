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
import vsu.csf.arangodbdecktop.model.QueryPatterns;
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
    private Tab mainT;

    @FXML
    private Button deleteButton;

    @FXML
    private Button insertButton;

    @FXML
    private Tab addTab;

    @FXML
    private Button findButton;
    @FXML
    private TextArea inputText;

    @FXML
    private TextArea outputText;

    @FXML
    private Button connectButton;

    @FXML
    private TreeView<String> tree = new TreeView<>(new TreeItem<>());

    @FXML
    private Button crColllectionButton;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button doingButton;

//    @FXML
//    void doSomething(ActionEvent event) {
//        System.out.println("hello");
//    }

    @FXML
    private TableView<ResultModel> resTable;

    @FXML
    private TreeView<String> collectionTree = new TreeView<>(new TreeItem<>());

    public DataConnection getConnection() {
        return this.connection;
    }

    public void start(DataConnection connection) {
        HttpService service = new HttpService();
        Map<String, List<?>> data = service.getFileTree(connection);

        TreeItem<String> rootNode = new TreeItem<>("Arango db files");
        rootNode.setExpanded(true);
        populateTree(data, rootNode);

        this.connection = new DataConnection(connection);

        this.tree.setRoot(rootNode);

    }

    public void start1(String name) {
        HttpService service = new HttpService();
        String queryRequest = String.format(QueryPatterns.FIND_ALL, name);
        showResult(service, queryRequest);
        inputText.setText(queryRequest);
        outputText.setText("Completed successfully!");
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

    private void populateTree1(Map<String, Map<String, Object>> data, TreeItem<String> parent) {
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
                TreeItem<String> item1 = new TreeItem<>();
                TreeItem<String> item2 = new TreeItem<>("indexes");
                TreeItem<String> item3 = new TreeItem<>("fields");
                if (name instanceof Map<?, ?> map) {
                    if (map.get("info") instanceof Map<?, ?> map1) {
                        item1.setValue(String.valueOf(map1.get("name")));
                        item1.getChildren().add(item2);
                        item1.getChildren().add(item3);
                    }
                    if (map.get("indexes") instanceof List<?> list)
                        for (Object i : list)
                            if (i instanceof Map<?, ?> map1) {
                                item2.getChildren().add(new TreeItem<>(
                                        String.valueOf(map1.get("name"))));
                                if (map1.get("fields") instanceof List<?> fields)
                                    for (Object field : fields)
                                        item3.getChildren().add(new TreeItem<>(String.valueOf(field)));

                            }
                    item.getChildren().add(item1);
                }
            }
        }
    }


    @FXML
    void createNew(MouseEvent event) {
        if (addTab.isSelected()) {
            FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("TabWindow.fxml"));
            try {
                fxmlLoader.load();
            } catch (IOException es) {
                throw new RuntimeException(es);
            }
            TabController d = fxmlLoader.getController();
            Tab tab = new Tab("Console " + tabPane.getTabs().size());
            tab.setContent(d.getPane());
            tabPane.getTabs().add(tabPane.getTabs().size() - 1, tab);
            tabPane.getSelectionModel().select(tab);
        } else {
            if (event.getClickCount() == 2) {
                tabPane.getTabs().forEach(e -> {
                    if (e.isSelected()) tabPane.getTabs().remove(e);
                });
            }
        }
    }

    public void selectCollection(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            TreeItem<String> selectedItem = this.tree.getSelectionModel().getSelectedItem();
            String name = selectedItem == null ? null : selectedItem.getValue();

            if (name != null) {
                this.connection.setCollection(name);
                start1(name);
            }
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

        crColllectionButton.setOnAction(e -> {
            int statusCode = 200;
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

        deleteButton.setOnAction(e -> {
            inputText.clear();
            inputText.setText("FOR my IN @mycollection REMOVE my IN @mycollection");
        });

        findButton.setOnAction(e -> {
            inputText.clear();
            inputText.setText("FOR my IN @mycollection RETURN my");
        });

        doingButton.setOnAction(e -> {
            new Thread(() -> {
                outputText.clear();
                String line = inputText.getText().trim();

                HttpService service = new HttpService();
                showResult(service, line);
                outputText.setText("Completed successfully!");
            }).start();
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
