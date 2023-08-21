package com.mubo.genetous_http;

import android.app.Activity;

public class KillBuilder {
    private Activity act;
    private String token;
    private String host;
    private PostGet.completionHandler completionHandler;

    public KillBuilder setActivity(Activity act) {
        this.act = act;
        return this;
    }

    public KillBuilder setToken(String token) {
        this.token = token;
        return this;
    }

    public KillBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public KillBuilder setCompletionHandler(PostGet.completionHandler completionHandler) {
        this.completionHandler = completionHandler;
        return this;
    }

    public Kill killToken() {
        return new Kill(act, token, host, completionHandler);
    }
}