package vsu.csf.arangodbdecktop.model;

public class QueryPattern {

    public static final String FIND_ALL = "FOR my IN %s RETURN my";
    public static final String DELETE_ALL = "FOR my IN %s REMOVE my";
    public static final String INSERT = "INSERT {\"@key\": @val} INTO %s";
    private String name;

    private String query;

    public QueryPattern(String name, String query) {
        this.name = name;
        this.query = query;
    }

    public QueryPattern() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return String.format("%s`%s", this.name, this.query);
    }
}
