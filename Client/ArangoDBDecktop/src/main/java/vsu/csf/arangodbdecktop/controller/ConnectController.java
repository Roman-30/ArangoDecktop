package vsu.csf.arangodbdecktop.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import vsu.csf.arangodbdecktop.ClientApplication;
import vsu.csf.arangodbdecktop.model.DataConnection;
import vsu.csf.arangodbdecktop.service.FileService;
import vsu.csf.arangodbdecktop.service.HttpService;
import vsu.csf.arangodbdecktop.util.WindowUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ConnectController {

    private List<DataConnection> connections;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button canselButton;

    @FXML
    private Button createButton;

    @FXML
    private TableColumn<DataConnection, String> dbName;

    @FXML
    private Button editButton;

    @FXML
    private TableColumn<DataConnection, String> host;

    @FXML
    private TableColumn<DataConnection, Integer> port;

    @FXML
    private Button removeButton;

    @FXML
    private TableColumn<DataConnection, String> userName;

    @FXML
    private Button testButton;

    @FXML
    private TableView<DataConnection> table;

    private void deleteData(DataConnection data) {
        List<DataConnection> dataConnections = FileService.readConnection(FileService.ALL_DATA_BASE_PASS);
        dataConnections.remove(data);
        FileService.writeConnection(dataConnections, FileService.ALL_DATA_BASE_PASS);
    }

    private void create() {

        dbName.setCellValueFactory(new PropertyValueFactory<>("dbName"));
        userName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        host.setCellValueFactory(new PropertyValueFactory<>("host"));
        port.setCellValueFactory(new PropertyValueFactory<>("port"));

    }

    public void selectContractContractTab(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            var data = table.getSelectionModel().getSelectedItem();

            canselButton.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientApplication.class.getResource("ArangoDBConstructor.fxml"));

            try {
                loader.load();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            FileService.writeConnection(List.of(data), FileService.CURRENT_DATA_BASE_PASS);

            MainController sd = loader.getController();
            sd.createDbFilesTree(data);
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.show();
        }
    }

    @FXML
    void initialize() {
        create();

        connections = FileService.readConnection(FileService.ALL_DATA_BASE_PASS);
        table.getItems().addAll(connections);

        removeButton.setOnAction(e -> {
            DataConnection selectedData = table.getSelectionModel().getSelectedItem();
            HttpService service = new HttpService();

            int statusCode = service.deleteDb(selectedData);
            Alert alert;
            if (statusCode >= 200 && statusCode < 300) {
                deleteData(selectedData);
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Deleted is successful!");
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Deleted is error!");
            }
            alert.setTitle("Message");
            alert.showAndWait();
            removeButton.getScene().getWindow().hide();
            WindowUtils.loadWindow("ConnectWindow.fxml", false);
        });

        testButton.setOnAction(e -> {
            DataConnection selectedData = table.getSelectionModel().getSelectedItem();
            HttpService service = new HttpService();

            int statusCode = service.checkConnectionDb(selectedData);
            Alert alert;
            if (statusCode >= 200 && statusCode < 300) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Connected is successful!");
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Connected is error!");
            }
            alert.setTitle("Message");
            alert.showAndWait();

        });

        editButton.setOnAction(e -> {
            editButton.getScene().getWindow().hide();
            DataConnection selectedData = table.getSelectionModel().getSelectedItem();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientApplication.class.getResource("EditWindow.fxml"));

            try {
                if (Objects.equals(selectedData, new DataConnection())) {
                    throw new Exception();
                }

                loader.load();
                Parent root = loader.getRoot();
                EditController sd = loader.getController();
                sd.setValue(selectedData);
                sd.setConnection(selectedData);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        createButton.setOnAction(e -> {
            createButton.getScene().getWindow().hide();
            WindowUtils.loadWindow("CreateWindow.fxml", false);
        });

        canselButton.setOnAction(e -> {
            canselButton.getScene().getWindow().hide();
            WindowUtils.loadWindow("ArangoDBConstructor.fxml", false);
        });
    }

}
