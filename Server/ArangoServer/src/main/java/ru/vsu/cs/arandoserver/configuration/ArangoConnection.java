package ru.vsu.cs.arandoserver.configuration;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.CollectionEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.vsu.cs.arandoserver.entity.DataConnection;

import java.util.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Setter
public class ArangoConnection {

    public ArangoDB arangoDB;
    public static ArangoDatabase db;
    public static ArangoCollection collection;

    public ArangoConnection(String host, int port, String username, String password) {
        this.arangoDB = new ArangoDB.Builder()
                .host(host, port)
                .user(username)
                .password(password)
                .build();
    }

    public ArangoConnection(DataConnection connection) {
        this.arangoDB = new ArangoDB.Builder()
                .host(connection.getHost(), connection.getPort())
                .user(connection.getUserName())
                .password(connection.getPassword())
                .build();
    }

    public static ArangoDatabase getDatabase(DataConnection data) {
        if (db == null) {
            db = new ArangoConnection(data).getArangoDB().db(data.getDbName());
        }
        return db;
    }

    public static ArangoCollection getCollection(DataConnection data) {
        if (collection == null) {
            collection = getDatabase(data).collection(data.getCollection());
        }
        return collection;
    }

    public void close() {
        this.arangoDB.shutdown();
    }
}

