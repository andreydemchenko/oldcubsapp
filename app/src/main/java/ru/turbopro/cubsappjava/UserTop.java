package ru.turbopro.cubsappjava;

import java.util.ArrayList;

public class UserTop {
    private String name;
    private String status;
    private String points;
    private ArrayList<String> achiv_progress;
    private int imageId;

    public UserTop(String name, String status, String points, ArrayList<String> achiv_progress, int imageId) {
        this.name = name;
        this.status = status;
        this.points = points;
        this.achiv_progress = achiv_progress;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getPoints() {
        return points;
    }

    public ArrayList<String> getAchiv_progress() {
        return achiv_progress;
    }

    public int getImageId() {
        return imageId;
    }
}
