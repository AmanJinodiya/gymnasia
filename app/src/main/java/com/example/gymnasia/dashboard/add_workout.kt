package com.example.gymnasia.dashboard

import android.content.ClipData
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.gymnasia.adapter.SwipeGesture
import com.example.gymnasia.adapter.add_workout_adapter
import com.example.gymnasia.databinding.ActivityAddWorkoutBinding
import com.google.firebase.storage.FirebaseStorage

class add_workout : AppCompatActivity() {
    lateinit var binding : ActivityAddWorkoutBinding
    lateinit var bodypart_name : String
    lateinit var email : String

    lateinit var xc : add_workout_adapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        var list = intent.getStringArrayListExtra("list")
//        Log.d("size",list!!.size.toString())




        bodypart_name = intent.getStringExtra("body_part_name").toString()
        email = intent.getStringExtra("email").toString()
        var exclude : HashMap<String,Boolean> = intent.getSerializableExtra("exclude") as HashMap<String,Boolean>


        binding.textView5.text = bodypart_name

        val hash = HashMap<String,Int>()


        var array = ArrayList<String>()
        Log.d("aman",bodypart_name)
        val storageReference = FirebaseStorage.getInstance().reference.child(bodypart_name+"/").listAll().addOnSuccessListener {
            var z = it.items
            var k = 0

            for(i in z )
            {
                var name = bodypart_name+"/"+ i.name
                if(exclude.containsKey(name) == false)
                {
                    array.add(name)
                }

            }

            if(array.size == 0)
            {
                binding.recycle.visibility = View.GONE
                binding.load.visibility = View.VISIBLE
            }
            else{
            with(binding.recycle)
            {
                xc = add_workout_adapter(array,hash,binding.nowAdd,email,bodypart_name)
                this.adapter = xc
                this.layoutManager = GridLayoutManager(this@add_workout,2)

                binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {

                        return false
                    }
                    override fun onQueryTextChange(newText: String): Boolean {
                        xc.getFilter().filter(newText)
                        return false
                    }
                })
            }

        }




        }





    }

    override fun onBackPressed() {
        super.onBackPressed()
        var inte = Intent(this,details::class.java).putExtra("body_part_name",bodypart_name).putExtra("email",email)
        finish()
        startActivity(inte)
    }
}