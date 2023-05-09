package com.example.myapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myapp.models.FamilyModel
import com.example.myapp.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {

    private lateinit var etFamName: EditText
    private lateinit var etFamAdd: EditText
    private lateinit var etFamDes: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

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
        if (famAdd.isEmpty()) {
            etFamAdd.error = "Please Enter Address"
        }
        if (famDes.isEmpty()) {
            etFamDes.error = "Please Enter Description"
        }

        val famId = dbRef.push().key!!

        val family = FamilyModel(famId, famName, famAdd, famDes)

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