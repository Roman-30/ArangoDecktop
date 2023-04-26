package vsu.csf.arangodbdecktop.service;

import vsu.csf.arangodbdecktop.controllers.DataConnection;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileService {
    private static final String FILE_PATH = "src\\main\\java\\vsu\\csf\\arangodbdecktop\\file\\connection_data.txt";

    public static void writeConnection(List<DataConnection> dataConnections) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write("Database Name, Host, Port, User Name, Password"+"\n");

            for (DataConnection dataConnection : dataConnections) {
                writer.write(dataConnection.getDbName() + "," +
                        dataConnection.getHost() + "," +
                        dataConnection.getPort() + "," +
                        dataConnection.getUserName() + "," +
                        dataConnection.getPassword());
                writer.write("\n");
                }
            System.out.println("DataConnectionDto list written to file successfully!");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static List<DataConnection> readConnection() {
        List<DataConnection> dataConnections = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                DataConnection dataConnection = new DataConnection();
                dataConnection.setDbName(tokens[0]);
                dataConnection.setHost(tokens[1]);
                dataConnection.setPort(Integer.parseInt(tokens[2]));
                dataConnection.setUserName(tokens[3]);
                dataConnection.setPassword(tokens[4]);
                dataConnections.add(dataConnection);
            }
            System.out.println("DataConnectionDto list read from file successfully!");
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }

        return dataConnections;
    }
}

