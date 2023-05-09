package com.example.crudapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.firebase.database.*

class ViewData : AppCompatActivity() {

    private lateinit var eventRecyclerView: RecyclerView
    private lateinit var loadingText : TextView
    private lateinit var eventLst : ArrayList<Events>
    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_data)

        val backNavImageView = findViewById<ImageView>(R.id.iv_backnav)
        backNavImageView.setOnClickListener {
            onBackPressed()
        }

        eventRecyclerView = findViewById(R.id.eventView)
        eventRecyclerView.layoutManager = LinearLayoutManager(this)
        eventRecyclerView.setHasFixedSize(true)
        loadingText = findViewById(R.id.loading)

        eventLst = arrayListOf<Events>()

        getEventListData()
    }
    private fun getEventListData(){
        eventRecyclerView.visibility = View.GONE
        loadingText.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Events")
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                eventLst.clear()
                if(snapshot.exists()){
                    for(event in snapshot.children){
                        val eventData = event.getValue(Events::class.java)
                        eventLst.add(eventData!!)
                    }
                    val eAdapter = EventAdapter(eventLst)
                    eventRecyclerView.adapter = eAdapter

                    eAdapter.setItemClickListner(object :EventAdapter.onItemClickListner{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@ViewData,ViewContent::class.java)

                            //add extras
                            intent.putExtra("eventId",eventLst[position].eventID)
                            intent.putExtra("eventName",eventLst[position].eventName)
                            intent.putExtra("eventAssoc",eventLst[position].association)
                            intent.putExtra("eventDescrip",eventLst[position].descrption)
                            startActivity(intent)
                        }

                    })

                    eventRecyclerView.visibility = View.VISIBLE
                    loadingText.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}