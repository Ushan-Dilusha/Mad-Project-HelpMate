package com.example.myapp.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.myapp.models.fam_FamilyModel
import com.example.myapp.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class fam_InsertionActivity : AppCompatActivity() {

    private lateinit var etFamName: EditText
    private lateinit var etFamAdd: EditText
    private lateinit var etFamDes: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fam_activity_insertion)

        val backNavImageView = findViewById<ImageView>(R.id.iv_backnav)
        backNavImageView.setOnClickListener {
            onBackPressed()
        }

        etFamName = findViewById(R.id.etFamName)
        etFamAdd = findViewById(R.id.etFamAdd)
        etFamDes = findViewById(R.id.etFamDes)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Families")

        btnSaveData.setOnClickListener {
            saveFamilyData()
        }
    }

    private fun saveFamilyData() {

        //getting values
        val famName = etFamName.text.toString()
        val famAdd = etFamAdd.text.toString()
        val famDes = etFamDes.text.toString()

        if (famName.isEmpty()) {
            etFamName.error = "Please Enter Name"
        }
        else if (famAdd.isEmpty()) {
            etFamAdd.error = "Please Enter Address"
        }
        else if (famDes.isEmpty()) {
            etFamDes.error = "Please Enter Description"
        }
        else{

        val famId = dbRef.push().key!!

        val family = fam_FamilyModel(famId, famName, famAdd, famDes)

        dbRef.child(famId).setValue(family)
            .addOnCompleteListener {
                Toast.makeText(this, "Family registered successfully", Toast.LENGTH_LONG).show()

                etFamName.text.clear()
                etFamAdd.text.clear()
                etFamDes.text.clear()


            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }

    }

 }
}