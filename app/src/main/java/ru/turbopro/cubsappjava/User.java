package ru.turbopro.cubsappjava;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class User {

    private String name;
    private String login;
    private String password;
    private String status;
    private String card_ID;
    private int level;
    private int points;
    private int all_points;
    private int hours;
    private String user_image_URL;
    private ArrayList<String> achiv_progress;

    public User() {}

    public User(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public User(String name, String login, int points, int all_points, int hours, String user_image_URL, ArrayList<String> achiv_progress){
        this.name = name;
        this.login = login;
        this.points = points;
        this.hours = hours;
        this.all_points = all_points;
        this.user_image_URL = user_image_URL;
        this.achiv_progress = achiv_progress;
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

    public String getNameWP() {
        char[] name3 = new char[100];
        if (name != null) {
            char[] name2 = name.toCharArray();
            int j = 0;
            for (int i = 0; i < name.length(); i++) {
                if (name2[i] == ' ') j++;
                if (j == 2) break;
                name3[i] = name2[i];
            }
        }
        return String.valueOf(name3);
    }

    public String getUser_image_URL() {
        return user_image_URL;
    }

    public int getLevel() {
        return level;
    }

    public int getHours() {
        return hours;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<String> getAchiv_progress() {
        return achiv_progress;
    }
}
