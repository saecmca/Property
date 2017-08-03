package com.mani.property.userdetails;

import com.mani.property.common.StatusOf;

/**
 * Created by Mani on 31-07-2017.
 */

public class SigninResponse {
    public StatusOf getStatus() {
        return status;
    }

    public void setStatus(StatusOf status) {
        this.status = status;
    }

    StatusOf status;
    private String UserId;
    private String username;
    private String mobile;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    private String auth_token;
}
