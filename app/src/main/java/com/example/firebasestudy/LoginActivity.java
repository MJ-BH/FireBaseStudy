package com.example.firebasestudy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firebasestudy.databinding.ActivityLoginBinding;
import com.example.firebasestudy.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefrence;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        initFireBase();
        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        binding.loginBtn.setOnClickListener(v -> Login());
        binding.inscriBtn.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, InscriptionActivity.class)));
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("gmail", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Gmail", "Google sign in failed", e);
                // [START_EXCLUDE]
                //updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }

    // [END onactivityresult]
    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Gmail", "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
                        if (acct != null) {
                            String personName = acct.getDisplayName();
                            String personGivenName = acct.getGivenName();
                            String personFamilyName = acct.getFamilyName();
                            String personEmail = acct.getEmail();
                            String personId = acct.getId();
                            Uri personPhoto = acct.getPhotoUrl();
                            User mUser = new User();
                            mUser.setMail(personEmail);
                            mUser.setNom(personName);
                            mUser.setPrenom(personGivenName);
                            mUser.setUid(user.getUid());
                            mUser.setUrl(personPhoto.toString());
                            saveUser(mUser);

                        }

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithCredential:failure", task.getException());
                        Snackbar.make(binding.getRoot(), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        // updateUI(null);
                    }


                });
    }

    // [END auth_with_google]
    private void Login() {
        if (!isEmpty()) {
            String mail = binding.emailInput.getText().toString();
            String password = binding.passwordInput.getText().toString();
            if (validEmail() && validPassword()) {

                mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = task.getResult().getUser().getUid();
                        mRefrence.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                User user = dataSnapshot.getValue(User.class);

                                if (user.getType() == 1) {
                                    startActivity(new Intent(LoginActivity.this, MainActivityAdmin.class));
                                    finish();
                                } else {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else {
                        Snackbar.make(binding.getRoot(), task.getException().getMessage(), Snackbar.LENGTH_LONG);
                    }

                });
            }
        }

    }

    private void saveUser(User mUser) {

        mRefrence.child(mUser.getUid()).setValue(mUser).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {


                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {

                Snackbar.make(binding.getRoot(), task.getException().getMessage(), Snackbar.LENGTH_LONG);
            }

        });

    }

    private boolean isEmpty() {
        boolean empty = false;

        if (TextUtils.isEmpty(binding.emailInput.getText())) {
            empty = true;
            binding.emailInput.setError("Champ Vide");
        }
        if ((TextUtils.isEmpty(binding.passwordInput.getText()))) {
            empty = true;
            binding.passwordInput.setError("Champ Vide");
        }
        return empty;
    }

    private boolean validEmail() {
        boolean valid = true;
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailInput.getText().toString()).matches()) {
            valid = false;
            binding.emailInput.setError(" entrer un email Valide");
        }

        return valid;
    }

    private boolean validPassword() {
        boolean valide = true;
        if (binding.passwordInput.getText().length() < 6) {
            valide = false;
            binding.passwordInput.setError("minimum 6 caractéres ");
        }
        return valide;
    }

    private void initFireBase() {
        mAuth = FirebaseAuth.getInstance();
        //get la base de donné
        mDatabase = FirebaseDatabase.getInstance();
        // le curseur
        mRefrence = mDatabase.getReference("user");

    }

}
