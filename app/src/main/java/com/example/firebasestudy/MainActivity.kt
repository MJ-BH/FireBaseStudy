package com.example.firebasestudy

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.firebasestudy.databinding.ActivityMainBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    var appBarConfiguration: AppBarConfiguration? = null
    private lateinit var binding: ActivityMainBinding
    private var mAuth: FirebaseAuth? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mRefrence: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFireBase()
        saveToken()
        subscribeTotopic()
        val navView = binding.navView


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard)
                .build()
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration!!)
        NavigationUI.setupWithNavController(navView, navController)
        binding.fab.setOnClickListener { navController.navigate(R.id.addArticleFragment) }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return (NavigationUI.navigateUp(navController, this.appBarConfiguration!!)
                || super.onSupportNavigateUp())
    }

    private fun initFireBase() {
        mAuth = FirebaseAuth.getInstance()
        //get la base de donné
        mDatabase = FirebaseDatabase.getInstance()
        // le curseur
        mRefrence = mDatabase?.getReference("user")
    }

    private fun saveToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task: Task<InstanceIdResult?>? ->
            if (!task?.isSuccessful!!) {
                Log.e("Main", "getInstanceId failed", task.exception)
                return@addOnCompleteListener
            } else {
                val token: String = task.result?.token!!
                mRefrence?.child(mAuth?.uid!!)?.child("token")?.setValue(token)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.e("token", "token updated :$token")
                    }
                }
            }
        }
    }

    private fun subscribeTotopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("article").addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("erro", "failed ti subscribe")
            } else {
                Log.e("topic", "SUcces")
            }
        }
    }
}