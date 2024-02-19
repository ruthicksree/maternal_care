package com.example.meternalcare;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.meternalcare.R;
import com.google.android.material.textview.MaterialTextView;

public class pharmacy extends AppCompatActivity {
    private static final int REQUEST_SEND_SMS = 1;
    private int quantity = 2;
    private int quantity1=2;// Initial quantity value
    private MaterialTextView quantityTextView,QuantityTextView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);

        Button sendSmsButton = findViewById(R.id.sendSmsButton);
        Button sendSmsButton1=findViewById(R.id.sendSmsButton1);

        sendSmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check for SMS permission
                if (ContextCompat.checkSelfPermission(
                        pharmacy.this, android.Manifest.permission.SEND_SMS)
                        == PackageManager.PERMISSION_GRANTED) {
                    // If permission is granted, send the SMS
                    sendSMS();
                } else {
                    // If permission is not granted, request it
                    ActivityCompat.requestPermissions(
                            pharmacy.this,
                            new String[]{android.Manifest.permission.SEND_SMS},
                            REQUEST_SEND_SMS
                    );
                }
            }
        });
        sendSmsButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check for SMS permission
                if (ContextCompat.checkSelfPermission(
                        pharmacy.this, android.Manifest.permission.SEND_SMS)
                        == PackageManager.PERMISSION_GRANTED) {
                    // If permission is granted, send the SMS
                    sendSMS1();
                } else {
                    // If permission is not granted, request it
                    ActivityCompat.requestPermissions(
                            pharmacy.this,
                            new String[]{android.Manifest.permission.SEND_SMS},
                            REQUEST_SEND_SMS
                    );
                }
            }
        });

        quantityTextView = findViewById(R.id.arrow);
        QuantityTextView1=findViewById(R.id.arrow1);



        // Set initial quantity in the TextView
        updateQuantityTextView();
        updateQuantityTextView1();

        // Make the TextView clickable
        quantityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Increase the quantity
                quantity++;
                // Update the TextView
                updateQuantityTextView();
            }
        });
        QuantityTextView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Increase the quantity
                quantity1++;
                // Update the TextView
                updateQuantityTextView1();
            }
        });
    }

    private void updateQuantityTextView() {
        quantityTextView.setText("Qty: " + quantity);
    }
    private void updateQuantityTextView1(){QuantityTextView1.setText("Qty :"+quantity1);}

    private void sendSMS() {
        String phoneNumber = "8903287800"; // Replace with the desired phone number
        String message = "Order is placed for Folic Acid tablets  "; // Replace with your message

        try {
            // Create an intent to send an SMS
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
            smsIntent.putExtra("sms_body", message);

            // Start the SMS activity
            startActivity(smsIntent);

            // Show a "Order placed" toast message
            Toast.makeText(this, "Order placed", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private void sendSMS1() {
        String phoneNumber = "8903287800"; // Replace with the desired phone number for sendSmsButton1
        String message = "Order is placed for Albendazole Tablets "; // Replace with your message for sendSmsButton1

        try {
            // Create an intent to send an SMS
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
            smsIntent.putExtra("sms_body", message);

            // Start the SMS activity
            startActivity(smsIntent);

            // Show a "Order placed" toast message
            Toast.makeText(this, "Order placed", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS for button 1", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If permission is granted, send the SMS
                sendSMS();
            } else {
                // If permission is denied, show a toast or handle accordingly
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
