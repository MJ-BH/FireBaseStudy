package com.example.firebasestudy

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasestudy.databinding.ActivityInscriptionBinding
import com.example.firebasestudy.model.User
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException

class InscriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInscriptionBinding
    private var filePath: Uri? = null
    private var mAuth: FirebaseAuth? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mRefrence: DatabaseReference? = null
    private var mStorage: FirebaseStorage? = null
    private val mUser: User? = User()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInscriptionBinding.inflate(layoutInflater)
        val view: View? = binding.root
        setContentView(view)
        initFireBAse()
        binding.addimage.setOnClickListener { view: View? -> choosePicture(view) }
        binding.inscriBtn.setOnClickListener { save() }
    }

    private fun save() {
        if (!isEmpty()) {
            val mail: String? = binding.mailInscri.text.toString()
            val pwd: String? = binding.pwdInscri.text.toString()
            if (validEmail() && validPassword() && valideImage()) {
                binding.progressBar.visibility = View.VISIBLE
                mAuth?.createUserWithEmailAndPassword(mail!!, pwd!!)?.addOnCompleteListener(OnCompleteListener { task: Task<AuthResult?>? ->
                    if (task!!.isSuccessful) {
                        mUser?.uid=mAuth?.currentUser?.uid
                        mUser?.mail = mail
                        uploadImage()
                    } else {
                        binding.progressBar.visibility = View.GONE
                        Snackbar.make(binding.root, task.exception?.message!!, Snackbar.LENGTH_LONG)
                    }
                })
            }
        }
    }

    private fun uploadImage() {
        val child: StorageReference? = mStorage?.reference?.child(mUser?.uid!!)
        child?.putFile(filePath!!)?.continueWithTask { child.downloadUrl }?.addOnCompleteListener { task: Task<Uri?>? ->
            if (task!!.isSuccessful) {
                mUser?.url=task.result.toString()
                saveUser()
            }
        }
    }

    private fun saveUser() {
        val nom: String? = binding.nomInscri.text.toString()
        mUser?.nom = nom
        mUser?.prenom =binding.prenomInscri.text.toString()
        mRefrence?.child(mUser?.uid!!)?.setValue(mUser)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                sendVerificationEmail()
                binding.progressBar.visibility = View.GONE
                startActivity(Intent(this@InscriptionActivity, MainActivity::class.java))
                finish()
            } else {
                binding.progressBar.visibility = View.GONE
                Snackbar.make(binding.root, task.exception?.message!!, Snackbar.LENGTH_LONG)
            }
        }
    }

    private fun sendVerificationEmail() {
        mAuth?.currentUser?.sendEmailVerification()?.addOnCompleteListener(OnCompleteListener { task: Task<Void?>? -> Log.e("mailsend", task.toString()) })
    }

    private fun choosePicture(view: View?) {
        val intent: Intent? = Intent()
        intent?.type = "image/*"
        intent?.action = Intent.ACTION_OPEN_DOCUMENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == PICK_IMAGE_REQUEST) && (resultCode == Activity.RESULT_OK
                        ) && (data != null) && (data.data != null)) {
            filePath = data.data
            contentResolver.takePersistableUriPermission(this.filePath!!, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            try {
                val bitmap: Bitmap? = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                binding.addimage.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun isEmpty(): Boolean {
        var empty: Boolean = false
        if (TextUtils.isEmpty(binding.mailInscri.text)) {
            empty = true
            binding.mailInscri.error = "Champ Vide"
        }
        if ((TextUtils.isEmpty(binding.pwdInscri.text))) {
            empty = true
            binding.pwdInscri.error = "Champ Vide"
        }
        if (TextUtils.isEmpty(binding.nomInscri.text)) {
            empty = true
            binding.nomInscri.error = "Champ Vide"
        }
        if (TextUtils.isEmpty(binding.prenomInscri.text)) {
            empty = true
            binding.prenomInscri.error = "Champ Vide"
        }
        return empty
    }

    private fun validEmail(): Boolean {
        var valid: Boolean = true
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.mailInscri.text.toString()).matches()) {
            valid = false
            binding.mailInscri.error = " entrer un email Valide"
        }
        return valid
    }

    private fun validPassword(): Boolean {
        var valide: Boolean = true
        if (binding.pwdInscri.text?.length!! < 6) {
            valide = false
            binding.pwdInscri.error = "minimum 6 caractéres "
        }
        return valide
    }

    private fun valideImage(): Boolean {
        var valid: Boolean = true
        if (filePath == null) {
            valid = false
            Snackbar.make(binding.addimage, "Ajouter une Image ", Snackbar.LENGTH_LONG)
        }
        return valid
    }

    private fun initFireBAse() {
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()
        mRefrence = mDatabase?.getReference("user")
        mStorage = FirebaseStorage.getInstance()
    }

    companion object {
        private const val PICK_IMAGE_REQUEST: Int = 1000
    }
}