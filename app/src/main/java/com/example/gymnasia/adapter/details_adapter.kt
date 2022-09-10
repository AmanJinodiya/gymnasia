package com.example.gymnasia.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gymnasia.R
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class details_adapter(var list: ArrayList<Pair<String, HashMap<String, String>>>, var email: String) : RecyclerView.Adapter<details_adapter.myviewHolder>() {

lateinit var context : Context
    lateinit var db : FirebaseFirestore
    class myviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image = itemView.findViewById<ImageView>(R.id.image)
        var s1 = itemView.findViewById<TextView>(R.id.s1)
        var s2 = itemView.findViewById<TextView>(R.id.s2)
        var s3 = itemView.findViewById<TextView>(R.id.s3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myviewHolder {
        context = parent.context
        return myviewHolder(LayoutInflater.from(parent.context).inflate(R.layout.detail_list_item, parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: myviewHolder, position: Int) {
        val storageReference = FirebaseStorage.getInstance().reference.child(list.get(position).first)

        storageReference.downloadUrl.addOnSuccessListener {
//            Log.d("url",it.toString())
            Glide.with(context).load(it.toString()).into(holder.image)
        }

        if(list.get(position).second["set1"] == "") holder.s1.text = "0--0"
        else holder.s1.text = list.get(position).second["set1"]


        if(list.get(position).second["set2"] == "") holder.s2.text = "0--0"
        else holder.s2.text = list.get(position).second["set2"]


        if(list.get(position).second["set3"] == "") holder.s3.text = "0--0"
        else holder.s3.text = list.get(position).second["set3"]

        holder.itemView.setOnLongClickListener{
            db = FirebaseFirestore.getInstance()


            holder.s1.setOnClickListener{
               number_picker("1",list.get(position).first,position,holder.s1)

            }

            holder.s2.setOnClickListener{
                number_picker("2", list.get(position).first, position, holder.s2)
            }

            holder.s3.setOnClickListener{
                number_picker("3", list.get(position).first, position, holder.s3)
            }
            true
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun number_picker(setNo: String, i: String, pos: Int, s: TextView) {
//        val dialog = context?.let { Dialog(it) }
        val dialog = Dialog(context)

        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.number_picker)

            var n = dialog.findViewById<NumberPicker>(R.id.wegith)
            var reps = dialog.findViewById<NumberPicker>(R.id.reps)
            var set = dialog.findViewById<TextView>(R.id.set_no)
            var update = dialog.findViewById<TextView>(R.id.update)
            set.text = setNo

            val map = HashMap<String,HashMap<String,String>>()
            val na = i.split("/")
            var sz = na.get(1).length
            var nam = na.get(1).substring(0,sz-4)
            val det = db.collection("USERS").document(email).collection("workout").document(na[0])
            val value = arrayOf("2.5","5","7.5","10","12.5","15","17.5","20","22.5","25","27.5","30","32.5","35","37.5","40","42.5","45","47.5","50","52.5","55","57.5","60","62.5","65")

            with(n)
            {
                this.minValue = 0
                this.maxValue = 25
                this.value = 4

                this.wrapSelectorWheel = true
                this.textColor = Color.BLACK
                this.textSize= 80F
                this.displayedValues = value

            }

            with(reps)
            {
                this.minValue = 1
                this.maxValue = 20
                this.wrapSelectorWheel = true
                this.textColor = Color.BLACK
                this.textSize= 80F
                this.value = 12

            }


            update.setOnClickListener{
//                val hash : HashMap<String,String> = HashMap()
                var temp : String = ""
                list.get(pos).second["set"+setNo] = value.get(n.value) + "*" + reps.value
                temp = list.get(pos).second["set"+setNo].toString()
                map[nam] = list.get(pos).second
                s.text = temp
                det.update(map as Map<String, Any>)
                Toast.makeText(context,"Updated",Toast.LENGTH_SHORT).show()
            }

            dialog.show()
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            dialog.window!!.attributes.windowAnimations = R.style.DilogAnimation
            dialog.window!!.setGravity(Gravity.CENTER)



        }


    }
    fun remove(position: Int){

        var t = list.get(position).first
        list.removeAt(position)

        val na = t.split("/")
        var sz = na.get(1).length
        var nam = na.get(1).substring(0,sz-4)
        val updates = hashMapOf<String, Any>(
            nam to FieldValue.delete()
        )
        db = FirebaseFirestore.getInstance()
        val det = db.collection("USERS").document(email).collection("workout").document(na[0]).update(updates).addOnSuccessListener {
            Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show()
        }




        notifyDataSetChanged()
    }
}
