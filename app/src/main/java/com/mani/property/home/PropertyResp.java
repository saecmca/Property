package com.mani.property.home;

import com.mani.property.common.StatusOf;

import java.util.ArrayList;

/**
 * Created by Mani on 04-08-2017.
 */

public class PropertyResp {
    public StatusOf getStatus() {
        return status;
    }

    public void setStatus(StatusOf status) {
        this.status = status;
    }

    StatusOf status;

    public ArrayList<PropertyModel> getProperties() {
        return properties;
    }

    public void setProperties(ArrayList<PropertyModel> properties) {
        this.properties = properties;
    }

    ArrayList<PropertyModel>properties;
}
