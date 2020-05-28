package com.example.firebasestudy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firebasestudy.databinding.ActivitySplashBinding;
import com.example.firebasestudy.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initFireBase();
         delay();
    }

    public void delay (){
        final Handler handler = new Handler();
        // Do something after 3s = 3000ms
        handler.postDelayed(this::isConnected, 3000);

    }

    private void isConnected() {

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            String uid = mAuth.getCurrentUser().getUid();
            mRefrence.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getType() == 1) {
                        startActivity(new Intent(SplashScreen.this, MainActivityAdmin.class));
                        finish();

                    } else {
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {
            startActivity(new Intent(SplashScreen.this , LoginActivity.class));
            finish();
        }

    }

    private void initFireBase() {
        mAuth = FirebaseAuth.getInstance();
        //get la base de donné
        mDatabase = FirebaseDatabase.getInstance();
        // le curseur
        mRefrence = mDatabase.getReference("user");

    }

}
