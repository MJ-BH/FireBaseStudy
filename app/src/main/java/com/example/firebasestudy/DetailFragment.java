package com.example.firebasestudy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.firebasestudy.databinding.FragmentDetailBinding;
import com.example.firebasestudy.model.Article;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    FragmentDetailBinding binding;

    Article article = new Article();
    private String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(getLayoutInflater());
        Bundle b = getArguments();
        id = b.getString("collectionid");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getdata(id);
    }

    private void getdata(String id) {

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection(Constantes.ARTICLE_COLLECTION).document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    article = task.getResult().toObject(Article.class);
                    binding.content.setText(article.getContent());
                    // Log.e("id", article.getId());
                }

            }
        });

    }
}
