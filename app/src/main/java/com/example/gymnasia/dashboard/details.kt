package com.example.gymnasia.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymnasia.adapter.SwipeGesture
import com.example.gymnasia.adapter.details_adapter
import com.example.gymnasia.databinding.ActivityDetailsBinding
import com.google.firebase.firestore.FirebaseFirestore

class details : AppCompatActivity() {
    lateinit var binding : ActivityDetailsBinding

    lateinit var db : FirebaseFirestore
    lateinit var bodypart_name : String
    lateinit var email : String
    lateinit var xc : details_adapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.det.visibility = View.GONE
        var max = hashMapOf<String,String>()

        db = FirebaseFirestore.getInstance()



        var exclude = HashMap<String,Boolean>()
        bodypart_name = intent.getStringExtra("body_part_name").toString()
        email = intent.getStringExtra("email").toString()
        binding.add.setOnClickListener {

            var inte = Intent(this, add_workout::class.java).putExtra(
                "body_part_name",
                bodypart_name
            ).putExtra("email", email)
                .putExtra("exclude",exclude)
            finish()
            startActivity(inte)

        }
//        db.collection("USERS").document(email).collection("workout")
//            .document(bodypart_name).set(max)

        var list = ArrayList<Pair<String, HashMap<String, String>>>()

        val workout = db.collection("USERS").document(email).collection("workout")
            .document(bodypart_name).get().addOnSuccessListener {


            val data = it.data
                if(data!!.size == 0)
                {
                    binding.load.visibility = View.VISIBLE
                    binding.det.visibility = View.GONE
                    binding.recycle.visibility = View.GONE
                }
                else binding.det.visibility = View.VISIBLE

            if (data != null) {
                lateinit var z: String
                for (i in data) {
                    Log.d("Aman", i.value.toString())
                    z = bodypart_name + "/" + i.key.toString() + ".gif"
                    exclude[z] = true
                    list.add(Pair(z, i.value) as Pair<String, HashMap<String, String>>)
//                    var localFile = File.createTempFile("images", "gif")
                }
                xc = details_adapter(list, email)
                binding.recycle.adapter = xc
                binding.recycle.layoutManager = LinearLayoutManager(this)

            }
        }
        val swipeGesture = object : SwipeGesture(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                when(direction)
                {
                    ItemTouchHelper.LEFT->{
                        xc.remove(viewHolder.adapterPosition)
                        Toast.makeText(this@details,"removed", Toast.LENGTH_SHORT).show()
                    }

                }


            }
        }

        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(binding.recycle)


//        Glide.with(this).load(storageReference).into(binding.image)


    }
}