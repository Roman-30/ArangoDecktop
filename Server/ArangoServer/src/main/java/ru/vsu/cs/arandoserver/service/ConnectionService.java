package ru.vsu.cs.arandoserver.service;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.IndexEntity;
import org.springframework.stereotype.Service;
import ru.vsu.cs.arandoserver.configuration.ArangoConnection;
import ru.vsu.cs.arandoserver.entity.DataConnection;
import ru.vsu.cs.arandoserver.entity.Quarry;

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

    public Map<String, Map<String, Object>> doQuarryRequest(Quarry quarry) {
        Map<String, Map<String, Object>> data = new HashMap<>();
        BaseDocument document;
        ArangoConnection ac = new ArangoConnection(quarry.getConnection());
        ArangoCursor<BaseDocument> cursor;
        String ads = quarry.getQuarry().trim().substring(0, 3).toLowerCase(Locale.ROOT);
        if (ads.equals("for")) {
            cursor = ac.getArangoDB().
                    db(quarry.getConnection().getDbName()).
                    query(quarry.getQuarry(), BaseDocument.class);
            while (cursor.hasNext()) {
                if ((document = cursor.next()) != null) {
                    data.put(document.getKey(), document.getProperties());
                }
            }
        } else {
            ac.getArangoDB().
                    db(quarry.getConnection().getDbName()).
                    query(quarry.getQuarry(), BaseDocument.class);
        }

        return data;
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

        for (IndexEntity i : ac.getArangoDB().db(connection.getDbName()).collection("test").getIndexes()) {
            System.out.println(i.getName());
        }

        ArangoCollection sd =  ac.getArangoDB().db(connection.getDbName()).collection("test");

        Map<String, List<String>> fileTree = new HashMap<>();
        List<String> names = new ArrayList<>();
        for (var item : ac.getArangoDB().db(connection.getDbName()).getCollections()) {
            names.add(item.getName());

        }
        fileTree.put(connection.getDbName(), names);

        return fileTree;
    }

    public Map<String, List<ArangoCollection>> getFileNames1(DataConnection connection) {
        ArangoConnection ac = new ArangoConnection(connection);

        Map<String, List<ArangoCollection>> fileTree = new HashMap<>();
        List<ArangoCollection> names = new ArrayList<>();
        for (var item : ac.getArangoDB().db(connection.getDbName()).getCollections()) {
            names.add(ac.getArangoDB().db(connection.getDbName()).collection(item.getName()));
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
                query(query, BaseDocument.class);

        while (cursor.hasNext()) {
            BaseDocument document = cursor.next();
            colData.put(document.getKey(), document.getProperties());
        }
        return colData;
    }
}
