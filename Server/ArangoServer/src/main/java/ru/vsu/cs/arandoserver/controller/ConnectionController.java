package ru.vsu.cs.arandoserver.controller;

import com.arangodb.ArangoCollection;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vsu.cs.arandoserver.entity.DataConnection;
import ru.vsu.cs.arandoserver.entity.Query;
import ru.vsu.cs.arandoserver.service.ConnectionService;

import java.util.List;
import java.util.Map;

@RestController
public class ConnectionController {
    private final ConnectionService service;

    public ConnectionController(ConnectionService service) {
        this.service = service;
    }

    @PostMapping("/database/create")
    public void createNewTable(@RequestBody DataConnection connection) {
        service.createTable(connection);
    }

    @PostMapping("/database/delete")
    public void dropTable(@RequestBody DataConnection connection) {
        service.dropTable(connection);
    }

    @PostMapping("/database/check")
    public void checkConnectionTable(@RequestBody DataConnection connection) {
        service.testConnection(connection);
    }

    @PostMapping("/quarry")
    public List<Object> doQuarry(@RequestBody Query query) {
        return service.doQuery(query);
    }

    @PostMapping("/database/files")
    public Map<String, List<ArangoCollection>> getDbFiles(@RequestBody DataConnection connection) {
        return service.getFileNames(connection);
    }

    @PostMapping("/collection/files")
    public Map<String, Map<String, Object>> getCollectionFiles(@RequestBody DataConnection connection) {
        return service.getCollectionData(connection);
    }

    @PostMapping("/collection/create")
    public void createCollection(@RequestBody DataConnection connection) {
        service.createCollection(connection);
    }
    @PostMapping("/collection/delete")
    public void deleteCollection(@RequestBody DataConnection connection) {
        service.deleteCollection(connection);
    }
}
