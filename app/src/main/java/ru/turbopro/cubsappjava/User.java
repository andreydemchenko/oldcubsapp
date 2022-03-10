package ru.turbopro.cubsappjava;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    private String name;
    private String login;
    private String password;
    private String status;
    private int level;
    private int points;
    private int all_points;
    private String user_image_URL;
    //private Long achiv_progress;

    public User() {}

    public User(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public User(String name, String login, int level, String status, int points, int all_points, String user_image_URL){
        this.name = name;
        this.login = login;
        this.status = status;
        this.level = level;
        this.points = points;
        this.all_points = all_points;
        this.user_image_URL = user_image_URL;
       // this.achiv_progress = achiv_progress;
    }

    public int getAll_points() {
        return all_points;
    }

    public int getPoints() {
        return points;
    }

    public String getStatus() {
        return status;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getUser_image_URL() {
        return user_image_URL;
    }

    public int getLevel() {
        return level;
    }

    public String getPassword() {
        return password;
    }

    //public Long getAchiv_progress() {
        //return achiv_progress;
    //}
}
