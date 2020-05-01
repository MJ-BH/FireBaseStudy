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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    ArticlesAdapter articlesAdapter;
    ArrayList<Article> articles = new ArrayList<>();
    private FragmentHomeBinding binding;
    private FirebaseFirestore firestore;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        articles.clear();
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
        firestore.collection(Constantes.ARTICLE_COLECTION).orderBy("creatdAt", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    articles.clear();
                    for (DocumentSnapshot document : task.getResult().getDocuments()) {
                        Article article = document.toObject(Article.class);
                        article.setId(document.getId());
                        articles.add(article);
                    }
                    articlesAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
