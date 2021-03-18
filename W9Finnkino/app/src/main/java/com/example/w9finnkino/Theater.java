package com.example.w9finnkino;

public class Theater {
    private String location;
    private String id;

    public Theater(String location, String id) {
        this.location = location;
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return location;
    }
}
