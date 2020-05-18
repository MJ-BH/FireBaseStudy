package com.example.firebasestudy

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasestudy.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view: View? = binding.root
        setContentView(view)
        binding.loginBtn.setOnClickListener { Login() }
        binding.inscriBtn.setOnClickListener { startActivity(Intent(this@LoginActivity, InscriptionActivity::class.java)) }
    }

    private fun Login() {
        if (!isEmpty()) {
            val mail: String? = binding.emailInput.text.toString()
            val password: String? = binding.passwordInput.text.toString()
            if (validEmail() && validPassword()) {
                mAuth?.signInWithEmailAndPassword(mail!!, password!!)?.addOnCompleteListener { task: Task<AuthResult?>? ->
                    if (task!!.isSuccessful) {
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Snackbar.make(binding.root, task.exception?.message!!, Snackbar.LENGTH_LONG)
                    }
                }
            }
        }
    }

    private fun isEmpty(): Boolean {
        var empty: Boolean = false
        if (TextUtils.isEmpty(binding.emailInput.text)) {
            empty = true
            binding.emailInput.error = "Champ Vide"
        }
        if ((TextUtils.isEmpty(binding.passwordInput.text))) {
            empty = true
            binding.passwordInput.error = "Champ Vide"
        }
        return empty
    }

    private fun validEmail(): Boolean {
        var valid: Boolean = true
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailInput.text.toString()).matches()) {
            valid = false
            binding.emailInput.error = " entrer un email Valide"
        }
        return valid
    }

    private fun validPassword(): Boolean {
        var valide: Boolean = true
        if (binding.passwordInput.text?.length!! < 6) {
            valide = false
            binding.passwordInput.error = "minimum 6 caractéres "
        }
        return valide
    }
}