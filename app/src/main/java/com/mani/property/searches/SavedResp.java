package com.mani.property.searches;

import com.mani.property.common.StatusOf;

import java.util.ArrayList;

/**
 * Created by Mani on 10-08-2017.
 */

public class SavedResp {
    StatusOf status;

    public StatusOf getStatus() {
        return status;
    }

    public void setStatus(StatusOf status) {
        this.status = status;
    }

    public ArrayList<SearchModel> getSave_searches() {
        return save_searches;
    }

    public void setSave_searches(ArrayList<SearchModel> save_searches) {
        this.save_searches = save_searches;
    }

    ArrayList<SearchModel>save_searches;
}
