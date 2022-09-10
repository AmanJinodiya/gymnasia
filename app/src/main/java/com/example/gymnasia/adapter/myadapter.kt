package com.example.gymnasia.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gymnasia.R
import com.example.gymnasia.dashboard.details
import java.util.*
import kotlin.collections.ArrayList

class myadapter(var list : ArrayList<String>,var email : String) : RecyclerView.Adapter<myadapter.myviewHolder>() {

    lateinit var context : Context
    var updated_list = list
    class myviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name = itemView.findViewById<TextView>(R.id.name)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myviewHolder {
        context = parent.context
        return myviewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: myviewHolder, position: Int) {
        holder.name.text = updated_list.get(position)
        holder.itemView.setOnClickListener{
            context.startActivity(Intent(context, details::class.java).putExtra("body_part_name",updated_list.get(position)).putExtra("email",email))
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