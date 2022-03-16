package com.mubo.genetoussdk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mubo.genetous_mqtt.MQTT;
import com.mubo.genetous_mqtt.MQTTBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class MqttActivity extends AppCompatActivity {

    AppCompatButton send;
    EditText payText;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt);
        send=findViewById(R.id.send);
        payText=findViewById(R.id.payloadText);
        txt=findViewById(R.id.txt);
        MQTT mq= new MQTTBuilder()
                .setUrl("tcp://157.90.140.35:1883")
                .setUsername("omeryil")
                .setPassword("12345678")
                .setTopic("CabIOT")
                .setClientId(create_clientId())
                .setContext(MqttActivity.this)
                .setMqttHandler(hnd)
                .createMQTT();

        mq.process();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mq.publish(payText.getText().toString());
            }
        });
    }
    String create_clientId(){
        return Settings.Secure.getString(MqttActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

    }
    MQTT.MqttHandler hnd=new MQTT.MqttHandler() {
        @Override
        public void messageArrived(String topic, byte[] payload) {
            String mes=new String(payload);
            try {
                JSONObject j=new JSONObject(mes);
                String device=j.getString("device");
                String time=j.getString("time");
                String temp=j.getString("tempature");
                String hum=j.getString("humidity");
                Date date=new Date((Long.parseLong(time)*1000)-60000);
                DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");
                String strDate = dateFormat.format(date);
                txt.setText("Device : "+device+"\nTime : "+strDate+"\nTemp : "+temp+"\nHumidity : "+hum);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //Toast.makeText(MqttActivity.this, mes, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void connectionLost(Throwable cause) {

        }

        @Override
        public void deliveryComplete(boolean value) {

        }

        @Override
        public void subscriptionSuccess(boolean value, String fail) {
            if(value)
                Toast.makeText(MqttActivity.this, "Subscription Success", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(MqttActivity.this, "Subscription "+fail, Toast.LENGTH_SHORT).show();
        }
    };
}