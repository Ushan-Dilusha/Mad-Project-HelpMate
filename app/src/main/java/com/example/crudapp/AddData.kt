package com.example.crudapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.crudapp.databinding.ActivityAddDataBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddData : AppCompatActivity() {
    private lateinit var addBtn : Button
    private lateinit var backBtn : Button
    private lateinit var edtName : EditText
    private lateinit var edtAssoc : EditText
    private lateinit var edtDesc : EditText
    private lateinit var dbRef:DatabaseReference
    private lateinit var binding : ActivityAddDataBinding
    private lateinit var storageReference: StorageReference
    private var imageUri : Uri?=null
    private lateinit var uploadImg : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addBtn = findViewById(R.id.btnAdd)
        //backBtn = findViewById(R.id.btnBack)
        edtName = findViewById(R.id.txtName)
        edtAssoc = findViewById(R.id.txtAssosiation)
        edtDesc = findViewById(R.id.txtDescrip)
        uploadImg = findViewById(R.id.addImage)

        val backNavImageView = findViewById<ImageView>(R.id.iv_backnav)
        backNavImageView.setOnClickListener {
            onBackPressed()
        }


        dbRef = FirebaseDatabase.getInstance().getReference("Events")

        var pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){
            binding.addImage.setImageURI(it)
            if (it != null){
                imageUri = it
            }
        }

        binding.btnAdd.setOnClickListener{
            addDataToDB()

        }

        binding.fabAdd.setOnClickListener{
           pickImage.launch("image/*")
        }



    }

    private fun addDataToDB(){
        //getting values
        val eventName = edtName.text.toString().trim()
        val association = edtAssoc.text.toString().trim()
        val description = edtDesc.text.toString().trim()

        if (eventName.isEmpty()){
            edtName.error = "Please Enter Event Name"
        }
        else if (association.isEmpty()){
            edtAssoc.error = "Please Enter Contribute Association"
        }
        else if (description.isEmpty()){
            edtDesc.error = "Please Enter Description"
        }
        else{
            //add unique event id
            val eventId = dbRef.push().key!!
            val events = Events(eventId, eventName, association, description)

            dbRef.child(eventId).setValue(events).
            addOnCompleteListener {
                if (imageUri != null){
                    addImageToDB(eventId)
                }
                Toast.makeText(this, "Data Added Success",Toast.LENGTH_SHORT).show()

                edtName.text.clear()
                edtDesc.text.clear()
                edtAssoc.text.clear()
            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}",Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun addImageToDB(eventID : String){
        storageReference = FirebaseStorage.getInstance().getReference("Events").child(eventID)
        storageReference.putFile(imageUri!!).addOnSuccessListener {
            Toast.makeText(this, "Image Upload Success",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
                err ->
            Toast.makeText(this, "Error ${err.message}",Toast.LENGTH_SHORT).show()
        }
    }
}