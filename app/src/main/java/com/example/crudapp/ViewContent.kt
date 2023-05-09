package com.example.crudapp

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class ViewContent : AppCompatActivity() {

    private lateinit var img : ImageView
    private lateinit var evtName : TextView
    private lateinit var evtAssoc : TextView
    private lateinit var desc : TextView
    private lateinit var updateBtn : Button
    private lateinit var deleteBtn : Button
    private lateinit var backBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_content)

        img = findViewById(R.id.imgFull)
        evtName = findViewById(R.id.evNamelbl)
        evtAssoc = findViewById(R.id.evAssoclbl)
        desc = findViewById(R.id.evDesclbl)
        updateBtn = findViewById(R.id.updateButton)
        deleteBtn = findViewById(R.id.delButton)

        setValuesToViews()

        val backNavImageView = findViewById<ImageView>(R.id.iv_backnav)
        backNavImageView.setOnClickListener {
            onBackPressed()
        }

        var dialogBox = AlertDialog.Builder(this)
        deleteBtn.setOnClickListener{

            dialogBox.setTitle("Alert")
            dialogBox.setMessage("Are you sure want to delete this event?")
            dialogBox.setCancelable(true)
            dialogBox.setPositiveButton("Yes"){
                    _, it ->

               var myId = intent.getStringExtra("eventId")
                var dbRef1 = myId?.let { it1 ->
                    FirebaseDatabase.getInstance().getReference("Events").child(
                        it1
                    )
                }
                val task = dbRef1?.removeValue()
                task?.addOnCompleteListener(){
                    Toast.makeText(this, "Delete Success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ViewData::class.java)
                    finish()
                    startActivity(intent)
                }
                task?.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
                }



        }
            dialogBox.setNegativeButton("No"){
                    btn, it ->
                btn.cancel()
            }
            dialogBox.show()

        }

         updateBtn.setOnClickListener{
             var name = intent.getStringExtra("eventName")
             var assoc = intent.getStringExtra("eventAssoc")
             var description= intent.getStringExtra("eventDescrip")
             var id = intent.getStringExtra("eventId")
            val intent = Intent(this, EditEvent::class.java)
             intent.putExtra("eventId",id)
             intent.putExtra("eventName",name)
             intent.putExtra("eventAssoc",assoc)
             intent.putExtra("eventDescrip",description)
            startActivity(intent)
        }

    }

    private fun setValuesToViews(){
        evtName.text = intent.getStringExtra("eventName")
        evtAssoc.text = intent.getStringExtra("eventAssoc")
        desc.text = intent.getStringExtra("eventDescrip")
        var id = intent.getStringExtra("eventId")
        var findImg = id?.let {
            FirebaseStorage.getInstance().getReference("Events").child(it) }

        findImg?.downloadUrl?.addOnSuccessListener {
            Picasso.get().load(it).into(img)
        }

    }
}