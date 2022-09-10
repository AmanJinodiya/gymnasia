package com.example.gymnasia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextPaint
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.gymnasia.databinding.ActivityMainBinding
import com.example.gymnasia.dataclasses.user_registration
import com.example.gymnasia.signup.signin
import com.example.gymnasia.signup.signup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var root : FirebaseDatabase
    lateinit var refrence : DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var frag = signin()
        var fragmentManager : FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentManager.replace(R.id.cont,frag).commit()



    }
}