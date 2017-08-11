package com.mani.property.searches;

/**
 * Created by Mani on 10-08-2017.
 */

public class SaveSearchReq {
    String UserId,search;
    public SaveSearchReq(String UserId,String search){
        this.UserId=UserId;
        this.search=search;
    }
}
