package com.mubo.genetous_mqtt;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MQTT {
    String url;
    String username;
    String password;
    String topic;
    String clientId;
    Context context;
    MqttHandler mqttHandler;
    boolean readOwnMessage=false;
    private String TAG="GenetousMQTT";
    MqttAndroidClient client =
            null;
    public interface MqttHandler {
        void messageArrived(String topic, byte[] payload);
        void connectionLost(Throwable cause);
        void deliveryComplete(boolean value);
        void subscriptionSuccess(boolean value,String fail);
    }
    public MQTT(String url, String username, String password, String topic,String clientId,Context context,
                MqttHandler mqttHandler) {
        this.url=url;
        this.username=username;
        this.password=password;
        this.topic=topic;
        this.clientId=clientId;
        this.context=context;
        this.mqttHandler=mqttHandler;
    }
    public void publish(String payload){
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setId(3);
            client.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }
    public void process() {
        try {
            client = new MqttAndroidClient(context,url,
                    clientId);
            client.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {

                }

                @Override
                public void connectionLost(Throwable cause) {
                    mqttHandler.connectionLost(cause);
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Log.d(TAG, new String(message.getPayload()));
                    mqttHandler.messageArrived(topic, message.getPayload());
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    mqttHandler.deliveryComplete(true);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                    try {
                        client.subscribe(topic, 0, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.d(TAG, "onSuccess");
                                mqttHandler.subscriptionSuccess(true,"");
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.d(TAG, "Fail");
                                mqttHandler.subscriptionSuccess(false,exception.getMessage());
                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
}
