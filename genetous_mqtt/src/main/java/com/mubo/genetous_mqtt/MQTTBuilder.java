package com.mubo.genetous_mqtt;

import android.content.Context;

public class MQTTBuilder {
    private String url;
    private String username;
    private String password;
    private String topic;
    private String clientId;
    private Context context;
    private MQTT.MqttHandler mqttHandler;

    public MQTTBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public MQTTBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public MQTTBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public MQTTBuilder setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public MQTTBuilder setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public MQTTBuilder setContext(Context context) {
        this.context = context;
        return this;
    }

    public MQTTBuilder setMqttHandler(MQTT.MqttHandler mqttHandler) {
        this.mqttHandler = mqttHandler;
        return this;
    }



    public MQTT createMQTT() {
        return new MQTT(url, username, password, topic, clientId, context, mqttHandler);
    }
}