
module vsu.csf.arangodbdecktop {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires arangodb.java.driver;
    requires com.fasterxml.jackson.databind;
    requires com.arangodb.jackson.dataformat.velocypack;
    requires arangodb.spark.datasource;
    requires arangodb.spark.commons;
    requires bigtable.hbase;
    requires okhttp3;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires org.json;

    opens vsu.csf.arangodbdecktop to javafx.fxml;
    exports vsu.csf.arangodbdecktop;
    exports vsu.csf.arangodbdecktop.controller;
    opens vsu.csf.arangodbdecktop.controller to javafx.fxml;
    exports vsu.csf.arangodbdecktop.model;
    opens vsu.csf.arangodbdecktop.model to javafx.fxml;
    exports vsu.csf.arangodbdecktop.util;
    opens vsu.csf.arangodbdecktop.util to javafx.fxml;
}
