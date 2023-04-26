package ru.vsu.cs.arandoserver.entity;

public class Quarry {

    private String quarry;

    public Quarry(String quarry) {
        this.quarry = quarry;
    }

    public Quarry() {
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
