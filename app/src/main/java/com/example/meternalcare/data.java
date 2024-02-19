package com.example.data;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.meternalcare.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class data extends AppCompatActivity {
    private Button Button1;
    private TextView textView;
    private RequestQueue volleyQueue;
    private static final String CHANNEL_ID = "fall_detection_channel";
    private static final double UPRIGHT_THRESHOLD = 80.0;
    private static final double LYING_DOWN_THRESHOLD = 10.0;
    private static final double INVERTED_THRESHOLD = -80.0;


    private int mag0, mag1, mag2, acc0, acc1, acc2, gyr0, gyr1, gyr2, acc2_0, acc2_1, acc2_2,stepcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        textView = findViewById(R.id.textView);
        Button1 = findViewById(R.id.Button1);

        // Initialize the Volley request queue
        volleyQueue = Volley.newRequestQueue(data.this);

        // Set click listener for the button
        Button1.setOnClickListener(v -> httpGet());


        // Make the initial HTTP request
        httpGet();
    }

    private void httpGet() {
        String url = "https://delhi-server.vercel.app/getval";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                (Response.Listener<JSONObject>) response -> {
                    try {
                        mag0 = response.getInt("mag0");
                        mag1 = response.getInt("mag1");
                        mag2 = response.getInt("mag2");
                        acc0 = response.getInt("acc0");
                        acc1 = response.getInt("acc1");
                        acc2 = response.getInt("acc2");
                        gyr0 = response.getInt("gyr0");
                        gyr1 = response.getInt("gyr1");
                        gyr2 = response.getInt("gyr2");
                        acc2_0 = response.getInt("acc2_0");
                        acc2_1 = response.getInt("acc2_1");
                        acc2_2 = response.getInt("acc2_2");
                        double combinedPitch = Math.random() * 180 - 90;
                        String posture = classifyPosture(combinedPitch);
                        updateUI(posture);

                        float accelerationMagnitude = (float) Math.sqrt(acc0 * acc0 + acc1 * acc1 + acc2 * acc2);
                        float gyroscopeMagnitude = (float) Math.sqrt(gyr0 * gyr0 + gyr1 * gyr1 + gyr2 * gyr2);
                        float acclero=(float)Math.sqrt(acc2_0*acc2_0+acc2_1*acc2_1+acc2_2*acc2_2);
                        double magMagnitude = Math.sqrt(mag0 * mag0 + mag1 * mag1 + mag2 * mag2);

                        // Concatenate all values and update the UI on the main thread
                        runOnUiThread(() -> {
                            if(accelerationMagnitude>999){
                                stepcount++;
                            }
                              String sensorValues = "mag0: " + mag0 + "\n" +
                                                  "mag1: " + mag1 + "\n" +
                                              "mag2: " + mag2 + "\n" +
                                      "acc0: " + acc0 + "\n" +
                                      "acc1: " + acc1 + "\n" +
                                    "acc2: " + acc2 + "\n" +
                                       "gyr0: " + gyr0 + "\n" +
                                    "gyr1: " + gyr1 + "\n" +
                                  "gyr2: " + gyr2 + "\n" +
                                    "acc2_0: " + acc2_0 + "\n" +
                                  "acc2_1: " + acc2_1 + "\n" +
                                    "acc2_2: " + acc2_2+"\n"+
                            "stepcount:" +stepcount+"\n"+
                                    "accelerationMagnitude: " + accelerationMagnitude + "\n" +
                                    "gyroscopeMagnitude: " + gyroscopeMagnitude+"\n"+
                                    "acclerometer integrated with gyro :"+ acclero+"\n"+
                                    "magMagnitude: " + magMagnitude;



                            textView.setText(sensorValues);


                        });
                        if(gyroscopeMagnitude>900){
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                                    .setSmallIcon(R.drawable.notify)
                                    .setContentTitle("Fall Detection")
                                    .setContentText("Be cautious! Fall detected!")
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setAutoCancel(true);
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                            notificationManager.notify(1, builder.build());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                (Response.ErrorListener) error -> {
                    // Show a Toast with a meaningful error message
                    Toast.makeText(data.this, "Error fetching data: " + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    // Log the error message
                    Log.e("MainActivity", "HTTP-get error: " + error.getLocalizedMessage());
                }
        );

        // Add the request to the Volley request queue
        volleyQueue.add(jsonObjectRequest);
    }

    @Override
    protected void onDestroy() {
        // Cancel all pending requests when the activity is destroyed
        if (volleyQueue != null) {
            volleyQueue.cancelAll(this);
        }
        super.onDestroy();
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Fall Detection Channel";
            String description = "Channel for fall detection notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private String classifyPosture(double combinedPitch) {
        if (combinedPitch > UPRIGHT_THRESHOLD) {
            return "Upright";
        } else if (combinedPitch < INVERTED_THRESHOLD) {
            return "Inverted";
        } else {
            return "Lying Down";
        }
    }
    private void updateUI(String posture) {
        // Update the TextView with the detected posture
        textView.setText("Detected Posture: " + posture);

        // Display a Toast message for demonstration purposes
        Toast.makeText(data.this, "Detected Posture: " + posture, Toast.LENGTH_SHORT).show();
    }


}
