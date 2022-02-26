package ru.turbopro.cubsappjava;

public class Achievements {
    private String imageURL;

    public Achievements() {}

    public Achievements(String name) {
        this.imageURL = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL){
        this.imageURL = imageURL;
    }
}
