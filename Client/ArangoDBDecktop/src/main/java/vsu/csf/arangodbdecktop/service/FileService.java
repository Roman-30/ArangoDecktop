package vsu.csf.arangodbdecktop.service;

import com.google.bigtable.repackaged.io.grpc.xds.shaded.io.envoyproxy.envoy.admin.v3.RoutesConfigDump;
import vsu.csf.arangodbdecktop.ClientApplication;
import vsu.csf.arangodbdecktop.model.DataConnection;
import vsu.csf.arangodbdecktop.model.QueryPattern;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileService {

    public static final String ALL_DATA_BASE_PASS = "C:\\Users\\romse\\OneDrive\\Документы\\GitHub\\ArangoDecktop\\Client\\ArangoDBDecktop\\src\\main\\resources\\files\\connection_data.txt";
    public static final String CURRENT_DATA_BASE_PASS = "C:\\Users\\romse\\OneDrive\\Документы\\GitHub\\ArangoDecktop\\Client\\ArangoDBDecktop\\src\\main\\resources\\files\\current_data_base.txt";


    public static void writeConnection(List<DataConnection> dataConnections, String path) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write("Database Name, Host, Port, User Name, Password" + "\n");

            for (DataConnection dataConnection : dataConnections) {
                writer.write(dataConnection.getDbName() + "," +
                        dataConnection.getHost() + "," +
                        dataConnection.getPort() + "," +
                        dataConnection.getUserName() + "," +
                        dataConnection.getPassword());
                writer.write("\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static List<DataConnection> readConnection(String path) {
        List<DataConnection> dataConnections = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
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
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }

        return dataConnections;
    }

    public static void writeHistory(String query, String name) {
        String path = "C:\\Users\\romse\\OneDrive\\Документы\\GitHub\\ArangoDecktop\\Client\\ArangoDBDecktop\\src\\main\\resources\\files\\" + name + "_history.txt";

        try {
            FileWriter writer = new FileWriter(path, true);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.write(query + "\n");
            bufferWriter.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static List<String> readHistory(String name) {
        String path = "C:\\Users\\romse\\OneDrive\\Документы\\GitHub\\ArangoDecktop\\Client\\ArangoDBDecktop\\src\\main\\resources\\files\\" + name + "_history.txt";

        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }

        return lines;
    }

    public static void writePattern(String fileName, QueryPattern pattern) {
        String path = "C:\\Users\\romse\\OneDrive\\Документы\\GitHub\\ArangoDecktop\\Client\\ArangoDBDecktop\\src\\main\\resources\\files\\" + fileName + "_patterns.txt";

        try {
            FileWriter writer = new FileWriter(path, true);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.write(pattern.toString() + "\n");
            bufferWriter.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static List<QueryPattern> readPattern(String fileName) {
        String path = "C:\\Users\\romse\\OneDrive\\Документы\\GitHub\\ArangoDecktop\\Client\\ArangoDBDecktop\\src\\main\\resources\\files\\" + fileName + "_patterns.txt";

        List<QueryPattern> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fragm = line.split("`");
                lines.add(new QueryPattern(fragm[0], fragm[1]));
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }

        return lines;
    }
}

