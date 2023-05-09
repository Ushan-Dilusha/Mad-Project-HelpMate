package com.example.crudapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class EditEvent : AppCompatActivity() {

    private lateinit var nameEdt : EditText
    private lateinit var idEdt : TextView
    private lateinit var assocEdt : EditText
    private lateinit var descEdt : EditText
    private lateinit var updateBtn : Button
    private lateinit var backBtn : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_event)
        nameEdt = findViewById(R.id.editName)
        assocEdt = findViewById(R.id.editAsso)
        descEdt = findViewById(R.id.editDesc)
        updateBtn = findViewById(R.id.updateBtn)
        //backBtn = findViewById(R.id.deleteBtn)
        setValuesToViews()

        val backNavImageView = findViewById<ImageView>(R.id.iv_backnav)
        backNavImageView.setOnClickListener {
            onBackPressed()
        }

        updateBtn.setOnClickListener(){
            var currentId = intent.getStringExtra("eventId")
            var name = nameEdt.text.toString()
            var assoc = assocEdt.text.toString()
            var desc = descEdt.text.toString()

            var dbRef =
                currentId?.let { it1 ->
                    FirebaseDatabase.getInstance().getReference("Events").child(
                        it1
                    )
                }
            var eventInfo = Events(currentId,name,assoc, desc)
            dbRef?.setValue(eventInfo)?.addOnCompleteListener {
                Toast.makeText(this, "Update Success", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ViewData::class.java)
                startActivity(intent)
            }?.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun setValuesToViews(){
        nameEdt.setText(intent.getStringExtra("eventName"))
        assocEdt.setText(intent.getStringExtra("eventAssoc"))
        descEdt.setText(intent.getStringExtra("eventDescrip"))
    }


}




