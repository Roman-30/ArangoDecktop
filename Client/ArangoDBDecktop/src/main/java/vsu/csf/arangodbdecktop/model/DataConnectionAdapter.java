package vsu.csf.arangodbdecktop.model;

import com.google.bigtable.repackaged.com.google.gson.TypeAdapter;
import com.google.bigtable.repackaged.com.google.gson.stream.JsonReader;
import com.google.bigtable.repackaged.com.google.gson.stream.JsonWriter;
import vsu.csf.arangodbdecktop.model.DataConnection;

import java.io.IOException;

public class DataConnectionAdapter extends TypeAdapter<DataConnection> {

    @Override
    public void write(JsonWriter out, DataConnection value) throws IOException {
        out.beginObject();
        out.name("dbName").value(value.getDbName());
        out.name("host").value(value.getHost());
        out.name("port").value(value.getPort());
        out.name("userName").value(value.getUserName());
        out.name("password").value(value.getPassword());
        out.name("collection").value(value.getCollection());
        out.endObject();
    }

    @Override
    public DataConnection read(JsonReader in) throws IOException {
        in.beginObject();
        String dbName = null;
        String host = null;
        int port = 0;
        String username = null;
        String password = null;
        String collection = null;
        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "dbName" -> dbName = in.nextString();
                case "host" -> host = in.nextString();
                case "port" -> port = in.nextInt();
                case "userName" -> username = in.nextString();
                case "password" -> password = in.nextString();
                case "collection" -> collection = in.nextString();
                default -> in.skipValue();
            }
        }
        in.endObject();
        return new DataConnection(dbName, host, port, username, password, collection);
    }

}
