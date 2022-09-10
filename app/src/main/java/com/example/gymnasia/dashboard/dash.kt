package com.example.gymnasia.dashboard

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymnasia.MainActivity
import com.example.gymnasia.R
import com.example.gymnasia.adapter.myadapter
import com.example.gymnasia.databinding.ActivityDashBinding
import com.example.gymnasia.signup.otp_login
import com.example.gymnasia.signup.otpactivity
import com.google.firebase.firestore.FirebaseFirestore

class dash : AppCompatActivity() {
    lateinit var binding : ActivityDashBinding
    lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var xemail = "1"
        db = FirebaseFirestore.getInstance()




        val sharedpref = this.getPreferences(Context.MODE_PRIVATE) ?:return
        val isLogin: String = sharedpref.getString("Email","1")!!
        Log.d("aman","here")
        if(isLogin == "1")
        {
            var email = intent.getStringExtra("Email") as String?
            var phone =intent.getStringExtra("phone") as String?
            if(email != null)
            {
                with(sharedpref.edit())
                {
                    putString("Email",email)
                    apply()
                }
                xemail = email!!
            }
            else
            {
                startActivity(Intent(this,otp_login::class.java))
                finish()
            }
        }
        else
        {
            Log.d("aman",isLogin)
            xemail = isLogin
        }

        Log.d("aman",xemail)
        var list = ArrayList<String>()
        list.add("chest")
        list.add("back")
        list.add("biceps")
        list.add("triceps")
        list.add("legs")
        list.add("core")

        var add_workout_names = db.collection("USERS").document(xemail).collection("workout")
        add_workout_names.get().addOnSuccessListener {
            if(it.isEmpty)
            {
                for (s in list)
                {
                    var hash = HashMap<String,String>()
                    add_workout_names.document(s).set(hash)
                }
            }
        }


        val adapter = myadapter(list,xemail)

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                adapter.getFilter().filter(newText)
                return false
            }
        })
        binding.recycleView.adapter = adapter
        binding.recycleView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL ,false)


        binding.logout.setOnClickListener{
            sharedpref.edit().remove("Email").apply()
            startActivity(Intent(this,otp_login::class.java))
        }

    }
}