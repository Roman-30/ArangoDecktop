package ru.vsu.cs.arandoserver.service;

import com.arangodb.ArangoCollection;
import com.arangodb.entity.CollectionEntity;
import com.arangodb.entity.CollectionPropertiesEntity;
import org.springframework.stereotype.Service;
import ru.vsu.cs.arandoserver.configuration.ArangoConnection;
import ru.vsu.cs.arandoserver.entity.DataConnection;
import ru.vsu.cs.arandoserver.entity.Quarry;
import ru.vsu.cs.arandoserver.repository.ArangoRepositor;

import java.util.Collection;
import java.util.List;
import java.util.Map;


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

    public String doQuarry(Quarry quarry) {
        //ArangoConnection arangoConnection = new ArangoConnection(quarry.getConnection());
        //repository.insertData(quarry.getQuarry());



        return "JE";
    }

    public Map<String, List<String>> doSomething(DataConnection connection) {
        ArangoConnection arangoConnection = new ArangoConnection(connection);
        return arangoConnection.doSomething();
    }
}
