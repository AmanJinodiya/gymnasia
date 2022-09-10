package com.example.gymnasia.signup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.gymnasia.R
import com.example.gymnasia.databinding.FragmentSigninBinding
import com.example.gymnasia.databinding.FragmentSignupBinding
import com.example.gymnasia.dataclasses.user_registration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class signup : Fragment() {

    lateinit var binding : FragmentSignupBinding

    lateinit var db : FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.regBtn.setOnClickListener{
            var name = binding.regName.editText!!.text.toString()
            var email = binding.regEmail.editText!!.text.toString()
            val phone = binding.regPhoneNo.editText!!.text.toString()
            val password = binding.regPassword.editText!!.text.toString()


            if(!email.isEmpty() or !password.isEmpty() or !(password.length < 8))
            {
                val user = hashMapOf(
                    "Name" to name,
                    "email" to email,
                    "phone" to phone,

                )



//                save user data first
                val users = db.collection("USERS")

                val query = users.whereEqualTo("email",email).get()
                    .addOnSuccessListener {
                        if(it.isEmpty)
                        {

                            auth.createUserWithEmailAndPassword(email,password)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        users.document(email).set(user)
                                        Toast.makeText(context, "Created Account", Toast.LENGTH_SHORT).show()
                                        var frag = signin()
                                        var fragmentManager: FragmentTransaction =
                                            activity?.supportFragmentManager!!.beginTransaction()
                                        fragmentManager.replace(R.id.cont, frag).commit()
                                    } else Toast.makeText(context, "Authentication fails", Toast.LENGTH_SHORT).show()
                                }

                        }
                        else
                        {
                            Toast.makeText(context,"User Already Registerd",Toast.LENGTH_SHORT).show()
                            var frag = signin()
                            var fragmentManager : FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
                            fragmentManager.replace(R.id.cont,frag).commit()
                        }
                    }



            }
            else Toast.makeText(context,"Fields are not correct",Toast.LENGTH_SHORT).show()

        }

        binding.regLoginBtn.setOnClickListener {
            Toast.makeText(context,"Account created ",Toast.LENGTH_SHORT).show()
            var frag = signin()
            var fragmentManager : FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
            fragmentManager.replace(R.id.cont,frag).commit()
        }


    }
}