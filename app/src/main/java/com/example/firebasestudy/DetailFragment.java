package com.example.firebasestudy;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.firebasestudy.databinding.FragmentDetailBinding;
import com.example.firebasestudy.model.Article;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    private FragmentDetailBinding binding;
    private FirebaseFirestore firestore;
    private Article article = new Article();
    private String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(getLayoutInflater());
        // Inflate the layout for this fragment
        Bundle b = getArguments();

        assert b != null;
        id = b.getString("collectionid");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getdata(id);


    }

    private void getdata(String id) {
        firestore = FirebaseFirestore.getInstance();
        firestore.collection(Constantes.ARTICLE_COLECTION).document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    article = task.getResult().toObject(Article.class);
                    binding.content.setText(article.getContent());
                    Glide.with(requireActivity()).load(article.getUrl()).into(binding.imagedetail);
                    Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(binding.imagedetail);
                    binding.info.setText(DateUtils.getRelativeTimeSpanString(article.getCreatdAt()));
                }

            }
        });

    }
}
