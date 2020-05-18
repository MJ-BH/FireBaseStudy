package com.example.firebasestudy.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.firebasestudy.databinding.FragmentProfilBinding
import com.example.firebasestudy.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfilBinding
    private var mAuth: FirebaseAuth? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mRefrence: DatabaseReference? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfilBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFireBase()
        getData()
    }

    private fun getData() {
        mRefrence?.child(mAuth?.uid!!)?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val mUser = dataSnapshot.getValue(User::class.java)
                Glide.with(activity!!).load(mUser?.url).into(binding.addimage)
                binding.mailInscri.setText(mUser?.mail)
                binding.nomInscri.setText(mUser?.nom)
                binding.prenomInscri.setText(mUser?.prenom)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("eror", databaseError.message)
            }
        })
    }

    private fun initFireBase() {
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()
        mRefrence = mDatabase?.getReference("user")
    }
}