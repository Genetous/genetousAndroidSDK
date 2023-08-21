package com.mubo.genetous_http;

import android.app.Activity;

import org.json.JSONObject;

public class GuestTokenBuilder {
    private Activity act;
    private String host;
    private PostGet.completionHandler completionHandler;

    public GuestTokenBuilder setActivity(Activity act) {
        this.act = act;
        return this;
    }
    public GuestTokenBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public GuestTokenBuilder setCompletionHandler(PostGet.completionHandler completionHandler) {
        this.completionHandler = completionHandler;
        return this;
    }

    public GuestToken getToken() {
        return new GuestToken(act, host, completionHandler);
    }
}
