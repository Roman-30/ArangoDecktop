package ru.vsu.cs.arandoserver.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vsu.cs.arandoserver.entity.DataConnection;
import ru.vsu.cs.arandoserver.entity.Quarry;
import ru.vsu.cs.arandoserver.service.ConnectionService;

import java.util.List;
import java.util.Map;

@RestController
public class ConnectionController {
    private final ConnectionService service;

    public ConnectionController(ConnectionService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public void createNewTable(@RequestBody DataConnection connection) {
         service.createTable(connection);
    }

    @PostMapping("/delete")
    public void dropTable(@RequestBody DataConnection connection) {
        service.dropTable(connection);
    }

    @PostMapping("/check")
    public void checkConnectionTable(@RequestBody DataConnection connection) {
        service.testConnection(connection);
    }

    @PostMapping("/quarry")
    public Map<String, Map<String, Object>> doQuarry(@RequestBody Quarry quarry) {
        return service.doQuarryRequest(quarry);
    }

    @PostMapping("/st")
    public Map<String, List<String>> doS(@RequestBody DataConnection connection) {
        return service.getFileNames(connection);
    }

    @PostMapping("/cf")
    public Map<String, Map<String, Object>> doSs(@RequestBody DataConnection connection) {
        return service.getCollectionData(connection);
    }


}
