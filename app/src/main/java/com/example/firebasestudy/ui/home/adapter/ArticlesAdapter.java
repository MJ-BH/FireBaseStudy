package com.example.firebasestudy.ui.home.adapter;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firebasestudy.R;
import com.example.firebasestudy.model.Article;

import java.util.ArrayList;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.MyViewHolder> {
    ArrayList<Article> articles;

    public ArticlesAdapter(ArrayList<Article> articles) {
        this.articles = articles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
        return new ArticlesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Article article = articles.get(position);
        holder.content.setText(article.getContent());
        Glide.with(holder.itemView).load(article.getUrl()).into(holder.image);
        holder.info.setText(DateUtils.getRelativeTimeSpanString(article.getCreatdAt()));

        Bundle b = new Bundle();
        b.putString("collectionid", article.getId());


    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView content, info;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            info = itemView.findViewById(R.id.info);
            image = itemView.findViewById(R.id.imagedetail);

        }
    }
}
