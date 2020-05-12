package com.example.firebasestudy.ui.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.firebasestudy.Constantes
import com.example.firebasestudy.R
import com.example.firebasestudy.databinding.FragmentAddArticleBinding
import com.example.firebasestudy.model.Article
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.IOException
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class AddArticleFragment : Fragment() {
    private lateinit var binding: FragmentAddArticleBinding
    var article: Article? = Article()
    private var mStorage: FirebaseStorage? = null
    private val mAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    private var firestore: FirebaseFirestore? = null
    private var filePath: Uri? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddArticleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView3.setOnClickListener { view: View? -> choosePicture(view) }
        binding.ajouterbtn.setOnClickListener {
            if (!isEmpty() && valideImage()) {
                uploadImage()
            }
        }
    }

    private fun uploadImage() {
        mStorage = FirebaseStorage.getInstance()
        val child = FirebaseStorage.getInstance().reference.child(Constantes.ARTICLE_COLECTION + "/" + mAuth?.currentUser?.uid)
        child.putFile(filePath!!).continueWithTask { child.downloadUrl }.addOnCompleteListener { task: Task<Uri?>? ->
            if (task?.isSuccessful!!) {
                article?.setUrl(task.result.toString())
                saveData()
            }
        }
    }

    private fun saveData() {
        firestore = FirebaseFirestore.getInstance()
        article?.setCreatdAt(Date().time)
        article?.setCreatedby(mAuth?.currentUser?.uid)
        article?.setContent(binding.description.text.toString())
        firestore?.collection(Constantes.ARTICLE_COLECTION!!)?.add(article!!)?.addOnCompleteListener { task: Task<DocumentReference?>? ->
            val s = task?.result?.id
            val b = Bundle()
            b.putString("collectionid", s)
            val navDirections: NavDirections = object : NavDirections {
                override fun getActionId(): Int {
                    return R.id.action_addArticleFragment_to_detailFragment
                }

                override fun getArguments(): Bundle {
                    return b
                }
            }
            Navigation.findNavController(binding.root).navigate(navDirections)
        }
    }

    fun choosePicture(view: View?) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_OPEN_DOCUMENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            requireActivity().contentResolver.takePersistableUriPermission(filePath!!, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, filePath)
                binding.imageView3.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun valideImage(): Boolean {
        var valid = true
        if (filePath == null) {
            valid = false
            Snackbar.make(binding.imageView3, "Ajouter une Image ", Snackbar.LENGTH_LONG)
        }
        return valid
    }

    private fun isEmpty(): Boolean {
        var empty = false
        if (TextUtils.isEmpty(binding.description.text)) {
            empty = true
            binding.description.error = "Champ Vide"
        }
        return empty
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1000
    }
}