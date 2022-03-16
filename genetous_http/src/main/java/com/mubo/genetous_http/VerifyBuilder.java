package com.mubo.genetous_http;

import android.app.Activity;

public class VerifyBuilder {
    private Activity act;
    private String token;
    private String host;
    private PostGet.completionHandler completionHandler;

    public VerifyBuilder setActivity(Activity act) {
        this.act = act;
        return this;
    }

    public VerifyBuilder setToken(String token) {
        this.token = token;
        return this;
    }

    public VerifyBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public VerifyBuilder setCompletionHandler(PostGet.completionHandler completionHandler) {
        this.completionHandler = completionHandler;
        return this;
    }

    public Verify createVerify() {
        return new Verify(act, token, host, completionHandler);
    }
}