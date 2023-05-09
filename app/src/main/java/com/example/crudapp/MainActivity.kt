package com.example.crudapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var btnAddData: Button
    private lateinit var btnViewData: Button
    private lateinit var eventcont: TextView
    private lateinit var firebaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get a reference to the Firebase database
        firebaseRef = FirebaseDatabase.getInstance().reference

        btnAddData = findViewById(R.id.addBtn)
        btnViewData = findViewById(R.id.viewBtn)
        eventcont = findViewById(R.id.eventcount)

        btnAddData.setOnClickListener {
            val intent = Intent(this, AddData::class.java)
            startActivity(intent)
        }

        btnViewData.setOnClickListener {
            val intent = Intent(this, ViewData::class.java)
            startActivity(intent)
        }

        // Fetch the count of events from the Firebase database and display it in the TextView
        firebaseRef.child("Events").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val eventCount = dataSnapshot.childrenCount.toInt()
                eventcont.text = eventCount.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors here
            }
        })
    }
}
