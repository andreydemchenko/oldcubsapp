package ru.turbopro.cubsappjava;

public class CardEvent {
    private String key;
    private String title;
    private String date;
    private String description;
    private String image;

    public CardEvent(String key, String title, String date, String description, String image) {
        this.key = key;
        this.title = title;
        this.date = date;
        this.description = description;
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }
}
