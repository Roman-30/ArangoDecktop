package ru.vsu.cs.arandoserver.configuration;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import ru.vsu.cs.arandoserver.entity.DataConnection;
import ru.vsu.cs.arandoserver.entity.Quarry;


public class ArangoConnection {

    private final ArangoDB arangoDB;

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

    public boolean testConnection(String databaseName) throws ArangoDBException {
        return this.arangoDB.db(databaseName).getVersion().getVersion() != null;
    }

    public void create(String databaseName) throws ArangoDBException {
        this.arangoDB.createDatabase(databaseName);
    }

    public boolean delete(String databaseName) throws ArangoDBException {
        return this.arangoDB.db(databaseName).drop();
    }

    public void close() {
        this.arangoDB.shutdown();
    }

    public String doQuarryRequest(Quarry quarry) {
        if (quarry != null) {
            return "OK";
        } else {
          return "ХУЙ";
        }
    }
}

