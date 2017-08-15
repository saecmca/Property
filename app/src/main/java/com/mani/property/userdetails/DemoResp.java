package com.mani.property.userdetails;

import com.mani.property.common.StatusOf;

import java.util.ArrayList;

/**
 * Created by Mani on 14-08-2017.
 */

public class DemoResp {
    public StatusOf getStatus() {
        return status;
    }

    public void setStatus(StatusOf status) {
        this.status = status;
    }

    StatusOf status;

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    ArrayList<String>images;
}
