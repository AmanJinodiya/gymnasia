package com.example.gymnasia.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.shapes.Shape

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gymnasia.databinding.ListItemAddWorkoutBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class add_workout_adapter(
    val list: ArrayList<String>,
    val hash: HashMap<String, Int>,
    val nowAdd: ImageView,
    val email : String,
    val body_part_name : String
) : RecyclerView.Adapter<add_workout_adapter.MYviewHolder>() {

    var updated_list :  ArrayList<String> = list

    lateinit var context : Context
    lateinit var db : FirebaseFirestore
    class MYviewHolder(val binding : ListItemAddWorkoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MYviewHolder {
        context = parent.context
        return MYviewHolder(ListItemAddWorkoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MYviewHolder, position: Int) {

        with(holder)
        {
            with(updated_list.get(position))
            {

                val n = this.split("/")
                var sz = n.get(1).length
                var nam = n.get(1).substring(0,sz-4)
                binding.name.text = nam
                binding.check.visibility = View.GONE
                FirebaseStorage.getInstance().reference.child(this).downloadUrl.addOnSuccessListener {
                    Glide.with(context).load(it.toString()).into(binding.image)
                }
            }


            if(hash.containsKey(updated_list.get(position)))
            {
                binding.check.visibility = View.VISIBLE
            }


            itemView.setOnClickListener{
                var party = Party(
                    speed = 0f,
                    maxSpeed = 30f,
                    damping = 0.9f,
                    spread = 360,
                    colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                    emitter = Emitter(duration = 1000L,
                        TimeUnit.MILLISECONDS).max(250),
                    position = Position.Relative(0.5, 0.3)
                )
                binding.kanfetti.start(party)


                if(hash.containsKey(updated_list.get(position)))
                {
                    binding.check.visibility = View.GONE
                    hash.remove(updated_list.get(position))
                }
                else
                {
                    hash[updated_list.get(position)] = 1

                    binding.check.visibility = View.VISIBLE
                }

                if(hash.size != 0)
                {
                    nowAdd.visibility = View.VISIBLE
                }
                else  nowAdd.visibility = View.GONE
            }

            nowAdd.setOnClickListener{
                db = FirebaseFirestore.getInstance()
                val det = db.collection("USERS").document(email).collection("workout").document(body_part_name)
                val map = HashMap<String,HashMap<String,String>>()
                for(i in hash)
                {
                    val n = i.key.split("/")
                    var sz = n.get(1).length
                    var nam = n.get(1).substring(0,sz-4)
                    Log.d("aman",nam)
                    var h = hashMapOf(
                        "set1" to "",
                        "set2" to "",
                        "set3" to "",
                    )

                    map[nam] = h
                }
//                det.set(map)
                det.update(map as Map<String, Any>)

                Toast.makeText(context,"SucessFully Added",Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun getItemCount(): Int {
      return updated_list.size
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults? {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    updated_list = list
                } else {
                    val resultList = java.util.ArrayList<String>()
                    for (row in list) {
                        if (row.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    updated_list = resultList

                }
                val filterResults = FilterResults()
                filterResults.values = updated_list
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                updated_list =
                    results?.values as java.util.ArrayList<String> /* = java.util.ArrayList<java.io.File> */
                notifyDataSetChanged()
            }


        }


    }


}