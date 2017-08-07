package com.mani.property.favourite;

/**
 * Created by Mani on 07-08-2017.
 */

public class FavouriteReq {
    private String UserId;
    private String zpid;
    private boolean fav;

    public FavouriteReq(String UserId, String zpid,boolean fav) {
        this.UserId = UserId;
        this.zpid = zpid;
        this.fav=fav;
    }
}
