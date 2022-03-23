package ru.turbopro.cubsappjava;

import android.os.Parcel;
import android.os.Parcelable;

public class CardEvent {
    private String id;
    private String title;
    private String date;
    private String description;
    private byte[] image;

    public CardEvent(String id, String title, String description, String date, byte[] image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.image = image;
    }

/*    public CardEvent(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.date = in.readString();
        this.image = new byte[in.readInt()];
        in.readByteArray(this.image);
    }*/

    public String getId() {
        return id;
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

    public byte[] getImage() {
        return image;
    }

 /*   @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.title);
        parcel.writeString(this.description);
        parcel.writeString(this.date);
        parcel.writeByteArray(this.image);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public CardEvent createFromParcel(Parcel in) {
            return new CardEvent(in);
        }

        public CardEvent[] newArray(int size) {
            return new CardEvent[size];
        }
    };*/
}
