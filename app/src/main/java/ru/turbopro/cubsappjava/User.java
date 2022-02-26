package ru.turbopro.cubsappjava;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class User {

    private String name;
    private String code;
    private String status;
    private int level;
    private int points;
    private int allPoints;
    private String imageURL;

    public User() {}

    public User(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public User(String name, String code, int level, String status, int points, int allPoints, String imageURL){
        this.name = name;
        this.code = code;
        this.status = status;
        this.level = level;
        this.points = points;
        this.allPoints = allPoints;
        this.imageURL = imageURL;
    }

    public int getAllPoints() {
        return allPoints;
    }

    public int getPoints() {
        return points;
    }

    public String getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getLevel() {
        return level;
    }
}
