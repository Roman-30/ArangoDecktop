package ru.vsu.cs.arandoserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vsu.cs.arandoserver.entity.DataConnection;
import ru.vsu.cs.arandoserver.entity.Quarry;
import ru.vsu.cs.arandoserver.service.ConnectionService;

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
        service.isConnected(connection);
    }

    @PostMapping("/quarry")
    public void doQuarry(@RequestBody DataConnection connection, @RequestBody Quarry quarry) {
        System.out.println(quarry);
        System.out.println(connection);

    }
}
