package vsu.csf.arangodbdecktop.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import vsu.csf.arangodbdecktop.ClientApplication;
import vsu.csf.arangodbdecktop.model.*;
import vsu.csf.arangodbdecktop.service.FileService;
import vsu.csf.arangodbdecktop.service.HttpService;
import vsu.csf.arangodbdecktop.util.WindowUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController {


    private DataConnection connection;
    @FXML
    private ResourceBundle resources;

    @FXML
    private Button crColllectionButton;
    @FXML
    private ListView<String> viewPatterns = new ListView<>();
    @FXML
    private ComboBox<String> patternsBox = new ComboBox<>();
    @FXML
    private ListView<String> historyList = new ListView<>();
    @FXML
    private URL location;
    @FXML
    private AnchorPane left;
    @FXML
    private Tab mainT;

    @FXML
    private Button deleteButton;

    @FXML
    private Button patternButton;

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
    private Button crCollectionButton;

    @FXML
    private Button deleteCollectionButton;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button doingButton;

    @FXML
    private ComboBox<String> fileFormatBox;
    @FXML
    private Spinner<Integer> outCount;

    @FXML
    private Button saveFileButton;
    @FXML
    private TableView<ResultModel> resTable;

    @FXML
    private TreeView<String> collectionTree = new TreeView<>(new TreeItem<>());

    public DataConnection getConnection() {
        return this.connection;
    }

    public void createDbFilesTree(DataConnection connection) {
        HttpService service = new HttpService();
        Map<String, List<?>> data = service.getFileTree(connection);

        TreeItem<String> rootNode = new TreeItem<>("Arango db files");
        rootNode.setExpanded(true);
        setTreeChildren(data, rootNode);

        this.connection = new DataConnection(connection);

        this.tree.setRoot(rootNode);

    }

    public void getOnClick(String name) {
        HttpService service = new HttpService();
        String queryRequest = String.format(QueryPattern.FIND_ALL, name);
        showResult(service, queryRequest);
        inputText.setText(queryRequest);
        FileService.writeHistory(queryRequest, this.connection.getDbName());
        new Thread(this::updateHistory).start();
        outputText.setText("Completed successfully!");
    }

    private void showResult(HttpService service, String queryRequest) {
        List<?> data = service.doingQuarry(
                new Quarry(this.connection, queryRequest));

        if (data.isEmpty()) return;

        if (data.get(0) instanceof Map<?, ?>) {

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

    private void updateHistory() {
        try {
            List<String> history = FileService.readHistory(this.connection.getDbName());
            if (history.size() > 0) {
                historyList.getItems().clear();
                for (String item : history) {
                    historyList.getItems().add(item);
                }
            }

        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    private void updatePatternsName() {
        try {
            List<QueryPattern> patterns = FileService.readPattern(connection.getDbName());
            Set<String> lines = new HashSet<>();
            if (patterns.size() > 0) {
                patternsBox.getItems().clear();
                for (QueryPattern pattern : new HashSet<>(patterns)) {
                    lines.add(pattern.getName());
                }

                lines.forEach(item -> {
                    patternsBox.getItems().add(item);
                });
                patternsBox.getItems().add(0, "All");
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    private void setTreeChildren(Map<String, List<?>> data, TreeItem<String> parent) {
        List<String> components = List.of("_analyzers",
                "_frontend",
                "_graphs",
                "_aqlfunctions",
                "_queues",
                "_jobs",
                "_apps",
                "_appbundles");

        for (Map.Entry<String, List<?>> entry : data.entrySet()) {
            TreeItem<String> item = new TreeItem<>(entry.getKey());
            parent.getChildren().add(item);

            for (Object name : entry.getValue()) {
                TreeItem<String> item1 = new TreeItem<>();
                TreeItem<String> item2 = new TreeItem<>("indexes");
                TreeItem<String> item3 = new TreeItem<>("fields");
                if (name instanceof Map<?, ?> map) {
                    if (map.get("info") instanceof Map<?, ?> map1) {
                        if (!components.contains(map1.get("name").toString())) {
                            item1.setValue(String.valueOf(map1.get("name")));
                            item1.getChildren().add(item2);
                            item1.getChildren().add(item3);
                        }
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
                getOnClick(name);
            }
        }
    }

    private void setDataOnTextArea(ListView<String> historyList) {
        historyList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                String query = historyList.getSelectionModel().getSelectedItem();

                Node node = ((Parent) tabPane.getSelectionModel()
                        .getSelectedItem().getContent())
                        .getChildrenUnmodifiable().get(0);
                Node node1 = ((Parent) node).getChildrenUnmodifiable().get(0);
                Node node2 = ((Parent) node1).getChildrenUnmodifiable().get(0);
                ((TextArea) ((Parent) node2).getChildrenUnmodifiable().get(0)).setText(query);
            }
        });
    }

    private void setFileFormat() {
        this.outCount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100));
        this.fileFormatBox.getItems().add("CSV");
        this.fileFormatBox.getItems().add("Json");
        this.fileFormatBox.getItems().add("TXT");
        this.fileFormatBox.getItems().add("DAT");
    }

    @FXML
    void initialize() {
        connection = FileService.readConnection(FileService.CURRENT_DATA_BASE_PASS).get(0);

        setFileFormat();

        updatePatternsName();
        updateHistory();

        setDataOnTextArea(historyList);

        setDataOnTextArea(viewPatterns);

        insertButton.setOnAction(e -> {
            inputText.clear();
            inputText.setText(String.format(QueryPattern.INSERT, "\"your collection\""));
        });

        patternButton.setOnAction(e -> {
            WindowUtils.loadWindow("CreatePatternWindow.fxml", true);
            updatePatternsName();
        });

        connectButton.setOnAction(e -> {
            connectButton.getScene().getWindow().hide();
            WindowUtils.loadWindow("ConnectWindow.fxml", false);
        });

        crCollectionButton.setOnAction(e -> {
            WindowUtils.loadWindow("AddCollectionWindow.fxml", true);
            createDbFilesTree(FileService.readConnection(FileService.CURRENT_DATA_BASE_PASS).get(0));
        });

        deleteButton.setOnAction(e -> {
            inputText.clear();
            inputText.setText(String.format(QueryPattern.DELETE_ALL, "\"your collection\""));
        });

        findButton.setOnAction(e -> {
            inputText.clear();
            inputText.setText(String.format(QueryPattern.FIND_ALL, "\"your collection\""));
        });

        patternsBox.setOnAction(e -> {
            try {
                List<QueryPattern> patterns = FileService.readPattern(connection.getDbName());
                String name = patternsBox.getSelectionModel().getSelectedItem();
                viewPatterns.getItems().clear();

                if (Objects.equals(name, "All")) {
                    patterns.forEach(item -> viewPatterns.getItems().add(item.getQuery()));
                } else {
                    for (QueryPattern pattern : patterns) {
                        if (Objects.equals(pattern.getName(), name)) {
                            viewPatterns.getItems().add(pattern.getQuery());
                        }
                    }
                }
            } catch (Exception e1) {
                e1.fillInStackTrace();
            }
        });

        deleteCollectionButton.setOnAction(e -> {
            TreeItem<String> selectedItem = this.tree.getSelectionModel().getSelectedItem();
            String name = selectedItem == null ? null : selectedItem.getValue();

            if (name != null) {
                DataConnection data = FileService.readConnection(FileService.CURRENT_DATA_BASE_PASS).get(0);
                data.setCollection(name);

                HttpService service = new HttpService();

                int code = service.deleteCollection(data);

                Alert alert;
                if (code < 300) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Message");
                    alert.setHeaderText("Delete is successful!");
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Message");
                    alert.setHeaderText("Delete Error!");
                }
                alert.showAndWait();

                createDbFilesTree(data);
            }
        });


        doingButton.setOnAction(e -> {
            new Thread(() -> {
                outputText.clear();
                String line = inputText.getText().trim()
                        .replace("\n", " ")
                        .replace("\"", "'");
                HttpService service = new HttpService();
                showResult(service, line);
                FileService.writeHistory(line, this.connection.getDbName());
                outputText.setText("Completed successfully!");
                updateHistory();
            }).start();
        });
    }
}
