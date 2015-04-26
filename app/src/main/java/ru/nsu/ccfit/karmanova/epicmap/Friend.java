package ru.nsu.ccfit.karmanova.epicmap;

import java.io.Serializable;

/**
 * Created by Константин on 25.04.2015.
 */
public class Friend implements Serializable {
    private String name;
    private int id;

    public Friend(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
