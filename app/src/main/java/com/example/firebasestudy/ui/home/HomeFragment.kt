package com.example.firebasestudy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.firebasestudy.Constantes
import com.example.firebasestudy.databinding.FragmentHomeBinding
import com.example.firebasestudy.model.Article
import com.example.firebasestudy.ui.home.adapter.ArticlesAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

class HomeFragment : Fragment() {
    var articlesAdapter: ArticlesAdapter? = null
    var articles: ArrayList<Article?>? = ArrayList()
    private lateinit var binding: FragmentHomeBinding
    private var firestore: FirebaseFirestore? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        articles?.clear()
        articlesAdapter = ArticlesAdapter(articles)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = articlesAdapter
        getData()
    }

    private fun getData() {
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection(Constantes.ARTICLE_COLECTION!!)?.orderBy("creatdAt", Query.Direction.DESCENDING)?.get()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                articles?.clear()
                for (document in task.result!!.documents) {
                    val article = document.toObject(Article::class.java)
                    article?.id=document.id
                    articles?.add(article)
                }
                articlesAdapter?.notifyDataSetChanged()
            }
        }
    }
}