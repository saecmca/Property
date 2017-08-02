package com.mani.property.login;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mani on 31-07-2017.
 */

public class UserRequest {
    public SigninRequest getUser() {
        return user;
    }

    public void setUser(SigninRequest user) {
        this.user = user;
    }

    @SerializedName("user")
    SigninRequest user;
}
