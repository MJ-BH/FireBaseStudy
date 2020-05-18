package com.example.firebasestudy

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.firebasestudy.databinding.FragmentDetailBinding
import com.example.firebasestudy.model.Article
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    var firestore: FirebaseFirestore? = null
    var article: Article? = Article()
    private var id: String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDetailBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        val b: Bundle? = arguments
        id = b?.getString("collectionid")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getdata(id)
    }

    private fun getdata(id: String?) {
        firestore = FirebaseFirestore.getInstance()
        if (id != null) {
            firestore?.collection(Constantes.ARTICLE_COLECTION!!)?.document(id)?.get()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    article = task.result?.toObject(Article::class.java)
                    binding.content.text = article?.content
                    Glide.with(this.requireActivity()).load(article?.url).into(binding.imagedetail)
                    binding.info.text = DateUtils.getRelativeTimeSpanString(article?.creatdAt!!)
                }
            }
        }
    }
}