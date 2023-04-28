package vsu.csf.arangodbdecktop.model;

import java.util.Objects;


public class DataConnection {
    private String dbName;
    private String host;
    private Integer port;
    private String userName;
    private String password;
    private String collection;

    public DataConnection(String dbName, String host, Integer port, String userName, String password) {
        this.dbName = dbName;
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.password = password;
    }

    public DataConnection(String dbName, String host, Integer port, String userName, String password, String collection) {
        this(dbName, host, port, userName, password);
        this.collection = collection;
    }

    public DataConnection() {
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("\"dbName\":\"%s\",\"host\":\"%s\",\"port\":\"%s\",\"userName\":\"%s\",\"password\":\"%s\"",
                this.dbName, this.host, this.port, this.userName, this.password
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null) return false;
        DataConnection that = (DataConnection) o;

        return Objects.equals(dbName, that.dbName) &&
                Objects.equals(host, that.host) &&
                Objects.equals(port, that.port) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(password, that.password);
    }



    @Override
    public int hashCode() {
        int result = dbName != null ? dbName.hashCode() : 0;
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + (port != null ? port.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
