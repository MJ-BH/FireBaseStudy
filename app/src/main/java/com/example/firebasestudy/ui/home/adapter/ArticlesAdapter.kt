package com.example.firebasestudy.ui.home.adapter

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasestudy.R
import com.example.firebasestudy.model.Article
import com.example.firebasestudy.ui.home.adapter.ArticlesAdapter.MyViewHolder
import java.util.*

class ArticlesAdapter(var articles: ArrayList<Article?>?) : RecyclerView.Adapter<MyViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val article = articles?.get(position)
        holder.content?.text = article?.content
        Glide.with(holder.itemView).load(article?.url).into(holder.image!!)
        holder.info?.text = DateUtils.getRelativeTimeSpanString(article?.creatdAt!!)
        val b = Bundle()
        b.putString("collectionid", article.id)
        val navDirections: NavDirections = object : NavDirections {
            override fun getActionId(): Int {
                return R.id.action_navigation_home_to_detailFragment
            }

            override fun getArguments(): Bundle {
                return b
            }
        }
        holder.itemView.setOnClickListener { v: View? -> Navigation.findNavController(v!!).navigate(navDirections) }
    }

    override fun getItemCount(): Int {
        return articles!!.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var content: TextView? = itemView.findViewById(R.id.content)
        var info: TextView? = itemView.findViewById(R.id.info)
        var image: ImageView? = itemView.findViewById(R.id.imagedetail)

    }

}