package com.example.firebasestudy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;

import com.example.firebasestudy.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initFireBAse();
        binding.loginBtn.setOnClickListener(v -> Login());
        binding.inscriBtn.setOnClickListener(v -> startActivity( new Intent(LoginActivity.this , InscriptionActivity.class)));
    }
    private void Login (){
        if (!isEmpty()) {
            String mail = binding.emailInput.getText().toString();
            String password = binding.passwordInput.getText().toString();
            if (validEmail() && validPassword())
            {

// TODO: FireBase Login
                startActivity( new Intent(LoginActivity.this , MainActivity.class));
                finish();
            }
        }

    }

    private boolean isEmpty() {
        boolean empty = false ;

        if (TextUtils.isEmpty(binding.emailInput.getText()))
        {
            empty = true ;
            binding.emailInput.setError("Champ Vide");
        }
        if ((TextUtils.isEmpty(binding.passwordInput.getText())))
        {
            empty = true ;
            binding.passwordInput.setError("Champ Vide");
        }
        return  empty ;
    }
    private boolean validEmail(){
        boolean valid = true ;
       if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailInput.getText()).matches())
       {
           valid = false ;
           binding.emailInput.setError(" entrer un email Valide");
       }

        return valid ;
    }
    private boolean validPassword (){
     boolean valide = true ;
     if (binding.passwordInput.getText().length() <6)
     {
         valide = false ;
         binding.passwordInput.setError("minimum 6 caractéres ");
     }
     return  valide ;
    }

    private void initFireBAse(){

    }
}
