package com.example.tonio.projektkoncowy.com.example.tonio.adapters;

public class CardViewRoomItem {
    private int mImageResource;
    private String location;

    public CardViewRoomItem(int mImageResource, String location) {
        this.mImageResource = mImageResource;
        this.location = location;
    }

    public int getmImageResource() {
        return mImageResource;
    }

    public void setmImageResource(int mImageResource) {
        this.mImageResource = mImageResource;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
