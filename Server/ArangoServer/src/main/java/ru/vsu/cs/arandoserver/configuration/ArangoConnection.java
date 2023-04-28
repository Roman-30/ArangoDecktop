package ru.vsu.cs.arandoserver.configuration;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
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

    private ArangoDB arangoDB;

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


//    public Map<String, List<String>> doSomething() {
//        Map<String, List<String>> fileTree = new HashMap<>();
//        for (var dbs : this.arangoDB.getDatabases()) {
//            List<String> names = new ArrayList<>();
//            for (var item : this.arangoDB.db(dbs).getCollections()) {
//                names.add(item.getName());
//            }
//            fileTree.put(dbs, names);
//        }
//        return fileTree;
//    }

}

