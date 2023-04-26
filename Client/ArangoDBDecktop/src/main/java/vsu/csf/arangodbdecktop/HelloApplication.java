package vsu.csf.arangodbdecktop;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ArangoDBConstructor.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.control.TreeItem;
//import javafx.scene.control.TreeView;
//import javafx.scene.layout.BorderPane;
//import javafx.stage.Stage;
//
//import java.io.File;
//
//public class HelloApplication extends Application {
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        BorderPane root = new BorderPane();
//
//        // Create the root directory
//        File rootDirectory = new File("C:/");
//        TreeItem<String> rootNode = new TreeItem<>(rootDirectory.getName());
//        rootNode.setExpanded(true);
//
//        // Populate the tree with directories
//        populateTree(rootDirectory, rootNode);
//
//        // Create the tree view
//        TreeView<String> treeView = new TreeView<>(rootNode);
//        root.setCenter(treeView);
//
//        Scene scene = new Scene(root, 400, 400);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    private void populateTree(File directory, TreeItem<String> parent) {
//        for (File file : directory.listFiles()) {
//            TreeItem<String> item = new TreeItem<>(file.getName());
//            parent.getChildren().add(item);
////            if (file.isDirectory()) {
////                populateTree(file, item);
////            }
//        }
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//}
