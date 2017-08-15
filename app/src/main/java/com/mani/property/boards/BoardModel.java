package com.mani.property.boards;

import com.mani.property.home.PropertyModel;

import java.util.ArrayList;

/**
 * Created by Mani on 14-08-2017.
 */

public class BoardModel {
    private String board_id,board_name;

    public String getBoard_id() {
        return board_id;
    }

    public void setBoard_id(String board_id) {
        this.board_id = board_id;
    }

    public String getBoard_name() {
        return board_name;
    }

    public void setBoard_name(String board_name) {
        this.board_name = board_name;
    }

    public ArrayList<PropertyModel> getProperties() {
        return properties;
    }

    public void setProperties(ArrayList<PropertyModel> properties) {
        this.properties = properties;
    }

    ArrayList<PropertyModel>properties;
}
