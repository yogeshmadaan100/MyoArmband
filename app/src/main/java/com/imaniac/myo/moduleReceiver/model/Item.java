package com.imaniac.myo.moduleReceiver.model;

/**
 * Created by yogeshmadaan on 11/10/15.
 */
public class Item{
    int icon;
    String title;

    public Item(int icon, String title)
    {
        this.icon = icon;
        this.title = title;
    }
    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
