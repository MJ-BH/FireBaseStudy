package com.example.firebasestudy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.firebasestudy.databinding.ActivityMainBinding;

public class SplashScreen extends AppCompatActivity {
    private ActivityMainBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
         delay();
    }

    public void delay (){
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            // Do something after 3s = 3000ms
            startActivity(new Intent(SplashScreen.this , LoginActivity.class));
            finish();
        }, 3000);

    }
}
