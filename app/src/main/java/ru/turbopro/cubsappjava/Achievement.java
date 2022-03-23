package ru.turbopro.cubsappjava;

public class Achievement {
    private int id;
    private byte[] ach_image;
    private String name;
    private int points;
    private int points_need;
    private String type;

    public Achievement(int id) {
        this.id = id;
    }

    public Achievement(int id, byte[] ach_image, String name, int points, int points_need, String type) {
        this.id = id;
        this.ach_image = ach_image;
        this.name = name;
        this.points = points;
        this.points_need = points_need;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public byte[] getAch_image() {
        return ach_image;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public int getPoints_need() {
        return points_need;
    }

    public String getType() {
        return type;
    }
}
