package com.mani.property.userdetails;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mani on 31-07-2017.
 */

public class UserRequest {
    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    private String UserId;
    public SigninRequest getUser() {
        return user;
    }

    public void setUser(SigninRequest user) {
        this.user = user;
    }

    @SerializedName("user")
    SigninRequest user;
}
