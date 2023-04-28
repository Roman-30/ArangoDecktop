package vsu.csf.arangodbdecktop.model;

public class Quarry {

    private DataConnection connection;

    private String quarry;

    public Quarry(DataConnection connection, String quarry) {
        this.connection = connection;
        this.quarry = quarry;
    }

    public DataConnection getConnection() {
        return connection;
    }

    public void setConnection(DataConnection connection) {
        this.connection = connection;
    }

    public String getQuarry() {
        return quarry;
    }

    public void setQuarry(String quarry) {
        this.quarry = quarry;
    }
    @Override
    public String toString() {
        return String.format("\"quarry\":\"%s\"", this.quarry);
    }
}
