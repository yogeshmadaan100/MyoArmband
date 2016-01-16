package com.imaniac.myo.moduleReceiver.model;

import java.util.List;

/**
 * Created by yogeshmadaan on 11/10/15.
 */
public class PreferenceModel {
    String title;
    List<Item> items;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

}
