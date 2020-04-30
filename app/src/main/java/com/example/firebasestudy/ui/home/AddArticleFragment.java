package com.example.firebasestudy.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.firebasestudy.Constantes;
import com.example.firebasestudy.R;
import com.example.firebasestudy.databinding.FragmentAddArticleBinding;
import com.example.firebasestudy.model.Article;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class AddArticleFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1000;
    FragmentAddArticleBinding binding;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    Article article = new Article();
    private Uri filePath;
    private FirebaseStorage mStorage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddArticleBinding.inflate(getLayoutInflater());
        mAuth = FirebaseAuth.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.imageView3.setOnClickListener(v -> choosePicture(v));
        binding.ajouterbtn.setOnClickListener(v -> {
            if (!isEmpty() && valideImage())
                AddArticleFragment.this.uploadImage();
        });
    }

    private void uploadImage() {
        mStorage = FirebaseStorage.getInstance();
        StorageReference child = mStorage.getReference().child(Constantes.ARTICLE_COLLECTION + "/" + mAuth.getCurrentUser().getUid());
        child.putFile(filePath).continueWithTask(task -> child.getDownloadUrl()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                article.setUrl(task.getResult().toString());
                saveData();
            }
        });

    }

    private void saveData() {

        firestore = FirebaseFirestore.getInstance();
        // article.setId();
        article.setCreatdAt(new Date().getTime());
        article.setCreatedby(mAuth.getCurrentUser().getUid());
        article.setContent(binding.description.getText().toString());
        firestore.collection(Constantes.ARTICLE_COLLECTION).add(article).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {

                    Bundle b = new Bundle();
                    b.putString("collectionid", task.getResult().getId());
                    NavDirections navDirections = new NavDirections() {
                        @Override
                        public int getActionId() {
                            return R.id.action_addArticleFragment_to_detailFragment;
                        }

                        @NonNull
                        @Override
                        public Bundle getArguments() {
                            return b;
                        }
                    };
                    Navigation.findNavController(binding.getRoot()).navigate(navDirections);
                }
            }
        });


    }

    public void choosePicture(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            requireActivity().getContentResolver().takePersistableUriPermission(filePath, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), filePath);
                binding.imageView3.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean valideImage() {
        boolean valid = true;
        if (filePath == null) {
            valid = false;
            Snackbar.make(binding.imageView3, "Ajouter une Image ", Snackbar.LENGTH_LONG);
        }
        return valid;
    }

    private boolean isEmpty() {
        boolean empty = false;

        if (TextUtils.isEmpty(binding.description.getText())) {
            empty = true;
            binding.description.setError("Champ Vide");
        }
        return empty;
    }
}
