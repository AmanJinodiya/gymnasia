package com.example.gymnasia.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.gymnasia.R
import com.example.gymnasia.dashboard.dash
import com.example.gymnasia.dashboard.details
import com.example.gymnasia.databinding.FragmentSigninBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

class signin : Fragment() {

    lateinit var binding : FragmentSigninBinding
    private lateinit var auth: FirebaseAuth
    lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSigninBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        binding.regBtn.setOnClickListener {

            binding.details.visibility = View.GONE
            binding.load.visibility = View.VISIBLE
            binding.regBtn.isEnabled = false
            var email = binding.regName.editText!!.text.toString()
            var password = binding.regPassword.editText!!.text.toString()

            if(!email.isEmpty() or !password.isEmpty() or (password.length > 8))
            {
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener{
                        if(it.isSuccessful)
                        {
                            Toast.makeText(context,"Log in succesfull",Toast.LENGTH_SHORT).show()
                            startActivity(Intent(context,dash::class.java).putExtra("Email",email))
                            getActivity()?.getFragmentManager()?.popBackStack();


                        }
                        else {
                            Toast.makeText(context, "wrong email or password", Toast.LENGTH_SHORT)
                                .show()
                            binding.details.visibility = View.VISIBLE
                            binding.load.visibility = View.GONE
                            binding.regBtn.isEnabled = true
                        }
                    }
            }
            else {
                Toast.makeText(context, "Fields are not correct", Toast.LENGTH_SHORT).show()
                binding.details.visibility = View.VISIBLE
                binding.load.visibility = View.GONE
                binding.regBtn.isEnabled = true
            }

        }

        binding.regLoginBtn.setOnClickListener{
            var frag = signup()
            var fragmentManager : FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
            fragmentManager.replace(R.id.cont,frag).commit()
        }

        binding.phoneAuthButton.setOnClickListener {
            startActivity(Intent(context,otp_login::class.java))
        }

    }

    private fun get_mobile_no_fromemail(email: String): Any {
        var phone: String = ""
        val doc_ref = db.collection("USERS").document(email)
        doc_ref.get().addOnSuccessListener {
            if(it != null)
            {
               phone = it.data?.get("phone")?.toString() ?: "-1"
            }
            else
            {
                Toast.makeText(context,"No User Exist",Toast.LENGTH_SHORT).show()
            }
        }
            .addOnFailureListener { exception ->
                Toast.makeText(context,"Couldn't get User Detail",Toast.LENGTH_SHORT).show()
            }

        return phone
    }

}