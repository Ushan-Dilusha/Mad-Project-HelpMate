package com.example.crudapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.squareup.picasso.Picasso

class EventAdapter (private val eventList : ArrayList<Events>) : RecyclerView.Adapter<EventAdapter.ViewHolder>(){

    private lateinit var myListner: onItemClickListner
    interface onItemClickListner{
        fun onItemClick(position: Int)
    }

    fun setItemClickListner(clickListner: onItemClickListner){
        myListner = clickListner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_card,parent,false)
        return ViewHolder(itemView, myListner)
    }

    override fun onBindViewHolder(holder: EventAdapter.ViewHolder, position: Int) {
        val currentEvent = eventList[position]
        holder.lblEventName.text = currentEvent.eventName
        holder.lblAssociation.text = currentEvent.association
       // holder.lblDescription.text = currentEvent.descrption
        var eId = currentEvent.eventID

        try {
            val findImg =
                currentEvent.eventID?.let {
                    FirebaseStorage.getInstance().getReference("Events").child(it)
                }
            if(findImg!=null){
                FirebaseStorage.getInstance().getReference("Events").child(eId!!).downloadUrl
                    .addOnSuccessListener {
                        Picasso.get().load(it).into(holder.img)
                    }.addOnFailureListener{

                    }
            }
            else{
                println()
            }
        }catch (_: StorageException){

        }



    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    class ViewHolder(view : View, clickListner: onItemClickListner) : RecyclerView.ViewHolder(view) {
        val lblEventName : TextView = itemView.findViewById(R.id.lblName)
        val lblAssociation : TextView = itemView.findViewById(R.id.lblAssociation)
        //val lblDescription : TextView = itemView.findViewById(R.id.lblDescription)
        val img : ImageView = itemView.findViewById((R.id.eventImg))

        init {
            itemView.setOnClickListener{
                clickListner.onItemClick(adapterPosition)
            }
        }
    }

}