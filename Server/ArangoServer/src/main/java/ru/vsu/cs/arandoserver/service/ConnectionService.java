package ru.vsu.cs.arandoserver.service;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.BaseDocument;
import org.springframework.stereotype.Service;
import ru.vsu.cs.arandoserver.configuration.ArangoConnection;
import ru.vsu.cs.arandoserver.entity.DataConnection;

import java.util.*;


@Service
public class ConnectionService {

    public boolean testConnection(DataConnection connection) throws ArangoDBException {
        ArangoConnection ac = new ArangoConnection(connection);
        return ac.getArangoDB().db(connection.getDbName()).getVersion().getVersion() != null;
    }

    public void close(ArangoDB arangoDB) {
        arangoDB.shutdown();
    }

    public String doQuarryRequest(String quarry) {
        if (quarry != null) {
            return "OK";
        } else {
            return "Не ОК";
        }

    }

    public void createTable(DataConnection connection) {
        ArangoConnection ac = new ArangoConnection(connection);
        ac.getArangoDB().createDatabase(connection.getDbName());
    }

    public void dropTable(DataConnection connection) {
        ArangoConnection ac = new ArangoConnection(connection);
        ac.getArangoDB().db(connection.getDbName()).drop();
    }

    public Map<String, List<String>> getFileNames(DataConnection connection) {
        ArangoConnection ac = new ArangoConnection(connection);

        Map<String, List<String>> fileTree = new HashMap<>();
        List<String> names = new ArrayList<>();
        for (var item : ac.getArangoDB().db(connection.getDbName()).getCollections()) {
            names.add(item.getName());
        }
        fileTree.put(connection.getDbName(), names);

        return fileTree;
    }

    public Map<String, Map<String, Object>> getCollectionData(DataConnection connection) {
        Map<String, Map<String, Object>> colData = new HashMap<>();

        ArangoConnection ac = new ArangoConnection(connection);
        String query = "FOR d IN " + connection.getCollection() + " RETURN d";
        ArangoCursor<BaseDocument> cursor = ac.getArangoDB().
                db(connection.getDbName()).
                query(query,  BaseDocument.class);

        while (cursor.hasNext()) {
            BaseDocument document = cursor.next();
            colData.put(document.getKey(), document.getProperties());
        }

        return colData;
    }
}
