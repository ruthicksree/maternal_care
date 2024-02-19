package com.example.meternalcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile extends AppCompatActivity {
    private TextView textViewWelcome, textViewFullName, textViewEmail, textViewGender, textViewMobile,textViewDob;
    private ProgressBar progressBar;
    private String fullName, email, doB, gender, mobile;
    private ImageView imageView;
    Button button;
    private FirebaseAuth authProfile;



    @SuppressLint("MissingInflatedId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("Home");
        textViewWelcome=findViewById(R.id.show_welcome);
        textViewFullName=findViewById(R.id.full_name);
        textViewEmail=findViewById(R.id.email);
        textViewGender=findViewById(R.id.gender);
        textViewMobile=findViewById(R.id.mobile);
        progressBar=findViewById(R.id.progressBar);
        textViewDob=findViewById(R.id.dob);
        button=findViewById(R.id.logout);



        authProfile=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=authProfile.getCurrentUser();

        if(firebaseUser == null){
            Toast.makeText(profile.this,"Something went wrong!",
                    Toast.LENGTH_LONG).show();
        }else{
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent =new Intent(getApplicationContext(),login.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                ReaderWriterdetails readWriteUserDetails=snapshot.getValue(ReaderWriterdetails.class);


                if (readWriteUserDetails!=null){
                    fullName=firebaseUser.getDisplayName();
                    email=firebaseUser.getEmail();
                    doB=readWriteUserDetails.doB;
                    gender=readWriteUserDetails.gender;
                    mobile=readWriteUserDetails.mobile;

                    textViewWelcome.setText("Welcome,"+ fullName +"!");
                    textViewFullName.setText(fullName);
                    textViewEmail.setText(email);
                    textViewGender.setText(gender);
                    textViewMobile.setText(mobile);
                    textViewDob.setText(doB);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error){
                Toast.makeText(profile.this,"Something went wrong!",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }



        });
    }
}
