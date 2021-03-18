package com.example.w9finnkino;

import java.util.ArrayList;

public class Theaters {
    private ArrayList<Theater> theaterList;
    private static Theaters theaters = new Theaters();

    public static Theaters getInstance() {
        return theaters;
    }

    public Theaters() {
        theaterList = new ArrayList<Theater>();
    }

    public void clearTheaterList() {
        theaterList.clear();
    }

    public void addTheater(String location, String id) {
        theaterList.add(new Theater(location, id));
    }

    public ArrayList<Theater> getTheaterList() {
        return  theaterList;
    }
}
