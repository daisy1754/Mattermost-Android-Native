package com.kilogramm.mattermost.model.fromnet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Evgeny on 20.09.2016.
 */
public class LogoutData {

    @SerializedName("user_id")
    @Expose
    private String userId;

    public LogoutData(String userId) {
        this.userId = userId;
    }

    public LogoutData() {
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
