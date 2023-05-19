package ru.vsu.cs.arandoserver.service;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDBException;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.BaseDocument;
import org.springframework.stereotype.Service;
import ru.vsu.cs.arandoserver.configuration.ArangoConnection;
import ru.vsu.cs.arandoserver.entity.DataConnection;
import ru.vsu.cs.arandoserver.entity.Query;

import java.util.*;


@Service
public class ConnectionService {
    public boolean testConnection(DataConnection connection) throws ArangoDBException {
        System.out.println(ArangoConnection.getDatabase(connection).getVersion().getVersion());
        return ArangoConnection.getDatabase(connection).getVersion().getVersion() != null;
    }

    public void createTable(DataConnection connection) {
        ArangoConnection ac = new ArangoConnection(connection);
        ac.getArangoDB().createDatabase(connection.getDbName());
        ac.close();
    }

    public void dropTable(DataConnection connection) {
        ArangoConnection.getDatabase(connection).drop();
    }

    public Map<String, List<ArangoCollection>> getFileNames(DataConnection connection) {
        ArangoDatabase db = ArangoConnection.getDatabase(connection);

        Map<String, List<ArangoCollection>> fileTree = new HashMap<>();
        List<ArangoCollection> names = new ArrayList<>();
        for (var item : db.getCollections()) {
            names.add(db.collection(item.getName()));
        }
        fileTree.put(connection.getDbName(), names);

        return fileTree;
    }

    public Map<String, Map<String, Object>> getCollectionData(DataConnection connection) {
        Map<String, Map<String, Object>> colData = new HashMap<>();
        String query = "FOR d IN " + connection.getCollection() + " RETURN d";
        ArangoCursor<BaseDocument> cursor = ArangoConnection
                .getDatabase(connection)
                .query(query, BaseDocument.class);

        while (cursor.hasNext()) {
            BaseDocument document = cursor.next();
            colData.put(document.getKey(), document.getProperties());
        }
        return colData;
    }

    public void createCollection(DataConnection connection) {
        if (ArangoConnection.getCollection(connection).exists()) {
            throw new ArangoDBException("Коллекция уже существует");
        }
        ArangoConnection.getDatabase(connection)
                .createCollection(connection.getCollection());
    }

    public void deleteCollection(DataConnection connection) {
        ArangoConnection.getCollection(connection).drop();
    }

    public List<Object> doQuery(Query query) {
        List<Object> list = new ArrayList<>();
        ArangoCursor<?> cursor;
        String isFor = query.getQuarry().trim().substring(0, 3).toLowerCase(Locale.ROOT);
        if (isFor.equals("for")) {
            cursor = ArangoConnection.getDatabase(query.getConnection()).
                    query(query.getQuarry(), Object.class);
            while (cursor.hasNext()) {
                list.add(cursor.next());
            }
        } else {
            ArangoConnection.getDatabase(query.getConnection()).
                    query(query.getQuarry(), BaseDocument.class);
        }
        return list;
    }
}
