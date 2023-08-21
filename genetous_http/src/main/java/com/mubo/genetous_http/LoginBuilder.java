package com.mubo.genetous_http;

import android.app.Activity;

import org.json.JSONObject;

public class LoginBuilder {
    private Activity act;
    private JSONObject data;
    private String host;
    private PostGet.completionHandler completionHandler;

    public LoginBuilder setActivity(Activity act) {
        this.act = act;
        return this;
    }

    public LoginBuilder setData(JSONObject data) {
        this.data = data;
        return this;
    }

    public LoginBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public LoginBuilder setCompletionHandler(PostGet.completionHandler completionHandler) {
        this.completionHandler = completionHandler;
        return this;
    }

    public Login createLogin() {
        return new Login(act, data, host, completionHandler);
    }
}