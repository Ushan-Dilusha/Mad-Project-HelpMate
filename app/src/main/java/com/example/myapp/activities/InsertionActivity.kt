package com.example.myapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myapp.models.EmployeeModel
import com.example.myapp.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {

    private lateinit var etAccName: EditText
    private lateinit var etAccEmail: EditText
    private lateinit var etAccMyOrders: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        etAccName = findViewById(R.id.etAccName)
        etAccEmail = findViewById(R.id.etAccEmail)
        etAccMyOrders = findViewById(R.id.etEmpSalary)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Employees")

        btnSaveData.setOnClickListener {
            saveEmployeeData()
        }
    }

    private fun saveEmployeeData() {

        //getting values
        val empName = etAccName.text.toString()
        val empAge = etAccEmail.text.toString()
        val empSalary = etEmpSalary.text.toString()

        if (empName.isEmpty()) {
            etAccName.error = "Please Enter Name"
        }
        if (empAge.isEmpty()) {
            etAccEmail.error = "Please enter Address"
        }
        if (empSalary.isEmpty()) {
            etEmpSalary.error = "Please Enter Telephone"
        }

        val empId = dbRef.push().key!!

        val employee = EmployeeModel(empId, empName, empAge, empSalary)

        dbRef.child(empId).setValue(employee)
            .addOnCompleteListener {
                Toast.makeText(this, "User registered successfully", Toast.LENGTH_LONG).show()

                etAccName.text.clear()
                etAccEmail.text.clear()
                etEmpSalary.text.clear()


            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }

    }

}