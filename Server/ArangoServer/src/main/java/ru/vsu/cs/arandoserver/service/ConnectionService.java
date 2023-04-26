package ru.vsu.cs.arandoserver.service;

import org.springframework.stereotype.Service;
import ru.vsu.cs.arandoserver.configuration.ArangoConnection;
import ru.vsu.cs.arandoserver.entity.DataConnection;
import ru.vsu.cs.arandoserver.entity.Quarry;


@Service
public class ConnectionService {

    public void createTable(DataConnection connection) {
        ArangoConnection arangoConnection = new ArangoConnection(connection);
        arangoConnection.create(connection.getDbName());
    }

    public void dropTable(DataConnection connection) {
        ArangoConnection arangoConnection = new ArangoConnection(connection);
        arangoConnection.delete(connection.getDbName());
    }

    public void isConnected(DataConnection connection) {
        ArangoConnection arangoConnection = new ArangoConnection(connection);
        arangoConnection.testConnection(connection.getDbName());
    }

    public String doQuarry(DataConnection connection, Quarry quarry) {
        ArangoConnection arangoConnection = new ArangoConnection(connection);
        return arangoConnection.doQuarryRequest(quarry);
    }
}
