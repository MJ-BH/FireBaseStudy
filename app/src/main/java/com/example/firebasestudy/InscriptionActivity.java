package com.example.firebasestudy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;

import com.example.firebasestudy.databinding.ActivityInscriptionBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class InscriptionActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1000 ;
    private ActivityInscriptionBinding binding ;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInscriptionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initFireBAse();
        binding.addimage.setOnClickListener(this::choosePicture);
        binding.inscriBtn.setOnClickListener(v -> save());
    }

    private void save (){
        if (!isEmpty()){
            String mail = binding.mailInscri.getText().toString();
            String pwd = binding.pwdInscri.getText().toString();
            String nom = binding.nomInscri.getText().toString();
            String prenom = binding.prenomInscri.getText().toString();
            if (validEmail()&&validPassword()&&valideImage())
            {
                binding.progressBar.setVisibility(View.VISIBLE);
                // TODO: FireBAse
                startActivity(new Intent(InscriptionActivity.this , MainActivity.class));
                finish();
            }
        }

    }


        public void choosePicture(View view) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                    && data != null && data.getData() != null) {
                filePath = data.getData();
                                  getContentResolver().takePersistableUriPermission(filePath, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    binding.addimage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    private boolean isEmpty() {
        boolean empty = false ;

        if (TextUtils.isEmpty(binding.mailInscri.getText()))
        {
            empty = true ;
            binding.mailInscri.setError("Champ Vide");
        }
        if ((TextUtils.isEmpty(binding.pwdInscri.getText())))
        {
            empty = true ;
            binding.pwdInscri.setError("Champ Vide");
        }
        if (TextUtils.isEmpty(binding.nomInscri.getText()))
        {
            empty = true ;
            binding.nomInscri.setError("Champ Vide");
        }
        if (TextUtils.isEmpty(binding.prenomInscri.getText()))
        {
            empty = true ;
            binding.prenomInscri.setError("Champ Vide");
        }
        return  empty ;
    }
    private boolean validEmail(){
        boolean valid = true ;
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.mailInscri.getText()).matches())
        {
            valid = false ;
            binding.mailInscri.setError(" entrer un email Valide");
        }

        return valid ;
    }
    private boolean validPassword (){
        boolean valide = true ;
        if (binding.pwdInscri.getText().length() <6)
        {
            valide = false ;
            binding.pwdInscri.setError("minimum 6 caractéres ");
        }
        return  valide ;
    }
    private boolean valideImage (){
        boolean valid = true ;
        if (filePath == null)
        {
            valid = false ;
            Snackbar.make(binding.addimage , "Ajouter une Image ", Snackbar.LENGTH_LONG);
        }
        return  valid ;
    }

    private void initFireBAse(){

    }
}
