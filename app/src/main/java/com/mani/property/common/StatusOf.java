package com.mani.property.common;

import java.util.ArrayList;

/**
 * Created by Mani on 31-07-2017.
 */

public class StatusOf {
private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public ArrayList<String> getUrl() {
        return url;
    }

    public void setUrl(ArrayList<String> url) {
        this.url = url;
    }

    private ArrayList<String>url;

}
