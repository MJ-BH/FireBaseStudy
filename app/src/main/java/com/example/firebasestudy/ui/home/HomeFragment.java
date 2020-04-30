package com.example.firebasestudy.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.firebasestudy.Constantes;
import com.example.firebasestudy.databinding.FragmentHomeBinding;
import com.example.firebasestudy.model.Article;
import com.example.firebasestudy.ui.home.adapter.ArticlesAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
   private FragmentHomeBinding binding ;

    private FirebaseFirestore firestore;
    private ArticlesAdapter articlesAdapter;
    private ArrayList<Article> articles = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
         binding = FragmentHomeBinding.inflate(getLayoutInflater());
        articlesAdapter = new ArticlesAdapter(articles);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recycler.setAdapter(articlesAdapter);
        getData();
    }

    private void getData() {
        firestore = FirebaseFirestore.getInstance();

        firestore.collection(Constantes.ARTICLE_COLLECTION).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult().getDocuments()) {

                    Article e = document.toObject(Article.class);
                    e.setId(document.getId());
                    articles.add(e);
                }
                articlesAdapter.notifyDataSetChanged();
            }
        });


    }

}
