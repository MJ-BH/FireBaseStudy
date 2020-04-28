package com.example.firebasestudy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firebasestudy.databinding.ActivitySplashBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
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
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(SplashScreen.this, LoginActivity.class));
            finish();
        }

    }
}
